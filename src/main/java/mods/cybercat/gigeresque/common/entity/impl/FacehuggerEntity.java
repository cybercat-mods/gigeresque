package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goal.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goal.hugger.FacehugGoal;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.pathing.CrawlerNavigation;
import mods.cybercat.gigeresque.common.entity.ai.pathing.DirectPathNavigator;
import mods.cybercat.gigeresque.common.entity.ai.pathing.FlightMoveController;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.EntityUtils;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FacehuggerEntity extends AlienEntity implements IAnimatable, IAnimationTickable {

	private final CrawlerNavigation landNavigation = new CrawlerNavigation(this, level);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level);
	private final DirectPathNavigator roofNavigation = new DirectPathNavigator(this, level);
	private final FlightMoveController roofMoveControl = new FlightMoveController(this);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f,
			false);
	private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);
	public float ticksAttachedToHost = -1.0f;
	private final AnimationFactory animationFactory = new AnimationFactory(this);
	public static final EntityDataAccessor<Boolean> EGGSPAWN = SynchedEntityData.defineId(FacehuggerEntity.class,
			EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(FacehuggerEntity.class,
			EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(FacehuggerEntity.class,
			EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_INFERTILE = SynchedEntityData.defineId(FacehuggerEntity.class,
			EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(FacehuggerEntity.class,
			EntityDataSerializers.BOOLEAN);

	public FacehuggerEntity(EntityType<? extends FacehuggerEntity> type, Level world) {
		super(type, world);
		maxUpStep = 1.5f;
		navigation = landNavigation;
		moveControl = landMoveControl;
		lookControl = landLookControl;
		this.dynamicGameEventListener = new DynamicGameEventListener<VibrationListener>(
				new VibrationListener(new EntityPositionSource(this, (float)this.getEyeHeight() + 1), 48, this, null, 0.0f, 0));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.ARMOR, 1.0)
				.add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0)
				.add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251);
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

		var vehicle = this.getVehicle();

		if (vehicle instanceof LivingEntity && removesParasite) {
			((Host) vehicle).removeParasite();
		}
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		if (this.level.isClientSide) {
			// complexBrain.stun(Constants.TPS * 5);
		}
		return super.getDismountLocationForPassenger(passenger);
	}

	public boolean isAttachedToHost() {
		return this.getVehicle() != null && this.getVehicle() instanceof LivingEntity;
	}

	public void attachToHost(LivingEntity validHost) {
		this.grabTarget(validHost);
		this.startRiding(validHost);
		validHost.setSpeed(0.0f);
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
			if (entity instanceof ServerPlayer) {
				((ServerPlayer) entity).connection.send(new ClientboundSetPassengersPacket(entity));
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
						.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10, false, false));
				if (ticksAttachedToHost > GigeresqueConfig.getFacehuggerAttachTickTimer()
						&& host.doesNotHaveParasite()) {
					host.setTicksUntilImpregnation(GigeresqueConfig.getImpregnationTickTimer());
					if (!level.isClientSide) {
						this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
								GigSounds.HUGGER_IMPLANT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
					}
					setIsInfertile(true);
					this.unRide();
					this.hurt(DamageSource.MAGIC, this.getMaxHealth());
				}
			}

			var vehicle = this.getVehicle();
			if (vehicle != null && ((Host) vehicle).isBleeding() || vehicle instanceof Player
					&& (((Player) vehicle).isCreative() || ((Player) vehicle).isSpectator())) {
				this.stopRiding();
				detachFromHost(true);
				setIsInfertile(true);
				this.hurt(DamageSource.MAGIC, this.getMaxHealth());
			}
		} else {
			ticksAttachedToHost = -1.0f;
		}

		if (isInfertile()) {
			this.kill();
			this.removeFreeWill();
			return;
		}
		if (this.isEggSpawn() == true && this.tickCount > 30) {
			this.setEggSpawnState(false);
		}
		this.setNoGravity(
				!this.getLevel().getBlockState(this.blockPosition().above()).isAir() && !this.verticalCollision);
		this.setSpeed(this.isNoGravity() ? 0.7F : this.flyDist);
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
	public boolean doHurtTarget(Entity target) {
		return super.doHurtTarget(target);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (isAttachedToHost() && ticksAttachedToHost < Constants.TPS * 3 && amount >= 5.0f) {
			detachFromHost(true);
		}

		if ((isAttachedToHost() || isInfertile()) && (source == DamageSource.DROWN || source == DamageSource.IN_WALL)) {
			return false;
		}

		return super.hurt(source, amount);
	}

	@Override
	public void knockback(double strength, double x, double z) {
		if (!isInfertile()) {
			super.knockback(strength, x, z);
		}
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
		if (vehicle != null && vehicle instanceof LivingEntity && vehicle.isAlive()
				&& ticksAttachedToHost < Constants.TPM * 5 && (isInWater() || isInWater())) {
			return;
		}
		setIsInfertile(true);
		super.stopRiding();
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation
				: this.isCrawling() ? landNavigation : this.isNoGravity() ? roofNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl
				: this.isNoGravity() ? roofMoveControl : this.isCrawling() ? landMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (isEffectiveAi() && this.isInWater()) {
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
	public boolean isPathFinding() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return this.isInWater() ? swimNavigation
				: this.isCrawling() ? landNavigation : this.isNoGravity() ? roofNavigation : landNavigation;
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
		setIsCrawling(this.horizontalCollision);
		return this.horizontalCollision;
	}

	@Override
	protected void registerGoals() {
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true,
				entity -> !((entity instanceof AlienEntity || entity instanceof Warden || entity instanceof ArmorStand)
						|| (entity.getVehicle() != null
								&& entity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance))
						|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding()
						|| ((Eggmorphable) entity).isEggmorphing() || (EntityUtils.isFacehuggerAttached(entity))
						|| (entity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS))
						&& !ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, entity) && entity.isAlive()));
		this.goalSelector.addGoal(5, new FleeFireGoal<FacehuggerEntity>(this));
		this.goalSelector.addGoal(5, new FacehugGoal(this, 1.3D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 0));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D));
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.getVehicle() != null && this.getVehicle() instanceof LivingEntity && !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("impregnate", true));
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false
				&& this.entityData.get(ATTACKING) == false && isInfertile() || this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("dead", true));
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.isUnderWater()
				&& !this.isCrawling() && !this.isDeadOrDying()) {
			if (this.entityData.get(ATTACKING) == false && event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
				return PlayState.CONTINUE;
			} else if (this.entityData.get(ATTACKING) == true && event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
				return PlayState.CONTINUE;
			}
		}
		if (this.entityData.get(JUMPING) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("charge", true));
			event.getController().setAnimationSpeed(0.5);
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(EGGSPAWN) == true && !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hatch_leap", false));
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false
				&& this.entityData.get(ATTACKING) == true && !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_crawl", true));
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false
				&& this.entityData.get(ATTACKING) == false && this.entityData.get(EGGSPAWN) == false
				&& (animationSpeedOld > 0.05F) && !this.isCrawling() && !this.isAttacking() && !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		}
		if (this.entityData.get(UPSIDE_DOWN) == false && this.entityData.get(JUMPING) == false && this.isCrawling()
				&& !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
		return PlayState.CONTINUE;
	}

	private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("huggingSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.HUGGER_IMPLANT, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<FacehuggerEntity> controller = new AnimationController<FacehuggerEntity>(this, "controller",
				10f, this::predicate);
		controller.registerSoundListener(this::soundListener);
		data.addAnimationController(controller);
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}

	@Override
	public int tickTimer() {
		return tickCount;
	}
	
	@Override
	public void onSignalReceive(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4, Entity var5,
			Entity var6, float var7) {
		super.onSignalReceive(var1, var2, var3, var4, var5, var6, var7);
		this.getNavigation().moveTo(var3.getX(), var3.getY(), var3.getZ(), 0.9F);
	}
}
