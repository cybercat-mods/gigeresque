package mods.cybercat.gigeresque.common.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.sblforked.api.core.navigation.SmoothAmphibiousPathNavigation;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.GigNav;
import mods.cybercat.gigeresque.common.entity.helper.AzureTicker;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.core.Vec3i;
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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class AlienEntity extends Monster implements VibrationSystem, GeoEntity, Growable, AbstractAlien {

    public static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FLEEING_FIRE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> PASSED_OUT = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());
    public static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_TUNNEL_CRAWLING = SynchedEntityData.defineId(
            AlienEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> WAKING_UP = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(
            AlienEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> IS_HISSING = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_SEARCHING = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_EXECUTION = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_HEADBITE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DynamicGameEventListener<Listener> dynamicGameEventListener;
    public int wakeupCounter = 0;
    public boolean inTwoBlockSpace = false;
    protected AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    protected User vibrationUser;
    protected int slowticks = 0;
    private Data vibrationData;

    protected AlienEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.vibrationUser = new AzureVibrationUser(this, 2.5F);
        this.vibrationData = new Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new Listener(this));
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.5F, 1.0F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public float maxUpStep() {
        return 2.5f;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        this.triggerAnim(Constants.LIVING_CONTROLLER, "death");
        this.triggerAnim(Constants.ATTACK_CONTROLLER, "death");
        if (this.deathTime == 150) {
            this.remove(RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience(this);
        }
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
    public boolean isUpsideDown() {
        return this.entityData.get(UPSIDE_DOWN);
    }

    @Override
    public boolean isCrawling() {
        return this.entityData.get(IS_CLIMBING);
    }

    @Override
    public void setIsCrawling(boolean shouldCrawl) {
        this.getEntityData().set(IS_CLIMBING, shouldCrawl);
        this.refreshDimensions();
    }

    @Override
    public boolean isTunnelCrawling() {
        return this.entityData.get(IS_TUNNEL_CRAWLING);
    }

    @Override
    public void setIsTunnelCrawling(boolean shouldTunnelCrawl) {
        this.getEntityData().set(IS_TUNNEL_CRAWLING, shouldTunnelCrawl);
        this.refreshDimensions();
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
    public boolean isSearching() {
        return entityData.get(IS_SEARCHING);
    }

    @Override
    public void setIsSearching(boolean isHissing) {
        entityData.set(IS_SEARCHING, isHissing);
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
    public boolean isPassedOut() {
        return this.entityData.get(PASSED_OUT);
    }

    @Override
    public void setPassedOutStatus(boolean passout) {
        this.entityData.set(PASSED_OUT, passout);
    }

    @Override
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(UPSIDE_DOWN, false);
        builder.define(FLEEING_FIRE, false);
        builder.define(IS_CLIMBING, false);
        builder.define(IS_TUNNEL_CRAWLING, false);
        builder.define(STATE, 0);
        builder.define(CLIENT_ANGER_LEVEL, 0);
        builder.define(GROWTH, 0.0f);
        builder.define(PASSED_OUT, false);
        builder.define(WAKING_UP, false);
        builder.define(IS_HISSING, false);
        builder.define(IS_EXECUTION, false);
        builder.define(IS_HEADBITE, false);
        builder.define(IS_SEARCHING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isCrawling", this.isCrawling());
        compound.putBoolean("isTunnelCrawling", this.isTunnelCrawling());
        Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("listener", tag));
        AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("anger", tag));
        compound.putFloat("growth", this.getGrowth());
        compound.putBoolean("isStasis", this.isPassedOut());
        compound.putBoolean("wakingup", this.isWakingUp());
        compound.putBoolean("isHissing", this.isHissing());
        compound.putBoolean("isSearching", this.isSearching());
        compound.putBoolean("isExecuting", this.isExecuting());
        compound.putBoolean("isHeadBite", this.isBiting());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("isCrawling")) this.setIsCrawling(compound.getBoolean("isCrawling"));
        if (compound.contains("anger")) {
            AngerManagement.codec(this::canTargetEntity).parse(
                    new Dynamic<>(NbtOps.INSTANCE, compound.get("anger"))).resultOrPartial(LOGGER::error).ifPresent(
                    angerM -> this.angerManagement = angerM);
            this.syncClientAngerLevel();
        }
        if (compound.contains("listener", 10)) Data.CODEC.parse(
                new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(
                LOGGER::error).ifPresent(data -> this.vibrationData = data);
        this.setGrowth(compound.getFloat("getStatisTimer"));
        this.setGrowth(compound.getFloat("growth"));
        this.setIsTunnelCrawling(compound.getBoolean("isTunnelCrawling"));
        this.setIsHissing(compound.getBoolean("isHissing"));
        this.setIsBiting(compound.getBoolean(("isHeadBite")));
        this.setIsSearching(compound.getBoolean("isSearching"));
        this.setIsExecuting(compound.getBoolean("isExecuting"));
        this.setIsExecuting(compound.getBoolean("isHeadBite"));
        this.setPassedOutStatus(compound.getBoolean("isStasis"));
        this.setWakingUpStatus(compound.getBoolean("wakingup"));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        PathNavigation pathNavigation;
        if (this.isUnderWater()) {
            pathNavigation = new SmoothAmphibiousPathNavigation(this, level());
        } else {
            pathNavigation = new GigNav(this, level());
            pathNavigation.setCanFloat(true);
        }
        return pathNavigation;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public int calculateFallDamage(float fallDistance, float damageMultiplier) {
        if (fallDistance <= 15) return 0;
        return super.calculateFallDamage(fallDistance, damageMultiplier);
    }

    @Override
    public int getMaxFallDistance() {
        return 9;
    }

    protected void syncClientAngerLevel() {
        this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
    }

    private int getActiveAnger() {
        return this.angerManagement.getActiveAnger(this.getTarget());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        var serverLevel = (ServerLevel) this.level();
        super.customServerAiStep();
        if (this.tickCount % 20 == 0) {
            this.angerManagement.tick(serverLevel, this::canTargetEntity);
            this.syncClientAngerLevel();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAggressive()) {
            this.setPassedOutStatus(false);
        }
        // Waking up logic
        if (this.isPassedOut()) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
            if (this.isAggressive()) {
                this.triggerAnim(Constants.ATTACK_CONTROLLER, "wakeup");
                this.setPassedOutStatus(false);
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 100, false, false));
            }
        }
        if (level() instanceof ServerLevel serverLevel) {
            if (this.isAlive()) this.grow(this, 1 * getGrowthMultiplier());
            if (this.isVehicle()) this.setAggressive(false);
            if (this.tickCount % Constants.TPS == 0 && this.getHealth() != this.getMaxHealth())
                this.level().getBlockStates(this.getBoundingBox().inflate(3)).forEach(e -> {
                    if (e.is(GigTags.NEST_BLOCKS)) this.heal(0.5833f);
                });
            if (this.isAggressive() || this.getSpeed() > 0.1) {
                var isAboveSolid = this.level().getBlockState(blockPosition().above()).isCollisionShapeFullBlock(level(), blockPosition().above());
                var isTwoAboveSolid = this.level().getBlockState(blockPosition().above(2)).isCollisionShapeFullBlock(level(), blockPosition().above(2));
                var offset = getDirectionVector();
                var isFacingSolid = this.level().getBlockState(blockPosition().relative(getDirection())).isCollisionShapeFullBlock(level(), blockPosition().relative(getDirection()));

                /** Offset is set to the block above the block position (which is at feet level) (since direction is used it's the block in front of both cases)
                 *  -----o                  -----o
                 *       o                       o <- offset
                 *  -----o <- current       -----o
                 **/
                if (isFacingSolid) {
                    offset = offset.offset(0, 1, 0);
                }

                var isOffsetFacingSolid = this.level().getBlockState(blockPosition().offset(offset)).isCollisionShapeFullBlock(level(), blockPosition().offset(offset));
                var isOffsetFacingAboveSolid = this.level().getBlockState(
                        blockPosition().offset(offset).above()).isCollisionShapeFullBlock(level(), blockPosition().offset(offset).above());

                /** [- : blocks | o : alien | + : alien in solid block]
                 *   To handle these variants among other things:
                 *       o           o
                 *   ----+       ----o       ----+
                 *       o           o           o
                 *   -----       -----       ----o
                 **/
                var shouldTunnelCrawl = isAboveSolid || !isOffsetFacingSolid && isOffsetFacingAboveSolid || isFacingSolid && isTwoAboveSolid;
                this.setIsTunnelCrawling(shouldTunnelCrawl);
            }
            AzureTicker.tick(serverLevel, this.vibrationData, this.vibrationUser);
        }
        if (this.tickCount % 10 == 0) this.refreshDimensions();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (DamageSourceUtils.isDamageSourceNotPuncturing(source,
                this.damageSources()) || source == damageSources().genericKill()) {
            super.die(source);
            return;
        }

        var damageCheck = !this.level().isClientSide && source != damageSources().genericKill() || source != damageSources().generic();
        if (damageCheck && !this.getType().is(GigTags.NO_ACID_BLOOD)) {
            if (getAcidDiameter() == 1) GigCommonMethods.generateAcidPool(this, this.blockPosition(), 0, 0);
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

    /*
     * SOUNDS
     */
    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource source) {
        return GigSounds.ALIEN_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
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
        if (entity == this.getTarget() && !entity.hasPassenger(
                this) && entity.getInBlockState().getBlock() != GigBlocks.NEST_RESIN_WEB_CROSS) {
            entity.startRiding(this, true);
            this.setAggressive(false);
            if (entity instanceof ServerPlayer player)
                player.connection.send(new ClientboundSetPassengersPacket(entity));
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        var multiplier = 1.0f;
        if (source == this.damageSources().onFire()) multiplier = 2.0f;
        if (source == damageSources().inWall()) return false;

        if (!this.level().isClientSide && source.getEntity() != null && source.getEntity() instanceof LivingEntity attacker)
            this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, attacker);

        if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources()))
            return super.hurt(source, amount);

        if (!this.level().isClientSide && source != this.damageSources().genericKill() && !this.getType().is(
                GigTags.NO_ACID_BLOOD) && this.isAlive()) {
            if (getAcidDiameter() == 1) GigCommonMethods.generateAcidPool(this, this.blockPosition(), 0, 0);
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
        return super.hurt(source, amount * multiplier);
    }

    @Override
    public boolean onClimbable() {
        return this.fallDistance <= 0.1;
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
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        if (this.level() != entity.level()) return false;
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) return false;
        if (livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)) return false;
        if (this.isVehicle()) return false;
        if (this.isAlliedTo(entity)) return false;
        if (!livingEntity.getType().is(GigTags.ALL_HOSTS)) return false;
        if (livingEntity.getType().is(EntityTypeTags.UNDEAD)) return false;
        if (livingEntity.getInBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS) return false;
        if (livingEntity.getType() == EntityType.ARMOR_STAND) return false;
        if (livingEntity.getType() == EntityType.WARDEN) return false;
        if (livingEntity instanceof Bat) return false;
        if (GigEntityUtils.isFacehuggerAttached(livingEntity)) return false;
        if (livingEntity.isInvulnerable()) return false;
        if (livingEntity.isDeadOrDying()) return false;
        if (!this.level().getWorldBorder().isWithinBounds(livingEntity.getBoundingBox())) return false;
        var list2 = livingEntity.level().getBlockStatesIfLoaded(livingEntity.getBoundingBox().inflate(2.0, 2.0, 2.0));
        if (list2.anyMatch(NEST)) return false;
        if (livingEntity.getVehicle() != null && livingEntity.getVehicle().getSelfAndPassengers().anyMatch(
                AlienEntity.class::isInstance)) return false;
        if (livingEntity.getType().is(GigTags.GIG_ALIENS)) return false;
        if (this.isAggressive()) return false;
        return this.level().getBlockState(this.blockPosition().below()).isCollisionShapeFullBlock(level(), this.blockPosition().below());
    }

    public void drop(LivingEntity target, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;

        var d = target.getEyeY() - 0.3f;
        var itemEntity = new ItemEntity(target.level(), target.getX(), d, target.getZ(), itemStack);
        itemEntity.setPickUpDelay(40);
        float g = Mth.sin(this.getXRot() * ((float) Math.PI / 180));
        float h = Mth.cos(this.getXRot() * ((float) Math.PI / 180));
        float i = Mth.sin(this.getYRot() * ((float) Math.PI / 180));
        float j = Mth.cos(this.getYRot() * ((float) Math.PI / 180));
        float k = this.random.nextFloat() * ((float) Math.PI * 2);
        float l = 0.02f * this.random.nextFloat();
        itemEntity.setDeltaMovement((-i * h * 0.3f) + Math.cos(k) * l, -g * 0.3f + 0.1f * 0.1f,
                (j * h * 0.3f) + Math.sin(k) * l);
        target.level().addFreshEntity(itemEntity);
    }

    protected Vec3i getDirectionVector() {
        return new Vec3i(getDirection().getStepX(), getDirection().getStepY(), getDirection().getStepZ());
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

}
