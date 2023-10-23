package mods.cybercat.gigeresque.common.entity.impl;

import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.AcidBlock;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AdultAlienEntity extends AlienEntity implements GeoEntity, Growable {

    public static final EntityDataAccessor<Boolean> PASSED_OUT = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> WAKING_UP = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> IS_HISSING = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_SEARCHING = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_BREAKING = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_EXECUTION = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_HEADBITE = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
    protected final AzureNavigation landNavigation = new AzureNavigation(this, level());
    protected final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level());
    protected final MoveControl landMoveControl = new MoveControl(this);
    protected final SmoothSwimmingLookControl landLookControl = new SmoothSwimmingLookControl(this, 5);
    protected final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.5f, 1.0f, false);
    protected final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);
    public int statisCounter = 0;
    public int holdingCounter = 0;
    public int breakingCounter = 0;
    public int biteCounter = 0;
    public int passoutCounter = 0;
    public int wakeupCounter = 0;
    protected long hissingCooldown = 0L;
    protected long searchingProgress = 0L;
    protected long searchingCooldown = 0L;
    protected int attackProgress = 0;

    public AdultAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
        super(type, world);
        this.setMaxUpStep(2.5f);
        this.vibrationUser = new AzureVibrationUser(this, 2.5F);
        this.navigation = landNavigation;
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0f);
    }

    public void setWakingUpStatus(boolean passout) {
        this.entityData.set(WAKING_UP, Boolean.valueOf(passout));
    }

    public boolean isWakingUp() {
        return this.entityData.get(WAKING_UP);
    }

    public void setPassedOutStatus(boolean passout) {
        this.entityData.set(PASSED_OUT, Boolean.valueOf(passout));
    }

    public boolean isPassedOut() {
        return this.entityData.get(PASSED_OUT);
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

    public boolean isBreaking() {
        return entityData.get(IS_BREAKING);
    }

    public void setIsBreaking(boolean isBreaking) {
        entityData.set(IS_BREAKING, isBreaking);
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

    @Override
    public void travel(Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : landMoveControl;
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimLookControl : landLookControl;

        if (this.isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            this.moveRelative(getSpeed(), movementInput);
            this.move(MoverType.SELF, getDeltaMovement());
            this.setDeltaMovement(getDeltaMovement().scale(0.9));
            if (getTarget() == null)
                this.setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
        } else
            super.travel(movementInput);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GROWTH, 0.0f);
        this.entityData.define(PASSED_OUT, false);
        this.entityData.define(WAKING_UP, false);
        this.entityData.define(IS_HISSING, false);
        this.entityData.define(IS_BREAKING, false);
        this.entityData.define(IS_EXECUTION, false);
        this.entityData.define(IS_HEADBITE, false);
        this.entityData.define(IS_SEARCHING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("growth", getGrowth());
        nbt.putBoolean("isStasis", this.isPassedOut());
        nbt.putBoolean("wakingup", this.isWakingUp());
        nbt.putBoolean("isHissing", isHissing());
        nbt.putBoolean("isBreaking", isBreaking());
        nbt.putBoolean("isSearching", isSearching());
        nbt.putBoolean("isExecuting", isExecuting());
        nbt.putBoolean("isHeadBite", isBiting());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setGrowth(nbt.getFloat("getStatisTimer"));
        this.setGrowth(nbt.getFloat("growth"));
        this.setIsHissing(nbt.getBoolean("isHissing"));
        this.setIsBreaking(nbt.getBoolean("isBreaking"));
        this.setIsSearching(nbt.getBoolean("isSearching"));
        this.setIsExecuting(nbt.getBoolean("isExecuting"));
        this.setIsExecuting(nbt.getBoolean("isHeadBite"));
        this.setPassedOutStatus(nbt.getBoolean("isStasis"));
        this.setWakingUpStatus(nbt.getBoolean("wakingup"));
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
    public void tick() {
        super.tick();
        if (!level().isClientSide && this.isAlive())
            this.grow(this, 1 * getGrowthMultiplier());

        if (!level().isClientSide && this.isVehicle())
            this.setAggressive(false);

        if (this.isAggressive()) {
            this.wakeupCounter = 0;
            this.setPassedOutStatus(false);
        }

        // Passing and waking up logic
        var velocityLength = this.getDeltaMovement().horizontalDistance();
        if (!this.getTypeName().getString().equalsIgnoreCase("neomorph"))
            if ((velocityLength == 0 && !this.isVehicle() && this.isAlive() && !this.isSearching() && !this.isHissing() && !this.isPassedOut())) {
                if (!this.level().isClientSide)
                    this.passoutCounter++;
                if (this.passoutCounter >= 6000) {
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
                    this.triggerAnim("attackController", "passout");
                    this.passoutCounter = -6000;
                    this.setPassedOutStatus(true);
                }
            }
        if (this.isPassedOut()) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
            if (this.isAggressive()) {
                this.triggerAnim("attackController", "wakeup");
                this.setPassedOutStatus(false);
                if (!this.level().isClientSide)
                    this.passoutCounter = -6000;
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 100, false, false));
            }
//			if (this.tickCount < 2 && !this.isAggressive())
//				this.triggerAnim("attackController", "passout");
        }
        if (this.isAggressive())
            if (!this.level().isClientSide)
                this.passoutCounter = 0;

        if (this.isInWater()) {
            this.hissingCooldown = 0;
            this.searchingProgress = 0;
        }

        // Hissing Logic
        if (velocityLength == 0 && !this.isInWater() && !level().isClientSide && (!this.isSearching() && !this.isVehicle() && this.isAlive() && this.isPassedOut() == false) && !this.isAggressive() && !this.isCrawling()) {
            if (!this.level().isClientSide)
                this.hissingCooldown++;

            if (hissingCooldown == 80)
                this.setIsHissing(true);

            if (hissingCooldown > 160) {
                this.setIsHissing(false);
                this.hissingCooldown = -500;
            }
        }

        // Searching Logic
        if ((this.level().getBlockState(this.blockPosition().below()).isSolid() && velocityLength == 0 && !this.isInWater() && !this.isAggressive() && !this.isVehicle() && !this.isHissing() && this.isAlive() && !this.isPassedOut() && !this.isCrawling())) {
            if (!this.level().isClientSide)
                this.searchingProgress++;

            if (this.searchingProgress == 80) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 100, false, false));
                this.setIsSearching(true);
            }

            if (this.searchingProgress > 160) {
                this.setIsSearching(false);
                this.searchingProgress = -500;
            }
        }

        if (this.level().getBlockState(this.blockPosition()).is(GigBlocks.ACID_BLOCK))
            this.level().removeBlock(this.blockPosition(), false);

        if (!this.isAggressive() && !this.isCrawling() && !this.isDeadOrDying() && !this.isPassedOut() && this.isAggressive() && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) == true) {
            if (!this.level().isClientSide)
                this.breakingCounter++;
            if (this.breakingCounter > 10)
                for (var testPos : BlockPos.betweenClosed(blockPosition().relative(getDirection()), blockPosition().relative(getDirection()).above(4))) {
                    if (!(this.level().getBlockState(testPos).is(Blocks.GRASS) || this.level().getBlockState(testPos).is(Blocks.TALL_GRASS)))
                        if (this.level().getBlockState(testPos).is(GigTags.WEAK_BLOCKS) && !this.level().getBlockState(testPos).isAir()) {
                            if (!this.level().isClientSide)
                                this.level().destroyBlock(testPos, true, null, 512);
                            if (!this.isVehicle())
                                this.triggerAnim("attackController", "swipe");
                            if (this.isVehicle())
                                this.triggerAnim("attackController", "swipe_left_tail");
                            this.breakingCounter = -90;
                            if (this.level().isClientSide()) {
                                for (var i = 2; i < 10; i++)
                                    this.level().addAlwaysVisibleParticle(Particles.ACID, this.getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0), this.getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), 0.0, -0.15, 0.0);
                                this.level().playLocalSound(testPos.getX(), testPos.getY(), testPos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
                            }
                        } else if (!this.isVehicle() && !this.level().getBlockState(testPos).is(GigTags.ACID_RESISTANT) && !this.level().getBlockState(testPos).isAir() && (this.getHealth() >= (this.getMaxHealth() * 0.50))) {
                            if (!this.level().isClientSide)
                                this.level().setBlockAndUpdate(testPos.above(), GigBlocks.ACID_BLOCK.defaultBlockState());
                            this.hurt(damageSources().generic(), 5);
                            this.breakingCounter = -90;
                        }
                }
            if (this.breakingCounter >= 25)
                this.breakingCounter = 0;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        var multiplier = 1.0f;
        if (source == this.damageSources().onFire())
            multiplier = 2.0f;

        if (!this.level().isClientSide)
            if (source.getEntity() != null)
                if (source.getEntity() instanceof LivingEntity attacker)
                    this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, attacker);

        if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources()))
            return super.hurt(source, amount);

        if (!this.level().isClientSide && source != this.damageSources().genericKill()) {
            var acidThickness = this.getHealth() < (this.getMaxHealth() / 2) ? 1 : 0;

            if (this.getHealth() < (this.getMaxHealth() / 4))
                acidThickness += 1;
            if (amount >= 5)
                acidThickness += 1;
            if (amount > (this.getMaxHealth() / 10))
                acidThickness += 1;
            if (acidThickness == 0)
                return super.hurt(source, amount);

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
        return !(this.fallDistance > 0.1);
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> fluid) {
        super.jumpInLiquid(fluid);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    /*
     * GROWTH
     */
    @Override
    public float getMaxGrowth() {
        return Constants.TPM;
    }

    @Override
    public LivingEntity growInto() {
        return null;
    }

    /*
     * SOUNDS
     */
    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return GigSounds.ALIEN_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return GigSounds.ALIEN_DEATH;
    }

    public void grabTarget(Entity entity) {
        if (entity == this.getTarget() && !entity.hasPassenger(this) && !(entity.getFeetBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS)) {
            entity.startRiding(this, true);
            this.setAggressive(false);
            if (entity instanceof ServerPlayer player)
                player.connection.send(new ClientboundSetPassengersPacket(entity));
        }
    }
}
