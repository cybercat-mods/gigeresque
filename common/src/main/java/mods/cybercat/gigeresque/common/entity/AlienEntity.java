package mods.cybercat.gigeresque.common.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.BiConsumer;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.nav.GigNavigation;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.entity.helper.managers.CrawlingManager;
import mods.cybercat.gigeresque.common.entity.helper.managers.SearchingManager;
import mods.cybercat.gigeresque.common.entity.helper.managers.StasisManager;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;

/**
 * TODO: Create new version of this class that will will use crawling library when ready.
 */
public abstract class AlienEntity extends Monster implements Enemy, VibrationSystem, Growable, AbstractAlien {

    public static final EntityDataAccessor<Boolean> FLEEING_FIRE = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.INT
    );

    public static final EntityDataAccessor<Boolean> IS_STASIS = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    private static final EntityDataAccessor<Boolean> IS_CRAWLING = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    public static final EntityDataAccessor<Boolean> WAKING_UP = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    protected static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.INT
    );

    protected static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.FLOAT
    );

    protected static final EntityDataAccessor<Boolean> IS_HISSING = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    protected static final EntityDataAccessor<Boolean> IS_SEARCHING = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    protected static final EntityDataAccessor<Boolean> IS_EXECUTION = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    protected static final EntityDataAccessor<Boolean> IS_HEADBITE = SynchedEntityData.defineId(
        AlienEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    private static final Logger LOGGER = LogUtils.getLogger();

    private final DynamicGameEventListener<Listener> dynamicGameEventListener;

    public int wakeupCounter = 0;

    public boolean inTwoBlockSpace = false;

    public int breakingCounter = 0;

    protected User vibrationUser;

    private Data vibrationData;

    public BlockPos savedNestWebCross;

    public SearchingManager searchingManager;

    public AnimationDispatcher animationDispatcher;

    public MoveAnalysis moveAnalysis;

    public final CrawlingManager crawlingManager;

    public final StasisManager stasisManager;

    protected AlienEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.noCulling = true;
        this.crawlingManager = new CrawlingManager(this, IS_CRAWLING);
        this.searchingManager = new SearchingManager(this, IS_SEARCHING);
        this.stasisManager = new StasisManager(this, IS_STASIS);
        this.vibrationUser = new AzureVibrationUser(this, 2.5F);
        this.vibrationData = new Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new Listener(this));
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 20, 10, 0.5F, 1.0F, true);
    }

    @Override
    public boolean onClimbable() {
        return this.fallDistance <= 0.1;
    }

    @Override
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluid) {}

    public static boolean checkMonsterSpawnRules(
        @NotNull EntityType<? extends Monster> type,
        ServerLevelAccessor level,
        @NotNull MobSpawnType spawnType,
        @NotNull BlockPos pos,
        @NotNull RandomSource random
    ) {
        return level.getDifficulty() != Difficulty.PEACEFUL
            && (MobSpawnType.ignoresLightRequirements(spawnType) || isDarkEnoughToSpawn(level, pos, random))
            && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    public static boolean isDarkEnoughToSpawn(ServerLevelAccessor level, @NotNull BlockPos pos, RandomSource random) {
        if (level.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensiontype = level.dimensionType();
            int i = dimensiontype.monsterSpawnBlockLightLimit();
            if (i < 15 && level.getBrightness(LightLayer.BLOCK, pos) > i) {
                return false;
            } else {
                int j = level.getLevel().isThundering()
                    ? level.getMaxLocalRawBrightness(
                        pos,
                        10
                    )
                    : level.getMaxLocalRawBrightness(pos);
                return j <= dimensiontype.monsterSpawnLightTest().sample(random);
            }
        }
    }

    @Override
    public float maxUpStep() {
        return 1.5f;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 150) {
            this.remove(RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience(this);
        }
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    public int getAcidDiameter() {
        return 0;
    }

    @Override
    public boolean isFleeing() {
        return this.entityData.get(FLEEING_FIRE);
    }

    @Override
    public void setFleeingStatus(boolean fleeing) {
        this.entityData.set(FLEEING_FIRE, fleeing);
    }

    @Override
    public void setWakingUpStatus(boolean passout) {
        this.entityData.set(WAKING_UP, passout);
    }

    @Override
    public boolean isWakingUp() {
        return this.entityData.get(WAKING_UP);
    }

    @Override
    public boolean isExecuting() {
        return entityData.get(IS_EXECUTION);
    }

    @Override
    public void setIsExecuting(boolean isExecuting) {
        entityData.set(IS_EXECUTION, isExecuting);
    }

    @Override
    public boolean isBiting() {
        return entityData.get(IS_HEADBITE);
    }

    @Override
    public void setIsBiting(boolean isBiting) {
        entityData.set(IS_HEADBITE, isBiting);
    }

    @Override
    public boolean isHissing() {
        return entityData.get(IS_HISSING);
    }

    @Override
    public void setIsHissing(boolean isHissing) {
        entityData.set(IS_HISSING, isHissing);
    }

    @Override
    public float getGrowth() {
        return entityData.get(GROWTH);
    }

    @Override
    public void setGrowth(float growth) {
        entityData.set(GROWTH, growth);
    }

    @Override
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLEEING_FIRE, false);
        builder.define(STATE, 0);
        builder.define(CLIENT_ANGER_LEVEL, 0);
        builder.define(GROWTH, 0.0f);
        builder.define(WAKING_UP, false);
        builder.define(IS_HISSING, false);
        builder.define(IS_EXECUTION, false);
        builder.define(IS_HEADBITE, false);
        // MOVED
        builder.define(IS_STASIS, false);
        builder.define(IS_SEARCHING, false);
        builder.define(IS_CRAWLING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData)
            .resultOrPartial(
                LOGGER::error
            )
            .ifPresent(tag -> compound.put("listener", tag));
        compound.putFloat("growth", this.getGrowth());
        compound.putBoolean("wakingup", this.isWakingUp());
        compound.putBoolean("isHissing", this.isHissing());
        compound.putBoolean("isExecuting", this.isExecuting());
        compound.putBoolean("isHeadBite", this.isBiting());
        stasisManager.save(compound);
        searchingManager.save(compound);
        crawlingManager.save(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        crawlingManager.load(compound);
        searchingManager.load(compound);
        stasisManager.load(compound);
        if (compound.contains("listener", 10))
            Data.CODEC.parse(
                new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))
            )
                .resultOrPartial(
                    LOGGER::error
                )
                .ifPresent(data -> this.vibrationData = data);
        this.setGrowth(compound.getFloat("getStatisTimer"));
        this.setGrowth(compound.getFloat("growth"));
        this.setIsHissing(compound.getBoolean("isHissing"));
        this.setIsBiting(compound.getBoolean(("isHeadBite")));
        this.setIsExecuting(compound.getBoolean("isExecuting"));
        this.setIsExecuting(compound.getBoolean("isHeadBite"));
        this.setWakingUpStatus(compound.getBoolean("wakingup"));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GigNavigation(this, level);
    }

    @Override
    protected boolean canRide(@NotNull Entity vehicle) {
        return false;
    }

    @Override
    public int calculateFallDamage(float fallDistance, float damageMultiplier) {
        if (fallDistance <= 15)
            return 0;
        return super.calculateFallDamage(fallDistance, damageMultiplier);
    }

    @Override
    public int getMaxFallDistance() {
        return 9;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        searchingManager.tick();
        stasisManager.tick();

        this.setAirSupply(this.getMaxAirSupply());
        if (level() instanceof ServerLevel serverLevel) {
            if (this.isAlive())
                this.grow(this, 1 * getGrowthMultiplier());
            if (this.isVehicle())
                this.setAggressive(false);
            if (this.tickCount % Constants.TPS == 0 && this.getHealth() != this.getMaxHealth())
                this.level().getBlockStates(this.getBoundingBox().inflate(3)).forEach(e -> {
                    if (e.is(GigTags.NEST_BLOCKS))
                        this.heal(0.5833f);
                });
            AzureTicker.tick(serverLevel, this.vibrationData, this.vibrationUser);
        }
        if (this.tickCount % 10 == 0)
            this.refreshDimensions();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void checkDespawn() {}

    @Override
    public void die(@NotNull DamageSource source) {
        if (
            DamageSourceUtils.isDamageSourceNotPuncturing(
                source,
                this.damageSources()
            ) || source == damageSources().genericKill()
        ) {
            super.die(source);
            return;
        }

        var damageCheck = !this.level().isClientSide && source != damageSources().genericKill() || source != damageSources().generic();
        if (damageCheck && !this.getType().is(GigTags.NO_ACID_BLOOD)) {
            if (getAcidDiameter() == 1)
                GigCommonMethods.generateAcidPool(this, this.blockPosition(), 0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (int i = 0; i < getAcidDiameter(); i++) {
                    int x = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    int z = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    if (source != damageSources().genericKill() || source != damageSources().generic()) {
                        GigCommonMethods.generateAcidPool(this, this.blockPosition(), x, z);
                    }
                }
            }
        }
        super.die(source);
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        if (this.level() instanceof ServerLevel serverLevel)
            biConsumer.accept(this.dynamicGameEventListener, serverLevel);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(travelVector);
        }
    }

    /*
     * SOUNDS
     */
    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return GigSounds.ALIEN_HURT.get();
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return GigSounds.ALIEN_DEATH.get();
    }

    @Override
    protected @NotNull SoundEvent getSwimSplashSound() {
        return SoundEvents.DOLPHIN_SPLASH;
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    public void grabTarget(Entity entity) {
        if (
            entity == this.getTarget() && !entity.hasPassenger(
                this
            ) && entity.getInBlockState().getBlock() != GigBlocks.NEST_RESIN_WEB_CROSS
        ) {
            entity.startRiding(this, true);
            this.setAggressive(false);
            if (entity instanceof ServerPlayer player)
                player.connection.send(new ClientboundSetPassengersPacket(entity));
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        var multiplier = 1.0f;
        if (source == this.damageSources().onFire())
            multiplier = 2.0f;
        if (source == damageSources().inWall())
            return false;

        if (!this.level().isClientSide && source.getEntity() != null && source.getEntity() instanceof LivingEntity attacker)
            this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, attacker);
        if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources()))
            return super.hurt(source, amount);

        if (
            !this.level().isClientSide && source != this.damageSources().genericKill() && !this.getType()
                .is(
                    GigTags.NO_ACID_BLOOD
                ) && this.isAlive() && amount > 8F
        ) {
            if (getAcidDiameter() == 1)
                GigCommonMethods.generateAcidPool(this, this.blockPosition(), 0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (int i = 0; i < getAcidDiameter(); i++) {
                    int x = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    int z = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    if (source != damageSources().genericKill() || source != damageSources().generic()) {
                        GigCommonMethods.generateAcidPool(this, this.blockPosition(), x, z);
                    }
                }
            }
        }
        if (source != this.damageSources().genericKill()) {
            var safeAmount = Math.max(amount, 1);
            var adjustedAmount = safeAmount > 50 ? safeAmount / (float) Math.log10(safeAmount) : safeAmount;
            return super.hurt(source, adjustedAmount);
        }
        return super.hurt(source, amount * multiplier);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    /*
     * GROWTH
     */
    public float getMaxGrowth() {
        return Constants.TPM;
    }

    public LivingEntity growInto() {
        return null;
    }

    @Override
    public @NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return this.vibrationUser;
    }

    /*
     * Enabled force condition propagation Lifted jumps to return sites
     */
    @Contract(value = "null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))
            return false;
        if (this.level() != entity.level())
            return false;
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity))
            return false;
        if (livingEntity.hasEffect(GigStatusEffects.IMPREGNATION))
            return false;
        if (this.isVehicle())
            return false;
        if (this.isAlliedTo(entity))
            return false;
        if (!livingEntity.getType().is(GigTags.ALL_HOSTS))
            return false;
        if (livingEntity.getType().is(EntityTypeTags.UNDEAD))
            return false;
        if (livingEntity.getInBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS)
            return false;
        if (livingEntity.getType() == EntityType.ARMOR_STAND)
            return false;
        if (livingEntity.getType() == EntityType.WARDEN)
            return false;
        if (livingEntity instanceof Bat)
            return false;
        if (GigEntityUtils.isFacehuggerAttached(livingEntity))
            return false;
        if (livingEntity.isInvulnerable())
            return false;
        if (livingEntity.isDeadOrDying())
            return false;
        if (!this.level().getWorldBorder().isWithinBounds(livingEntity.getBoundingBox()))
            return false;
        if (
            livingEntity.getVehicle() != null && livingEntity.getVehicle()
                .getSelfAndPassengers()
                .anyMatch(
                    AlienEntity.class::isInstance
                )
        )
            return false;
        if (livingEntity.getType().is(GigTags.GIG_ALIENS))
            return false;
        if (this.isAggressive())
            return false;
        return this.level()
            .getBlockState(this.blockPosition().below())
            .isCollisionShapeFullBlock(
                level(),
                this.blockPosition().below()
            );
    }

    public void drop(LivingEntity target, ItemStack itemStack) {
        if (itemStack.isEmpty())
            return;

        var d = target.getEyeY() - 0.3f;
        var itemEntity = new ItemEntity(target.level(), target.getX(), d, target.getZ(), itemStack);
        itemEntity.setPickUpDelay(40);
        float g = Mth.sin(this.getXRot() * ((float) Math.PI / 180));
        float h = Mth.cos(this.getXRot() * ((float) Math.PI / 180));
        float i = Mth.sin(this.getYRot() * ((float) Math.PI / 180));
        float j = Mth.cos(this.getYRot() * ((float) Math.PI / 180));
        float k = this.random.nextFloat() * ((float) Math.PI * 2);
        float l = 0.02f * this.random.nextFloat();
        itemEntity.setDeltaMovement(
            (-i * h * 0.3f) + Math.cos(k) * l,
            -g * 0.3f + 0.1f * 0.1f,
            (j * h * 0.3f) + Math.sin(k) * l
        );
        target.level().addFreshEntity(itemEntity);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.BUCKET) && !player.level().isClientSide()) {
            player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            player.addEffect(new MobEffectInstance(GigStatusEffects.ACID, 100, 0), this);
            if (player instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("dontdothat"));
                if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    for (
                        var s : serverPlayer.getAdvancements()
                            .getOrStartProgress(
                                advancement
                            )
                            .getRemainingCriteria()
                    ) {
                        serverPlayer.getAdvancements().award(advancement, s);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (player.getItemInHand(hand).is(Items.GLASS_BOTTLE) && !player.level().isClientSide()) {
            player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            player.hurt(GigDamageSources.of(player.level(), GigDamageSources.ACID), CommonMod.config.acidDamage);

            if (player instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("dontacidbottle"));
                if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    for (
                        var s : serverPlayer.getAdvancements()
                            .getOrStartProgress(
                                advancement
                            )
                            .getRemainingCriteria()
                    ) {
                        serverPlayer.getAdvancements().award(advancement, s);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isWithinMeleeAttackRange(@NotNull LivingEntity entity) {
        for (
            var testPos : BlockPos.betweenClosed(
                this.blockPosition().relative(this.getDirection(), 1).above(-1).relative(this.getDirection().getClockWise(), -1),
                this.blockPosition().relative(this.getDirection(), 3).above(1).relative(this.getDirection().getClockWise(), 1)
            )
        ) {
            if (entity.blockPosition().equals(testPos)) {
                return true;
            }
        }
        return super.isWithinMeleeAttackRange(entity);
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

}
