package mods.cybercat.gigeresque.common.entity.impl;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goal.FacehugGoal;
import mods.cybercat.gigeresque.common.entity.ai.goal.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.pathing.CrawlerNavigation;
import mods.cybercat.gigeresque.common.entity.ai.pathing.DirectPathNavigator;
import mods.cybercat.gigeresque.common.entity.ai.pathing.FlightMoveController;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.SoundUtil;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
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
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FacehuggerEntity extends AlienEntity implements IAnimatable, IAnimationTickable {

	private final CrawlerNavigation landNavigation = new CrawlerNavigation(this, world);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, world);
	private final DirectPathNavigator roofNavigation = new DirectPathNavigator(this, world);

	private final FlightMoveController roofMoveControl = new FlightMoveController(this);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final AquaticMoveControl swimMoveControl = new AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final YawAdjustingLookControl swimLookControl = new YawAdjustingLookControl(this, 10);

	public float ticksAttachedToHost = -1.0f;

	private final AnimationFactory animationFactory = new AnimationFactory(this);

	public static final TrackedData<Boolean> EGGSPAWN = DataTracker.registerData(FacehuggerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	public static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(FacehuggerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	public static final TrackedData<Boolean> JUMPING = DataTracker.registerData(FacehuggerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	public FacehuggerEntity(EntityType<? extends FacehuggerEntity> type, World world) {
		super(type, world);

		stepHeight = 1.5f;
		navigation = landNavigation;
		moveControl = landMoveControl;
		lookControl = landLookControl;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
				.add(EntityAttributes.GENERIC_ARMOR, 1.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3300000041723251);
	}

	private static final TrackedData<Boolean> IS_INFERTILE = DataTracker.registerData(FacehuggerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_CLIMBING = DataTracker.registerData(FacehuggerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	@Override
	protected void updatePostDeath() {
		++this.deathTime;
		if (this.deathTime == 200) {
			this.remove(Entity.RemovalReason.KILLED);
			super.updatePostDeath();
			this.dropXp();
		}
	}

	public boolean isEggSpawn() {
		return this.dataTracker.get(EGGSPAWN).booleanValue();
	}

	public void setEggSpawnState(boolean state) {
		this.dataTracker.set(EGGSPAWN, Boolean.valueOf(state));
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	public boolean isInfertile() {
		return dataTracker.get(IS_INFERTILE);
	}

	public void setIsInfertile(boolean value) {
		dataTracker.set(IS_INFERTILE, value);
	}

	public boolean isCrawling() {
		return dataTracker.get(IS_CLIMBING);
	}

	public void setIsCrawling(boolean isHissing) {
		dataTracker.set(IS_CLIMBING, isHissing);
	}

	public boolean isAttacking() {
		return dataTracker.get(ATTACKING);
	}

	public void setIsAttakcing(boolean isHissing) {
		dataTracker.set(ATTACKING, isHissing);
	}

	public boolean isJumping() {
		return dataTracker.get(JUMPING);
	}

	public void setIsJumping(boolean isHissing) {
		dataTracker.set(JUMPING, isHissing);
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(IS_INFERTILE, false);
		dataTracker.startTracking(IS_CLIMBING, false);
		dataTracker.startTracking(EGGSPAWN, false);
		dataTracker.startTracking(ATTACKING, false);
		dataTracker.startTracking(JUMPING, false);
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
			// complexBrain.stun(Constants.TPS * 5);
		}
		return super.updatePassengerForDismount(passenger);
	}

	public boolean isAttachedToHost() {
		return this.getVehicle() != null && this.getVehicle() instanceof LivingEntity;
	}

	public void attachToHost(LivingEntity validHost) {
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
			ticksAttachedToHost += 1;

			var host = (Host) this.getVehicle();

			if (host != null) {
				((LivingEntity) getVehicle())
						.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000, 10, false, false));
				if (ticksAttachedToHost > GigeresqueConfig.getFacehuggerAttachTickTimer()
						&& host.doesNotHaveParasite()) {
					host.setTicksUntilImpregnation(GigeresqueConfig.getImpregnationTickTimer());
					SoundUtil.playServerSound(world, null, this.getBlockPos(), GigSounds.FACEHUGGER_IMPLANT,
							SoundCategory.NEUTRAL, 0.5f);
					setIsInfertile(true);
					this.detach();
					this.damage(DamageSource.MAGIC, this.getMaxHealth());
				}
			}

			var vehicle = this.getVehicle();
			if (vehicle != null && ((Host) vehicle).isBleeding() || vehicle instanceof PlayerEntity
					&& (((PlayerEntity) vehicle).isCreative() || ((PlayerEntity) vehicle).isSpectator())) {
				this.stopRiding();
				detachFromHost(true);
				setIsInfertile(true);
				this.damage(DamageSource.MAGIC, this.getMaxHealth());
			}
		} else {
			ticksAttachedToHost = -1.0f;
		}

		if (isInfertile()) {
			this.clearGoalsAndTasks();
			this.kill();
			return;
		}
		if (this.isEggSpawn() == true && this.age > 30) {
			this.setEggSpawnState(false);
		}
		this.setNoGravity(!this.getWorld().getBlockState(this.getBlockPos().up()).isAir() && !this.verticalCollision);
		this.setMovementSpeed(this.hasNoGravity() ? 0.7F : this.speed);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("isInfertile", isInfertile());
		nbt.putFloat("ticksAttachedToHost", ticksAttachedToHost);
		nbt.putBoolean("isCrawling", isCrawling());
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
		if (nbt.contains("isCrawling")) {
			setIsCrawling(nbt.getBoolean("isCrawling"));
		}
	}

	@Override
	public boolean tryAttack(Entity target) {
		return super.tryAttack(target);
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
		return (isAttachedToHost() || isInfertile()) ? null : GigSounds.FACEHUGGER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return (isAttachedToHost() || isInfertile()) ? null : GigSounds.FACEHUGGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return (isAttachedToHost() || isInfertile()) ? null : GigSounds.FACEHUGGER_DEATH;
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
		setIsInfertile(true);
//		this.kill();
		super.stopRiding();
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation
				: this.isCrawling() ? landNavigation : this.hasNoGravity() ? roofNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl
				: this.hasNoGravity() ? roofMoveControl : this.isCrawling() ? landMoveControl : landMoveControl;
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
	public boolean isNavigating() {
		return false;
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public EntityNavigation createNavigation(World world) {
		return this.isTouchingWater() ? swimNavigation
				: this.isCrawling() ? landNavigation : this.hasNoGravity() ? roofNavigation : landNavigation;
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		return super.getDimensions(pose);
	}

	@Override
	public boolean isClimbing() {
		setIsCrawling(this.horizontalCollision);
		return this.horizontalCollision;
	}

	@Override
	protected void initGoals() {
		this.targetSelector.add(2,
				new ActiveTargetGoal<>(this, LivingEntity.class, true,
						entity -> !(entity instanceof AlienEntity) && !((Host) entity).isBleeding()
								|| !((Eggmorphable) entity).isEggmorphing()
								|| !(entity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)
								&& !ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, entity)));
		this.goalSelector.add(5, new FleeFireGoal<FacehuggerEntity>(this));
		this.goalSelector.add(5, new FacehugGoal(this, 0.9D));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F, 0));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.6D));
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.getVehicle() != null && this.getVehicle() instanceof LivingEntity && !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("impregnate", true));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(UPSIDE_DOWN) == false && this.dataTracker.get(JUMPING) == false
				&& this.dataTracker.get(ATTACKING) == false && isInfertile() || this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("dead", true));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(UPSIDE_DOWN) == false && this.dataTracker.get(JUMPING) == false
				&& this.isSubmergedInWater() && !this.isCrawling() && !this.isDead()) {
			if (this.dataTracker.get(ATTACKING) == false && event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
				return PlayState.CONTINUE;
			} else if (this.dataTracker.get(ATTACKING) == true && event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
				return PlayState.CONTINUE;
			}
		}
		if (this.dataTracker.get(JUMPING) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("charge", true));
			event.getController().setAnimationSpeed(0.5);
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(EGGSPAWN) == true && !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hatch_leap", false));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(UPSIDE_DOWN) == false && this.dataTracker.get(JUMPING) == false
				&& this.dataTracker.get(ATTACKING) == true && !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_crawl", true));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(UPSIDE_DOWN) == false && this.dataTracker.get(JUMPING) == false
				&& this.dataTracker.get(ATTACKING) == false && this.dataTracker.get(EGGSPAWN) == false
				&& (lastLimbDistance > 0.05F) && !this.isCrawling() && !this.isAttacking() && !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(UPSIDE_DOWN) == false && this.dataTracker.get(JUMPING) == false && this.isCrawling()
				&& !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}

	@Override
	public int tickTimer() {
		return age;
	}
}
