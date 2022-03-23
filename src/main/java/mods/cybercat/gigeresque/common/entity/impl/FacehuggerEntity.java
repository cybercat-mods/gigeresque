package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Dynamic;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.brain.FacehuggerBrain;
import mods.cybercat.gigeresque.common.entity.ai.brain.sensor.SensorTypes;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.sound.Sounds;
import mods.cybercat.gigeresque.common.util.SoundUtil;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FacehuggerEntity extends AlienEntity implements IAnimatable {

	private final MobNavigation landNavigation = new MobNavigation(this, world);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, world);

	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final AquaticMoveControl swimMoveControl = new AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final YawAdjustingLookControl swimLookControl = new YawAdjustingLookControl(this, 10);

	public FacehuggerEntity(EntityType<? extends FacehuggerEntity> type, World world) {
		super(type, world);

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingPenalty(PathNodeType.WATER, 0.0f);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
				.add(EntityAttributes.GENERIC_ARMOR, 1.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3300000041723251);
	}

	private static final TrackedData<Boolean> IS_INFERTILE = DataTracker.registerData(FacehuggerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	private static final List<SensorType<? extends Sensor<? super FacehuggerEntity>>> SENSOR_TYPES = List
			.of(SensorType.NEAREST_LIVING_ENTITIES, SensorTypes.NEAREST_HOST, SensorTypes.ALIEN_REPELLENT);

	private static final List<MemoryModuleType<?>> MEMORY_MODULE_TYPES = List.of(MemoryModuleType.ATTACK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LOOK_TARGET, MemoryModuleType.MOBS,
			MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PATH,
			MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.WALK_TARGET);

	@Override
	protected void updatePostDeath() {
		++this.deathTime;
		if (this.deathTime == 4800 && this.isInfertile()) {
			this.remove(Entity.RemovalReason.KILLED);
			this.dropXp();
		} else {
			this.remove(Entity.RemovalReason.KILLED);
		}
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	private final AnimationFactory animationFactory = new AnimationFactory(this);

	private FacehuggerBrain complexBrain;

	public boolean isInfertile() {
		return dataTracker.get(IS_INFERTILE);
	}

	public void setIsInfertile(boolean value) {
		dataTracker.set(IS_INFERTILE, value);
	}

	public float ticksAttachedToHost = -1.0f;

	@Override
	protected Brain.Profile<? extends FacehuggerEntity> createBrainProfile() {
		return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
	}

	@Override
	protected Brain<? extends FacehuggerEntity> deserializeBrain(Dynamic<?> dynamic) {
		complexBrain = new FacehuggerBrain(this);
		return complexBrain.initialize(createBrainProfile().deserialize(dynamic));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Brain<FacehuggerEntity> getBrain() {
		return (Brain<FacehuggerEntity>) super.getBrain();
	}

	@Override
	protected void mobTick() {
		world.getProfiler().push("facehuggerBrain");
		complexBrain.tick();
		world.getProfiler().pop();
		complexBrain.tickActivities();
		super.mobTick();
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(IS_INFERTILE, false);
	}

	private void detachFromHost(boolean removesParasite) {
		this.stopRiding();
		this.ticksAttachedToHost = -1.0f;

		var vehicle = this.getVehicle();

		if (vehicle instanceof LivingEntity && removesParasite) {
			((Host) vehicle).removeParasite();
		}
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		if (this.world.isClient) {
			complexBrain.stun(Constants.TPS * 5);
		}
		return super.updatePassengerForDismount(passenger);
	}

	public boolean isAttachedToHost() {
		return this.getVehicle() != null && this.getVehicle() instanceof LivingEntity;
	}

	private void attachToHost(LivingEntity validHost) {
		this.grabTarget(validHost);
		this.startRiding(validHost);
		validHost.setMovementSpeed(0.0f);
	}

	@Override
	protected int computeFallDamage(float fallDistance, float damageMultiplier) {
		if (fallDistance <= 12)
			return 0;
		return super.computeFallDamage(fallDistance, damageMultiplier);
	}

	@Override
	public int getSafeFallDistance() {
		return 12;
	}

	public void grabTarget(Entity entity) {
		if (!entity.hasPassenger(this)) {
			this.startRiding(entity, true);
			if (entity instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (isAttachedToHost()) {
			ticksAttachedToHost += Gigeresque.config.miscellaneous.facehuggerAttachTickMultiplier;

			var host = (Host) this.getVehicle();

			if (host != null) {
				((LivingEntity) getVehicle())
						.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000, 10, false, false));
				if (ticksAttachedToHost > 4800 && host.doesNotHaveParasite()) {
					host.setTicksUntilImpregnation(9600);
					SoundUtil.playServerSound(world, null, this.getBlockPos(), Sounds.FACEHUGGER_IMPLANT,
							SoundCategory.NEUTRAL, 0.5f);
					setIsInfertile(true);
					this.kill();
				}
			}
		} else {
			ticksAttachedToHost = -1.0f;
		}

		var vehicle = this.getVehicle();
		if (vehicle != null && ((vehicle instanceof PlayerEntity && ((PlayerEntity) vehicle).isCreative())
				|| ticksAttachedToHost >= Constants.TPM * 5)) {
			detachFromHost(vehicle instanceof PlayerEntity && ((PlayerEntity) vehicle).isCreative());
		}

		if (isInfertile()) {
			this.clearGoalsAndTasks();
			return;
		}

		if (vehicle == null && !isInfertile()) {
			var target = this.getTarget();
			if (target == null) {
				target = brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
			}
			if (target == null)
				return;

			if (this.distanceTo(target) < target.getWidth() * 2 && canStartRiding(target)) {
				attachToHost(target);
			}
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("isInfertile", isInfertile());
		nbt.putFloat("ticksAttachedToHost", ticksAttachedToHost);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("isInfertile")) {
			setIsInfertile(nbt.getBoolean("isInfertile"));
		}
		if (nbt.contains("ticksAttachedToHost")) {
			ticksAttachedToHost = nbt.getFloat("ticksAttachedToHost");
		}
	}

	@Override
	public boolean tryAttack(Entity target) {
		return true;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (isAttachedToHost() && ticksAttachedToHost < Constants.TPS * 3 && amount >= 5.0f) {
			detachFromHost(true);
		}

		if ((isAttachedToHost() || isInfertile()) && (source == DamageSource.DROWN || source == DamageSource.IN_WALL)) {
			return false;
		}

		return super.damage(source, amount);
	}

	@Override
	public void takeKnockback(double strength, double x, double z) {
		if (!isInfertile()) {
			super.takeKnockback(strength, x, z);
		}
	}

	@Override
	public double squaredAttackRange(LivingEntity target) {
		return 0.0;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return (isAttachedToHost() || isInfertile()) ? null : Sounds.FACEHUGGER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return (isAttachedToHost() || isInfertile()) ? null : Sounds.FACEHUGGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return (isAttachedToHost() || isInfertile()) ? null : Sounds.FACEHUGGER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.05f, 10.0f);
	}

	@Override
	protected float calculateNextStepSoundDistance() {
		return this.distanceTraveled + 0.25f;
	}

	@Override
	protected void swimUpward(TagKey<Fluid> fluid) {
	}

	@Override
	public void stopRiding() {
		var vehicle = this.getVehicle();
		if (vehicle != null && vehicle instanceof LivingEntity && vehicle.isAlive()
				&& ticksAttachedToHost < Constants.TPM * 5 && (isSubmergedInWater() || isTouchingWater())) {
			return;
		}
		super.stopRiding();
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.submergedInWater || this.isTouchingWater()) ? swimLookControl : landLookControl;

		if (this.age % 10 == 0) {
			this.calculateDimensions();
		}

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
	public EntityNavigation createNavigation(World world) {
		return swimNavigation;
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		return this.submergedInWater ? super.getDimensions(pose).scaled(1.0f, 0.5f) : super.getDimensions(pose);
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();

		if (this.getVehicle() != null && this.getVehicle() instanceof LivingEntity) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hugging_loop", true));
			return PlayState.CONTINUE;
		}

		if (isInfertile()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("dead_loop", true));
			return PlayState.CONTINUE;
		}

		if (this.isSubmergedInWater()) {
			if (event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
				return PlayState.CONTINUE;
			}
		}

		if (velocityLength > 0.0 && !this.isSubmergedInWater()) {
			if (this.isAttacking()) {
				if (!this.isOnGround()) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("midair_loop", true));
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("moving_aggro", true));
					return PlayState.CONTINUE;
				}
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("moving_noaggro", true));
				return PlayState.CONTINUE;
			}
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
			return PlayState.CONTINUE;
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}
}
