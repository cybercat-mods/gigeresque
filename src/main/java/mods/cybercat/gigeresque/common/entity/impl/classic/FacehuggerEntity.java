package mods.cybercat.gigeresque.common.entity.impl.classic;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FacehuggerPounceTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.common.util.GigVibrationListener;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

public class FacehuggerEntity extends AlienEntity implements GeoEntity, SmartBrainOwner<FacehuggerEntity> {

	private final WallClimberNavigation landNavigation = new WallClimberNavigation(this, level);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level);
//	private final DirectPathNavigator roofNavigation = new DirectPathNavigator(this, level);
//	private final FlightMoveController roofMoveControl = new FlightMoveController(this);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);
	public float ticksAttachedToHost = -1.0f;
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	public static final EntityDataAccessor<Boolean> EGGSPAWN = SynchedEntityData.defineId(FacehuggerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(FacehuggerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(FacehuggerEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_INFERTILE = SynchedEntityData.defineId(FacehuggerEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(FacehuggerEntity.class, EntityDataSerializers.BOOLEAN);

	public FacehuggerEntity(EntityType<? extends FacehuggerEntity> type, Level world) {
		super(type, world);
		setMaxUpStep(1.5f);
		navigation = landNavigation;
		moveControl = landMoveControl;
		lookControl = landLookControl;
		this.dynamicGameEventListener = new DynamicGameEventListener<GigVibrationListener>(new GigVibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 48, this));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, GigeresqueConfig.facehuggerHealth).add(Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251);
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 200) {
			this.remove(Entity.RemovalReason.KILLED);
			super.tickDeath();
			this.dropExperience();
		}
	}

	public boolean isEggSpawn() {
		return this.entityData.get(EGGSPAWN).booleanValue();
	}

	public void setEggSpawnState(boolean state) {
		this.entityData.set(EGGSPAWN, Boolean.valueOf(state));
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	public boolean isInfertile() {
		return entityData.get(IS_INFERTILE);
	}

	public void setIsInfertile(boolean value) {
		entityData.set(IS_INFERTILE, value);
	}

	public boolean isCrawling() {
		return entityData.get(IS_CLIMBING);
	}

	public void setIsCrawling(boolean isHissing) {
		entityData.set(IS_CLIMBING, isHissing);
	}

	public boolean isAttacking() {
		return entityData.get(ATTACKING);
	}

	public void setIsAttakcing(boolean isHissing) {
		entityData.set(ATTACKING, isHissing);
	}

	public boolean isJumping() {
		return entityData.get(JUMPING);
	}

	public void setIsJumping(boolean isHissing) {
		entityData.set(JUMPING, isHissing);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(IS_INFERTILE, false);
		entityData.define(IS_CLIMBING, false);
		entityData.define(EGGSPAWN, false);
		entityData.define(ATTACKING, false);
		entityData.define(JUMPING, false);
	}

	private void detachFromHost(boolean removesParasite) {
		this.stopRiding();
		this.ticksAttachedToHost = -1.0f;
		this.kill();

		var vehicle = this.getVehicle();

		if (vehicle instanceof LivingEntity && removesParasite)
			((Host) vehicle).removeParasite();
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		return super.getDismountLocationForPassenger(passenger);
	}

	public boolean isAttachedToHost() {
		return this.getVehicle() != null && this.getVehicle() instanceof LivingEntity;
	}

	public void attachToHost(LivingEntity validHost) {
		this.grabTarget(validHost);
		this.startRiding(validHost);
		validHost.setSpeed(0.0f);
		if (GigeresqueConfig.facehuggerGivesBlindness == true)
			validHost.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, (int) GigeresqueConfig.facehuggerAttachTickTimer, 0));
	}

	@Override
	protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
		if (fallDistance <= 12)
			return 0;
		return super.calculateFallDamage(fallDistance, damageMultiplier);
	}

	@Override
	public int getMaxFallDistance() {
		return 12;
	}

	public void grabTarget(Entity entity) {
		if (!entity.hasPassenger(this)) {
			this.startRiding(entity, true);
			if (entity instanceof ServerPlayer)
				((ServerPlayer) entity).connection.send(new ClientboundSetPassengersPacket(entity));
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (isAttachedToHost()) {
			ticksAttachedToHost += 1;

			var host = (Host) this.getVehicle();

			if (host != null) {
				((LivingEntity) getVehicle()).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10, false, false));
				if (ticksAttachedToHost > GigeresqueConfig.getFacehuggerAttachTickTimer() && host.doesNotHaveParasite()) {
					host.setTicksUntilImpregnation(GigeresqueConfig.getImpregnationTickTimer());
					host.hasParasite();
					if (((LivingEntity) host).hasEffect(MobEffects.BLINDNESS))
						((LivingEntity) host).removeEffect(MobEffects.BLINDNESS);
					if (!level.isClientSide) {
						this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.HUGGER_IMPLANT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
					}
					setIsInfertile(true);
					this.unRide();
					this.hurt(damageSources().outOfWorld(), Float.MAX_VALUE);
				}
			}

			var vehicle = this.getVehicle();
			if (vehicle != null && ((Host) vehicle).isBleeding() || vehicle instanceof Player && (((Player) vehicle).isCreative() || ((Player) vehicle).isSpectator())) {
				if (((Player) vehicle).hasEffect(MobEffects.BLINDNESS))
					((Player) vehicle).removeEffect(MobEffects.BLINDNESS);
				this.stopRiding();
				detachFromHost(true);
				setIsInfertile(true);
				this.kill();
			}
		} else
			ticksAttachedToHost = -1.0f;

		if (isInfertile()) {
			this.kill();
			this.removeFreeWill();
			return;
		}
		if (this.isEggSpawn() == true && this.tickCount > 30)
			this.setEggSpawnState(false);
		if (this.getTarget() != null) {
			var target = this.getTarget();
			var huggerchecklist = !((target instanceof AlienEntity || target instanceof Warden || target instanceof ArmorStand) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target instanceof AlienEggEntity) || ((Host) target).isBleeding() || target.getMobType() == MobType.UNDEAD || ((Eggmorphable) target).isEggmorphing() || (EntityUtils.isFacehuggerAttached(target))
					|| (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)) && ConfigAccessor.isTargetHostable(target) && target.isAlive();
			if (this.getBoundingBox().intersects(this.getTarget().getBoundingBox()) && huggerchecklist && !this.isDeadOrDying())
				this.attachToHost(this.getTarget());
		}
//		this.setNoGravity(!this.getLevel().getBlockState(this.blockPosition().above()).isAir()
//		&& !this.verticalCollision && !this.isDeadOrDying() && !this.isAggressive());
//		this.setSpeed(this.isNoGravity() ? 0.7F : this.flyDist);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("isInfertile", isInfertile());
		nbt.putFloat("ticksAttachedToHost", ticksAttachedToHost);
		nbt.putBoolean("isCrawling", isCrawling());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("isInfertile"))
			setIsInfertile(nbt.getBoolean("isInfertile"));
		if (nbt.contains("ticksAttachedToHost"))
			ticksAttachedToHost = nbt.getFloat("ticksAttachedToHost");
		if (nbt.contains("isCrawling"))
			setIsCrawling(nbt.getBoolean("isCrawling"));
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		return super.doHurtTarget(target);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (isAttachedToHost() && ticksAttachedToHost < Constants.TPS * 3 && amount >= 5.0f)
			detachFromHost(true);

		if ((isAttachedToHost() || isInfertile()) && (source == damageSources().drown() || source == damageSources().inWall()))
			return false;

		return super.hurt(source, amount);
	}

	@Override
	public void knockback(double strength, double x, double z) {
		if (!isInfertile())
			super.knockback(strength, x, z);
	}

	@Override
	public double getMeleeAttackRangeSqr(LivingEntity target) {
		return 0.0;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return (isAttachedToHost() || isInfertile()) ? null : GigSounds.HUGGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return (isAttachedToHost() || isInfertile()) ? null : GigSounds.HUGGER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(SoundEvents.STRIDER_STEP, 0.05f, 10.0f);
	}

	@Override
	protected float nextStep() {
		return this.moveDist + 0.25f;
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
	}

	@Override
	public void stopRiding() {
		var vehicle = this.getVehicle();
		if (vehicle != null && vehicle instanceof LivingEntity && vehicle.isAlive() && ticksAttachedToHost < Constants.TPM * 5 && (isInWater() || isInWater()))
			return;
		setIsInfertile(true);
		super.stopRiding();
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation : this.isCrawling() ? landNavigation :
//					this.isNoGravity() ? roofNavigation : 
				landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl : this.isNoGravity() ?
//						roofMoveControl : this.isCrawling() ? 
				landMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (isEffectiveAi() && this.isInWater()) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.9));
			if (getTarget() == null)
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
		} else
			super.travel(movementInput);
	}

	@Override
	public boolean isPathFinding() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return this.isInWater() ? swimNavigation : this.isCrawling() ? landNavigation :
//					this.isNoGravity() ? roofNavigation :
				landNavigation;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return super.getDimensions(pose);
	}

	@Override
	public boolean onClimbable() {
		setIsCrawling(this.horizontalCollision && !this.isNoGravity());
		return true;
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
		super.customServerAiStep();
	}

	@Override
	public List<ExtendedSensor<FacehuggerEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<FacehuggerEntity>().setPredicate((target,
						entity) -> !((target instanceof AlienEntity || target instanceof Warden || target instanceof ArmorStand || target instanceof Bat || target instanceof IronGolem) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target instanceof AlienEggEntity) || ((Host) target).isBleeding() || ((Host) target).hasParasite() || target.getMobType() == MobType.UNDEAD || ((Eggmorphable) target).isEggmorphing()
								|| (EntityUtils.isFacehuggerAttached(target)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)) && ConfigAccessor.isTargetHostable(target) && target.isAlive() && entity.hasLineOfSight(target)),
				new NearbyBlocksSensor<FacehuggerEntity>().setRadius(7), new NearbyRepellentsSensor<FacehuggerEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new HurtBySensor<>(), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<FacehuggerEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.2F), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<FacehuggerEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<FacehuggerEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().stopIf(target -> !target.isAlive() || target instanceof Player && ((Player) target).isCreative()), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.65f), new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<FacehuggerEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((entity, target) -> ((target instanceof AlienEntity || target instanceof Warden || target instanceof ArmorStand || target instanceof Bat || target instanceof IronGolem) || !this.hasLineOfSight(target) || !(entity instanceof LivingEntity) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target instanceof AlienEggEntity)
				|| ((Host) target).isBleeding() || ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing() || !ConfigAccessor.isTargetHostable(target) || (EntityUtils.isFacehuggerAttached(target)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) && !target.isAlive())), new SetWalkTargetToAttackTarget<>().speedMod(1.05F), new FacehuggerPounceTask(10));
	}

	@Override
	protected void registerGoals() {
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			if (this.getVehicle() != null && this.getVehicle() instanceof LivingEntity && !this.isDeadOrDying())
				return event.setAndContinue(GigAnimationsDefault.IMPREGNATE);
			if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.entityData.get(ATTACKING) == false && isInfertile() || this.isDeadOrDying())
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.isUnderWater() && !this.isCrawling() && !this.isDeadOrDying())
				if (this.entityData.get(ATTACKING) == false && event.isMoving())
					return event.setAndContinue(GigAnimationsDefault.SWIM);
				else if (this.entityData.get(ATTACKING) == true && event.isMoving())
					return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
				else
					return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
			if (this.entityData.get(JUMPING) == true)
				return event.setAndContinue(GigAnimationsDefault.CHARGE);
			if (this.entityData.get(EGGSPAWN) == true && !this.isDeadOrDying())
				return event.setAndContinue(GigAnimationsDefault.HATCH_LEAP);
			if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.entityData.get(ATTACKING) == true && !this.isDeadOrDying()) {
				event.getController().setAnimationSpeed(1 + event.getAnimatable().getSpeed());
				return event.setAndContinue(GigAnimationsDefault.CRAWL_RUSH);
			}
			if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.entityData.get(ATTACKING) == false && this.entityData.get(EGGSPAWN) == false && (walkAnimation.speedOld > 0.05F) && !this.isCrawling() && !this.isAttacking() && !this.isDeadOrDying()) {
				event.getController().setAnimationSpeed(1 + event.getAnimatable().getSpeed());
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			}
			if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.isCrawling() && !this.isDeadOrDying()) {
				event.getController().setAnimationSpeed(1 + event.getAnimatable().getSpeed());
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			}
			return event.setAndContinue(GigAnimationsDefault.IDLE_LAND);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("huggingSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.HUGGER_IMPLANT, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	public void onSignalReceive(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4, Entity var5, Entity var6, float var7) {
		super.onSignalReceive(var1, var2, var3, var4, var5, var6, var7);
		if (!(var6 instanceof IronGolem))
			BrainUtils.setMemory(this, MemoryModuleType.WALK_TARGET, new WalkTarget(var3, 1.2F, 0));
	}
}
