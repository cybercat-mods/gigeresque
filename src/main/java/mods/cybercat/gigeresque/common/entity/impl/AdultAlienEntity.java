package mods.cybercat.gigeresque.common.entity.impl;

import static java.lang.Math.max;

import org.jetbrains.annotations.NotNull;

import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public abstract class AdultAlienEntity extends AlienEntity implements GeoEntity, Growable {

	protected static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Boolean> IS_HISSING = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
//	protected static final EntityDataAccessor<Float> STATIS_TIMER = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.FLOAT);
//	protected static final EntityDataAccessor<Boolean> IS_STATIS = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> PASSED_OUT = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> WAKING_UP = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> IS_BREAKING = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> IS_EXECUTION = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> IS_HEADBITE = SynchedEntityData.defineId(AdultAlienEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<AlienAttackType> CURRENT_ATTACK_TYPE = SynchedEntityData.defineId(AdultAlienEntity.class, TrackedDataHandlers.ALIEN_ATTACK_TYPE);
	protected final AzureNavigation landNavigation = new AzureNavigation(this, level());
	protected final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level());
	protected final MoveControl landMoveControl = new MoveControl(this);
	protected final SmoothSwimmingLookControl landLookControl = new SmoothSwimmingLookControl(this, 5);
	protected final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.5f, 1.0f, false);
	protected final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);
	protected long hissingCooldown = 0L;
	public int statisCounter = 0;
	public boolean isSearching = false;
	protected long searchingProgress = 0L;
	protected long searchingCooldown = 0L;
	protected int attackProgress = 0;
	public int holdingCounter = 0;
	public int breakingCounter = 0;
	public int biteCounter = 0;
	public int passoutCounter = 0;
	public int wakeupCounter = 0;

	public AdultAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
		super(type, world);
		setMaxUpStep(2.5f);
		this.vibrationUser = new AzureVibrationUser(this, 2.5F);
		navigation = landNavigation;
		moveControl = landMoveControl;
		lookControl = landLookControl;
		setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
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

		if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.9));
			if (getTarget() == null) {
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	public AlienAttackType getCurrentAttackType() {
		return entityData.get(CURRENT_ATTACK_TYPE);
	}

	public void setCurrentAttackType(AlienAttackType value) {
		entityData.set(CURRENT_ATTACK_TYPE, value);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(GROWTH, 0.0f);
		this.entityData.define(PASSED_OUT, false);
		this.entityData.define(WAKING_UP, false);
		entityData.define(IS_HISSING, false);
		entityData.define(IS_BREAKING, false);
		entityData.define(IS_EXECUTION, false);
		entityData.define(IS_HEADBITE, false);
		entityData.define(CURRENT_ATTACK_TYPE, AlienAttackType.NONE);
		// this.entityData.define(CLIENT_ANGER_LEVEL, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putFloat("growth", getGrowth());
		nbt.putBoolean("isStasis", this.isPassedOut());
		nbt.putBoolean("wakingup", this.isWakingUp());
		nbt.putBoolean("isHissing", isHissing());
		nbt.putBoolean("isBreaking", isBreaking());
		nbt.putBoolean("isExecuting", isExecuting());
		nbt.putBoolean("isHeadBite", isBiting());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		setGrowth(nbt.getFloat("getStatisTimer"));
		setGrowth(nbt.getFloat("growth"));
		setIsHissing(nbt.getBoolean("isHissing"));
		setIsBreaking(nbt.getBoolean("isBreaking"));
		setIsExecuting(nbt.getBoolean("isExecuting"));
		setIsExecuting(nbt.getBoolean("isHeadBite"));
		setPassedOutStatus(nbt.getBoolean("isStasis"));
		setWakingUpStatus(nbt.getBoolean("wakingup"));
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
			grow(this, 1 * getGrowthMultiplier());

		if (!level().isClientSide && this.isVehicle())
			this.setAggressive(false);

		// Passing and waking up logic
		var velocityLength = this.getDeltaMovement().horizontalDistance();
		if (!this.getTypeName().getString().equalsIgnoreCase("neomorph"))
			if ((velocityLength == 0 && !this.isVehicle() && this.isAlive() && !this.isSearching && !this.isHissing() && !this.isPassedOut())) {
				if (!this.level().isClientSide)
					this.passoutCounter++;
//				if (!this.level().isClientSide)
//					AzureLib.LOGGER.info(this.passoutCounter);
				if (this.passoutCounter >= 600) {
					this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
					this.triggerAnim("attackController", "passout");
					this.setPassedOutStatus(true);
				}
			}
		if (this.isPassedOut()) {
			if (this.isAggressive()) {
				this.triggerAnim("attackController", "wakeup");
				this.setPassedOutStatus(false);
				if (!this.level().isClientSide)
					this.passoutCounter = -600;
				this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 100, false, false));
			}
			if (this.tickCount < 2 && !this.isAggressive())
				this.triggerAnim("attackController", "passout");
		}

		// Hissing Logic
		if (!level().isClientSide && (!this.isSearching && !this.isVehicle() && this.isAlive() && this.isPassedOut() == false) && !this.isAggressive()) {
			hissingCooldown++;

			if (hissingCooldown == 20) {
				this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 100, false, false));
				setIsHissing(true);
			}

			if (hissingCooldown > 80) {
				setIsHissing(false);
				hissingCooldown = -500;
			}
		}

		// Searching Logic
		if (level().isClientSide && (velocityLength == 0 && this.getDeltaMovement().horizontalDistance() == 0.0 && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && !this.isAggressive() && !this.isHissing() && this.isAlive() && this.isPassedOut() == false)) {
			if (isSearching) {
				if (searchingProgress > Constants.TPS * 3) {
					searchingProgress = 0;
					searchingCooldown = Constants.TPS * 15L;
					isSearching = false;
				} else
					searchingProgress++;
			} else {
				searchingCooldown = max(searchingCooldown - 1, 0);

				if (searchingCooldown <= 0) {
					int next = random.nextInt(10);

					isSearching = next == 0 || next == 1;
				}
			}
		}

		if (level().getBlockState(this.blockPosition()).is(GIgBlocks.ACID_BLOCK))
			this.level().removeBlock(this.blockPosition(), false);

		if (!this.isCrawling() && !this.isDeadOrDying() && !this.isPassedOut() && this.isAggressive() && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) == true) {
			breakingCounter++;
			if (breakingCounter > 10)
				for (BlockPos testPos : BlockPos.betweenClosed(blockPosition().relative(getDirection()), blockPosition().relative(getDirection()).above(3))) {
					if (level().getBlockState(testPos).is(GigTags.WEAK_BLOCKS) && !level().getBlockState(testPos).isAir()) {
						if (!level().isClientSide)
							this.level().destroyBlock(testPos, true, null, 512);
						this.triggerAnim("attackController", "swipe");
						breakingCounter = -90;
						if (level().isClientSide()) {
							for (int i = 2; i < 10; i++) {
								level().addAlwaysVisibleParticle(Particles.ACID, this.getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0), this.getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), 0.0, -0.15, 0.0);
							}
							level().playLocalSound(testPos.getX(), testPos.getY(), testPos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
						}
					} else if (!level().getBlockState(testPos).is(GigTags.ACID_RESISTANT) && !level().getBlockState(testPos).isAir() && (getHealth() >= (getMaxHealth() * 0.50))) {
						if (!level().isClientSide)
							this.level().setBlockAndUpdate(testPos.above(), GIgBlocks.ACID_BLOCK.defaultBlockState());
						this.hurt(damageSources().generic(), 5);
						breakingCounter = -90;
					}
				}
			if (breakingCounter >= 25)
				breakingCounter = 0;
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		var multiplier = 1.0f;
		if (source == damageSources().onFire())
			multiplier = 2.0f;

		return super.hurt(source, amount * multiplier);
	}

	@Override
	public boolean onClimbable() {
//		setIsCrawling(this.horizontalCollision && this.isAggressive() && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && !this.hasEffect(MobEffects.LEVITATION));
//		return this.horizontalCollision && this.isAggressive() && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && !this.hasEffect(MobEffects.LEVITATION);
		return false;
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
		super.jumpInLiquid(fluid);
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
	}

	public boolean isCarryingEggmorphableTarget() {
		return !getPassengers().isEmpty() && GigEntityUtils.isEggmorphable(this.getFirstPassenger());
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
		if (entity == this.getTarget() && !entity.hasPassenger(this) && !(entity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)) {
			entity.startRiding(this, true);
			this.setAggressive(false);
			if (entity instanceof ServerPlayer player)
				player.connection.send(new ClientboundSetPassengersPacket(entity));
		}
	}
}
