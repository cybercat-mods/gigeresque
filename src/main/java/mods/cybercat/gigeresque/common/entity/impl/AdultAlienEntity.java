package mods.cybercat.gigeresque.common.entity.impl;

import static java.lang.Math.max;

import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Growable;
import mods.cybercat.gigeresque.common.entity.ai.goal.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goal.classic.KillLightsGoal;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.pathing.CrawlerNavigation;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;

public abstract class AdultAlienEntity extends AlienEntity implements IAnimatable, Growable, IAnimationTickable {

	private static final TrackedData<Float> GROWTH = DataTracker.registerData(AdultAlienEntity.class,
			TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Boolean> IS_HISSING = DataTracker.registerData(AdultAlienEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_CLIMBING = DataTracker.registerData(AdultAlienEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	protected static final TrackedData<Boolean> IS_BREAKING = DataTracker.registerData(AdultAlienEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	protected static final TrackedData<Boolean> IS_EXECUTION = DataTracker.registerData(AdultAlienEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	protected final CrawlerNavigation landNavigation = new CrawlerNavigation(this, world);
	protected final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, world);
	protected final MoveControl landMoveControl = new MoveControl(this);
	protected final LookControl landLookControl = new LookControl(this);
	protected final AquaticMoveControl swimMoveControl = new AquaticMoveControl(this, 85, 10, 0.5f, 1.0f, false);
	protected final YawAdjustingLookControl swimLookControl = new YawAdjustingLookControl(this, 10);
	private long hissingCooldown = 0L;

	public AdultAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull World world) {
		super(type, world);
		stepHeight = 1.5f;

		navigation = landNavigation;
		moveControl = landMoveControl;
		lookControl = landLookControl;
		setPathfindingPenalty(PathNodeType.WATER, 0.0f);
	}

	public boolean isExecuting() {
		return dataTracker.get(IS_EXECUTION);
	}

	public void setIsExecuting(boolean isExecuting) {
		dataTracker.set(IS_EXECUTION, isExecuting);
	}

	public boolean isBreaking() {
		return dataTracker.get(IS_BREAKING);
	}

	public void setIsBreaking(boolean isBreaking) {
		dataTracker.set(IS_BREAKING, isBreaking);
	}

	public boolean isHissing() {
		return dataTracker.get(IS_HISSING);
	}

	public void setIsHissing(boolean isHissing) {
		dataTracker.set(IS_HISSING, isHissing);
	}

	public boolean isCrawling() {
		return dataTracker.get(IS_CLIMBING);
	}

	public void setIsCrawling(boolean isHissing) {
		dataTracker.set(IS_CLIMBING, isHissing);
	}

	public float getGrowth() {
		return dataTracker.get(GROWTH);
	}

	public void setGrowth(float growth) {
		dataTracker.set(GROWTH, growth);
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.submergedInWater || this.isTouchingWater()) ? swimLookControl : landLookControl;

		if (canMoveVoluntarily() && this.isTouchingWater()) {
			updateVelocity(getMovementSpeed(), movementInput);
			move(MovementType.SELF, getVelocity());
			setVelocity(getVelocity().multiply(0.9));
			if (getTarget() == null) {
				setVelocity(getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(GROWTH, 0.0f);
		dataTracker.startTracking(IS_HISSING, false);
		dataTracker.startTracking(IS_CLIMBING, false);
		dataTracker.startTracking(IS_BREAKING, false);
		dataTracker.startTracking(IS_EXECUTION, false);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putFloat("growth", getGrowth());
		nbt.putBoolean("isHissing", isHissing());
		nbt.putBoolean("isCrawling", isCrawling());
		nbt.putBoolean("isBreaking", isBreaking());
		nbt.putBoolean("isExecuting", isExecuting());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("growth")) {
			setGrowth(nbt.getFloat("growth"));
		}
		if (nbt.contains("isHissing")) {
			setIsHissing(nbt.getBoolean("isHissing"));
		}
		if (nbt.contains("isCrawling")) {
			setIsCrawling(nbt.getBoolean("isCrawling"));
		}
		if (nbt.contains("isBreaking")) {
			setIsBreaking(nbt.getBoolean("isBreaking"));
		}
		if (nbt.contains("isExecuting")) {
			setIsExecuting(nbt.getBoolean("isExecuting"));
		}
	}

	@Override
	public int computeFallDamage(float fallDistance, float damageMultiplier) {
		if (fallDistance <= 15)
			return 0;
		return super.computeFallDamage(fallDistance, damageMultiplier);
	}

	@Override
	public int getSafeFallDistance() {
		return 9;
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
			EntityData entityData, NbtCompound entityNbt) {
		if (spawnReason != SpawnReason.NATURAL) {
			setGrowth(getMaxGrowth());
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	public void tick() {
		super.tick();

		if (!world.isClient && this.isAlive()) {
			grow(this, 1 * getGrowthMultiplier());
		}

		if (!world.isClient && this.hasPassengers()) {
			this.setAttacking(false);
		}

		// Hissing Logic

		if (!world.isClient && isHissing() && !this.hasPassengers()) {
			hissingCooldown = max(hissingCooldown - 1, 0);

			if (hissingCooldown <= 0) {
				setIsHissing(false);
			}
		}
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		var multiplier = 1.0f;
		if (source.isFire()) {
			multiplier = 2.0f;
		} else if (source.isProjectile()) {
			multiplier = 0.5f;
		}

		var isolationModeMultiplier = GigeresqueConfig.isolationMode ? 0.05f : 1.0f;

		return super.damage(source, amount * multiplier * isolationModeMultiplier);
	}

	@Override
	public boolean onKilledOther(ServerWorld world, LivingEntity other) {
		if (!world.isClient)
			this.heal(1.0833f);
		return super.onKilledOther(world, other);
	}

	@Override
	public boolean isClimbing() {
		setIsCrawling(this.horizontalCollision && this.getTarget() != null);
		return this.horizontalCollision && this.getTarget() != null;
	}

	@Override
	protected void swimUpward(TagKey<Fluid> fluid) {
		super.swimUpward(fluid);
	}

	@Override
	public EntityNavigation createNavigation(World world) {
		return (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation : landNavigation;
	}

	public boolean isCarryingEggmorphableTarget() {
		return !getPassengerList().isEmpty() && EntityUtils.isEggmorphable(this.getFirstPassenger());
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

	@Override
	protected void initGoals() {
		this.goalSelector.add(5, new FleeFireGoal<AdultAlienEntity>(this));
		this.goalSelector.add(5, new KillLightsGoal(this));
		this.goalSelector.add(1, new SwimAroundGoal(this, 1.0D, 10));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.15D));
		this.targetSelector.add(2,
				new ActiveTargetGoal<>(this, LivingEntity.class, true,
						entity -> !((entity instanceof AlienEntity) || (entity instanceof WardenEntity)
								|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding()
								|| ((Eggmorphable) entity).isEggmorphing() || (EntityUtils.isFacehuggerAttached(entity))
								|| (entity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS))));
		this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge());
	}

	@Override
	public int tickTimer() {
		return age;
	}
}
