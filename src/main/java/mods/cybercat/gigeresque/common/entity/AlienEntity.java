package mods.cybercat.gigeresque.common.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.AcidBlock;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.helper.AzureTicker;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
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
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class AlienEntity extends Monster implements VibrationSystem, GeoEntity, Growable {

    public static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FLEEING_FIRE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> PASSED_OUT = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS);
    protected static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(
            AlienEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_TUNNEL_CRAWLING = SynchedEntityData.defineId(
            AlienEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> WAKING_UP = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
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
    protected final AzureNavigation landNavigation = new AzureNavigation(this, level());
    protected final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level());
    protected final MoveControl landMoveControl = new MoveControl(this);
    protected final SmoothSwimmingLookControl landLookControl = new SmoothSwimmingLookControl(this, 5);
    protected final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.5f, 1.0f,
            false);
    protected final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
    protected AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    protected VibrationSystem.User vibrationUser;
    protected int slowticks = 0;
    private VibrationSystem.Data vibrationData;
    public int wakeupCounter = 0;

    protected AlienEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.setMaxUpStep(2.5f);
        setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
        setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
        this.vibrationUser = new AzureVibrationUser(this, 2.5F);
        this.vibrationData = new VibrationSystem.Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
        this.navigation = landNavigation;
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0f);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        this.triggerAnim("livingController", "death");
        this.triggerAnim("attackController", "death");
        if (this.deathTime == 150) {
            this.remove(Entity.RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience();
        }
    }

    protected int getAcidDiameter() {
        return 3;
    }

    public boolean isFleeing() {
        return this.entityData.get(FLEEING_FIRE);
    }

    public void setFleeingStatus(boolean fleeing) {
        this.entityData.set(FLEEING_FIRE, fleeing);
    }

    public boolean isUpsideDown() {
        return this.entityData.get(UPSIDE_DOWN);
    }

    public int getAttckingState() {
        return this.entityData.get(STATE);
    }

    public void setAttackingState(int time) {
        this.entityData.set(STATE, time);
    }

    public boolean isCrawling() {
        return this.entityData.get(IS_CLIMBING);
    }

    public void setIsCrawling(boolean shouldCrawl) {
        this.getEntityData().set(IS_CLIMBING, shouldCrawl);
        this.refreshDimensions();
    }

    public boolean isTunnelCrawling() {
        return this.entityData.get(IS_TUNNEL_CRAWLING);
    }

    public void setIsTunnelCrawling(boolean shouldTunnelCrawl) {
        this.getEntityData().set(IS_TUNNEL_CRAWLING, shouldTunnelCrawl);
        this.refreshDimensions();
    }

    public void setWakingUpStatus(boolean passout) {
        this.entityData.set(WAKING_UP, passout);
    }

    public boolean isWakingUp() {
        return this.entityData.get(WAKING_UP);
    }

    public boolean isExecuting() {
        return entityData.get(IS_EXECUTION);
    }

    public void setIsExecuting(boolean isExecuting) {
        entityData.set(IS_EXECUTION, isExecuting);
    }

    public boolean isBiting() {
        return entityData.get(IS_HEADBITE);
    }

    public void setIsBiting(boolean isBiting) {
        entityData.set(IS_HEADBITE, isBiting);
    }

    public boolean isSearching() {
        return entityData.get(IS_SEARCHING);
    }

    public void setIsSearching(boolean isHissing) {
        entityData.set(IS_SEARCHING, isHissing);
    }

    public boolean isHissing() {
        return entityData.get(IS_HISSING);
    }

    public void setIsHissing(boolean isHissing) {
        entityData.set(IS_HISSING, isHissing);
    }

    public float getGrowth() {
        return entityData.get(GROWTH);
    }

    public void setGrowth(float growth) {
        entityData.set(GROWTH, growth);
    }

    public boolean isPassedOut() {
        return this.entityData.get(PASSED_OUT);
    }

    public void setPassedOutStatus(boolean passout) {
        this.entityData.set(PASSED_OUT, passout);
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(UPSIDE_DOWN, false);
        this.entityData.define(FLEEING_FIRE, false);
        this.entityData.define(IS_CLIMBING, false);
        this.entityData.define(IS_TUNNEL_CRAWLING, false);
        this.entityData.define(STATE, 0);
        this.entityData.define(CLIENT_ANGER_LEVEL, 0);
        this.entityData.define(GROWTH, 0.0f);
        this.entityData.define(PASSED_OUT, false);
        this.entityData.define(WAKING_UP, false);
        this.entityData.define(IS_HISSING, false);
        this.entityData.define(IS_EXECUTION, false);
        this.entityData.define(IS_HEADBITE, false);
        this.entityData.define(IS_SEARCHING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isCrawling", isCrawling());
        compound.putBoolean("isTunnelCrawling", isTunnelCrawling());
        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("listener", tag));
        AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("anger", tag));
        compound.putFloat("growth", getGrowth());
        compound.putBoolean("isStasis", this.isPassedOut());
        compound.putBoolean("wakingup", this.isWakingUp());
        compound.putBoolean("isHissing", isHissing());
        compound.putBoolean("isSearching", isSearching());
        compound.putBoolean("isExecuting", isExecuting());
        compound.putBoolean("isHeadBite", isBiting());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("isCrawling")) setIsCrawling(compound.getBoolean("isCrawling"));
        if (compound.contains("anger")) {
            AngerManagement.codec(this::canTargetEntity).parse(
                    new Dynamic<>(NbtOps.INSTANCE, compound.get("anger"))).resultOrPartial(LOGGER::error).ifPresent(
                    angerM -> this.angerManagement = angerM);
            this.syncClientAngerLevel();
        }
        if (compound.contains("listener", 10)) VibrationSystem.Data.CODEC.parse(
                new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(
                LOGGER::error).ifPresent(data -> this.vibrationData = data);
        this.setGrowth(compound.getFloat("getStatisTimer"));
        this.setGrowth(compound.getFloat("growth"));
        this.setIsTunnelCrawling(compound.getBoolean("isTunnelCrawling"));
        this.setIsHissing(compound.getBoolean("isHissing"));
        this.setIsSearching(compound.getBoolean("isSearching"));
        this.setIsExecuting(compound.getBoolean("isExecuting"));
        this.setIsExecuting(compound.getBoolean("isHeadBite"));
        this.setPassedOutStatus(compound.getBoolean("isStasis"));
        this.setWakingUpStatus(compound.getBoolean("wakingup"));
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : landMoveControl;
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimLookControl : landLookControl;

        if (this.isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            this.moveRelative(getSpeed(), movementInput);
            this.move(MoverType.SELF, getDeltaMovement());
            this.setDeltaMovement(getDeltaMovement().scale(0.9));
            if (getTarget() == null) this.setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
        } else super.travel(movementInput);
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
        if (!level().isClientSide && this.isAlive()) this.grow(this, 1 * getGrowthMultiplier());
        if (!level().isClientSide && this.isVehicle()) this.setAggressive(false);
        if (this.level().getBlockState(this.blockPosition()).is(GigBlocks.ACID_BLOCK))
            this.level().removeBlock(this.blockPosition(), false);
        if (this.isAggressive()) {
            this.setPassedOutStatus(false);
        }
        if (!this.level().isClientSide) slowticks++;
        if (this.slowticks > 10 && !this.isCrawling() && this.getNavigation().isDone() && !this.isAggressive() && !(this.level().getFluidState(
                this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 100, false, false));
            slowticks = -60;
        }
        if (this.level() instanceof ServerLevel serverLevel)
            AzureTicker.tick(serverLevel, this.vibrationData, this.vibrationUser);
        if (!level().isClientSide && this.tickCount % Constants.TPS == 0)
            this.level().getBlockStates(this.getBoundingBox().inflate(3)).forEach(e -> {
                if (e.is(GigTags.NEST_BLOCKS)) this.heal(0.5833f);
            });
        // Waking up logic
        if (this.isPassedOut()) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
            if (this.isAggressive()) {
                this.triggerAnim("attackController", "wakeup");
                this.setPassedOutStatus(false);
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 100, false, false));
            }
        }
        if (this.isNoGravity()) this.setNoGravity(false);
        if (level() instanceof ServerLevel) {
            var isAboveSolid = this.level().getBlockState(blockPosition().above()).isSolid();
            var isTwoAboveSolid = this.level().getBlockState(blockPosition().above(2)).isSolid();
            var offset = getDirectionVector();
            var isFacingSolid = this.level().getBlockState(blockPosition().relative(getDirection())).isSolid();

            /** Offset is set to the block above the block position (which is at feet level) (since direction is used it's the block in front of both cases)
             *  -----o                  -----o
             *       o                       o <- offset
             *  -----o <- current       -----o
             **/
            if (isFacingSolid) {
                offset = offset.offset(0, 1, 0);
            }

            var isOffsetFacingSolid = this.level().getBlockState(blockPosition().offset(offset)).isSolid();
            var isOffsetFacingAboveSolid = this.level().getBlockState(blockPosition().offset(offset).above()).isSolid();

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
        this.refreshDimensions();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void checkDespawn() {
    }

    public void generateAcidPool(int xOffset, int zOffset) {
        var pos = this.blockPosition().offset(xOffset, 0, zOffset);
        var posState = level().getBlockState(pos);
        var newState = GigBlocks.ACID_BLOCK.defaultBlockState();

        if (posState.getBlock() == Blocks.WATER) newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);

        if (!(posState.getBlock() instanceof AirBlock) && !(posState.getBlock() instanceof LiquidBlock && !(posState.is(
                GigTags.ACID_RESISTANT))) && !(posState.getBlock() instanceof TorchBlock)) return;
        level().setBlockAndUpdate(pos, newState);
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (DamageSourceUtils.isDamageSourceNotPuncturing(source,
                this.damageSources()) || source == damageSources().genericKill()) {
            super.die(source);
            return;
        }

        var damageCheck = !this.level().isClientSide && source != damageSources().genericKill() || source != damageSources().generic();
        if (damageCheck) {
            if (getAcidDiameter() == 1) generateAcidPool(0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (var x = -radius; x <= radius; x++) {
                    for (var z = -radius; z <= radius; z++)
                        if (source != damageSources().genericKill() || source != damageSources().generic())
                            generateAcidPool(x, z);
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
        return SoundEvents.EMPTY;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.EMPTY;
    }

    public void grabTarget(Entity entity) {
        if (entity == this.getTarget() && !entity.hasPassenger(
                this) && entity.getFeetBlockState().getBlock() != GigBlocks.NEST_RESIN_WEB_CROSS) {
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

        if (!this.level().isClientSide && source != this.damageSources().genericKill()) {
            var acidThickness = this.getHealth() < (this.getMaxHealth() / 2) ? 1 : 0;

            if (this.getHealth() < (this.getMaxHealth() / 4)) acidThickness += 1;
            if (amount >= 5) acidThickness += 1;
            if (amount > (this.getMaxHealth() / 10)) acidThickness += 1;
            if (acidThickness == 0) return super.hurt(source, amount);

            var newState = GigBlocks.ACID_BLOCK.defaultBlockState().setValue(AcidBlock.THICKNESS, acidThickness);

            if (this.getFeetBlockState().getBlock() == Blocks.WATER)
                newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
            if (!this.getFeetBlockState().is(GigTags.ACID_RESISTANT))
                this.level().setBlockAndUpdate(this.blockPosition(), newState);
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
    public VibrationSystem.@NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.@NotNull User getVibrationUser() {
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
        if (((Host) livingEntity).hasParasite()) return false;
        if (this.isVehicle()) return false;
        if (this.isAlliedTo(entity)) return false;
        if (livingEntity.getMobType() == MobType.UNDEAD) return false;
        if (livingEntity.getFeetBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS) return false;
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
        if (livingEntity instanceof AlienEntity) return false;
        if (this.isAggressive()) return false;
        return this.level().getBlockState(this.blockPosition().below()).isSolid();
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

}
