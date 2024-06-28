package mods.cybercat.gigeresque.common.entity;

import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.interfacing.AbstractAlien;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.tslat.smartbrainlib.api.core.navigation.SmoothWaterBoundPathNavigation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class WaterAlienEntity extends Dolphin implements Enemy, GeoEntity, Growable, AbstractAlien {

    public static final EntityDataAccessor<Boolean> FLEEING_FIRE = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> PASSED_OUT = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());
    public static final EntityDataAccessor<Boolean> WAKING_UP = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> IS_HISSING = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_SEARCHING = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_EXECUTION = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_HEADBITE = SynchedEntityData.defineId(WaterAlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected int slowticks = 0;
    public int wakeupCounter = 0;

    public WaterAlienEntity(EntityType<? extends Dolphin> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    public float maxUpStep() {
        return 2.5f;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothWaterBoundPathNavigation(this, level);
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        var attackBox = this.getBoundingBox().inflate(1);
        return attackBox.intersects(entity.getBoundingBox());
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return target.hurt(this.damageSources().mobAttack(this),
                ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
    }

    @Override
    protected boolean canRide(@NotNull Entity vehicle) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.setAirSupply(this.getMaxAirSupply());
        if (!level().isClientSide && this.isAlive()) this.grow(this, 1 * getGrowthMultiplier());
        if (!level().isClientSide && this.isVehicle()) this.setAggressive(false);
        if (this.isAggressive()) {
            this.setPassedOutStatus(false);
        }
        if (!this.level().isClientSide) slowticks++;
        if (this.slowticks > 10 && this.getNavigation().isDone() && !this.isAggressive() && !(this.level().getFluidState(
                this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 100, false, false));
            slowticks = -60;
        }
        if (!level().isClientSide && this.tickCount % Constants.TPS == 0)
            this.level().getBlockStates(this.getBoundingBox().inflate(3)).forEach(e -> {
                if (e.is(GigTags.NEST_BLOCKS)) this.heal(0.5833f);
            });
        // Waking up logic
        if (this.isPassedOut()) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
            if (this.isAggressive()) {
                this.triggerAnim(Constants.ATTACK_CONTROLLER, "wakeup");
                this.setPassedOutStatus(false);
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 100, false, false));
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 38) {
        } else {
            super.handleEntityEvent(id);
        }
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

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return InteractionResult.PASS;
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
        return false;
    }

    @Override
    public boolean isCrawling() {
        return false;
    }

    @Override
    public void setIsCrawling(boolean shouldCrawl) {
    }

    @Override
    public boolean isTunnelCrawling() {
        return false;
    }

    @Override
    public void setIsTunnelCrawling(boolean shouldTunnelCrawl) {
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
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLEEING_FIRE, false);
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
        this.setGrowth(compound.getFloat("getStatisTimer"));
        this.setGrowth(compound.getFloat("growth"));
        this.setIsHissing(compound.getBoolean("isHissing"));
        this.setIsBiting(compound.getBoolean(("isHeadBite")));
        this.setIsSearching(compound.getBoolean("isSearching"));
        this.setIsExecuting(compound.getBoolean("isExecuting"));
        this.setIsExecuting(compound.getBoolean("isHeadBite"));
        this.setPassedOutStatus(compound.getBoolean("isStasis"));
        this.setWakingUpStatus(compound.getBoolean("wakingup"));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
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
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EMPTY;
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
    public boolean canBeLeashed() {
        return false;
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
                GigTags.NO_ACID_BLOOD)) {
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
}
