package mods.cybercat.gigeresque.common.entity.impl.mutant;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.pathing.FlightMoveController;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.LeapAtTargetTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

public class StalkerEntity extends AlienEntity implements GeoEntity, SmartBrainOwner<StalkerEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private static final EntityDataAccessor<AlienAttackType> CURRENT_ATTACK_TYPE = SynchedEntityData.defineId(StalkerEntity.class, TrackedDataHandlers.ALIEN_ATTACK_TYPE);
	private final AzureNavigation landNavigation = new AzureNavigation(this, level());
	private final FlightMoveController roofMoveControl = new FlightMoveController(this);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	public int breakingCounter = 0;

	public StalkerEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		setMaxUpStep(1.5f);
		this.vibrationUser = new AzureVibrationUser(this, 1.9F);
		navigation = landNavigation;
		moveControl = landMoveControl;
		lookControl = landLookControl;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var velocityLength = this.getDeltaMovement().horizontalDistance();
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (velocityLength >= 0.000000001 && !isDead && this.getLastDamageSource() == null && event.getAnimatable().getAttckingState() == 0)
				if (walkAnimation.speedOld >= 0.35F && event.getAnimatable().isAggressive())
					return event.setAndContinue(GigAnimationsDefault.RUNNING);
				else
					return event.setAndContinue(GigAnimationsDefault.MOVING);
			if (this.getLastDamageSource() != null && this.hurtDuration > 0 && !isDead && event.getAnimatable().getAttckingState() == 0)
				return event.setAndContinue(RawAnimation.begin().then("hurt", LoopType.PLAY_ONCE));
			return event.setAndContinue(GigAnimationsDefault.IDLE);
		}).triggerableAnim("attack_heavy", GigAnimationsDefault.ATTACK_HEAVY) // attack
				.triggerableAnim("attack_normal", GigAnimationsDefault.ATTACK_NORMAL) // attack
				.triggerableAnim("death", GigAnimationsDefault.DEATH) // death
				.triggerableAnim("idle", GigAnimationsDefault.IDLE) // idle
		);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
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
	public List<ExtendedSensor<StalkerEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<StalkerEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self)),
				new NearbyBlocksSensor<StalkerEntity>().setRadius(7), new NearbyRepellentsSensor<StalkerEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new NearbyLightsBlocksSensor<StalkerEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<StalkerEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.3F), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<StalkerEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.entityData.get(FLEEING_FIRE).booleanValue() == true)), new FirstApplicableBehaviour<StalkerEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())), new SetRandomLookTarget<>()),
				new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.9f), new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<StalkerEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this)),
				new LeapAtTargetTask<>(0), new SetWalkTargetToAttackTarget<>().speedMod(Gigeresque.config.stalkerAttackSpeed), // move to
				new AlienMeleeAttack(13));// attack
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.stalkerXenoHealth).add(Attributes.ARMOR, Gigeresque.config.stalkerXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.stalkerAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide && getCurrentAttackType() == AlienAttackType.NONE)
			setCurrentAttackType(switch (random.nextInt(5)) {
			case 0 -> AlienAttackType.NORMAL;
			case 1 -> AlienAttackType.HEAVY;
			case 2 -> AlienAttackType.NORMAL;
			case 3 -> AlienAttackType.HEAVY;
			default -> AlienAttackType.NORMAL;
			});

		if (level().getBlockState(this.blockPosition()).is(GIgBlocks.ACID_BLOCK))
			this.level().removeBlock(this.blockPosition(), false);

		if (!this.isDeadOrDying() && !this.isInWater() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) == true && this.isAggressive()) {
			breakingCounter++;
			if (breakingCounter > 10)
				for (var testPos : BlockPos.betweenClosed(blockPosition().relative(getDirection()), blockPosition().relative(getDirection()).above(3))) {
					if (!(level().getBlockState(testPos).is(Blocks.GRASS) || level().getBlockState(testPos).is(Blocks.TALL_GRASS)))
						if (level().getBlockState(testPos).is(GigTags.WEAK_BLOCKS) && !level().getBlockState(testPos).isAir()) {
							if (!level().isClientSide)
								this.level().destroyBlock(testPos, true, null, 512);
							this.triggerAnim("attackController", "swipe");
							breakingCounter = -90;
							if (level().isClientSide()) {
								for (var i = 2; i < 10; i++)
									level().addAlwaysVisibleParticle(Particles.ACID, this.getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0), this.getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), 0.0, -0.15, 0.0);
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
		this.setNoGravity(!this.level().getBlockState(this.blockPosition().above()).isAir() && !this.level().getBlockState(this.blockPosition().above()).is(BlockTags.STAIRS) && !this.verticalCollision && !this.isDeadOrDying() && !this.isAggressive());
		this.setSpeed(this.isNoGravity() ? 0.7F : this.flyDist);
	}

	@Override
	public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
		return this.getBbWidth() * 3.0f * (this.getBbWidth() * 3.0f) + livingEntity.getBbWidth();
	}

	@Override
	public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
		double d = this.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
		return d <= this.getMeleeAttackRangeSqr(livingEntity);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public boolean doHurtTarget(Entity target) {
		var additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case HEAVY -> Gigeresque.config.stalkerTailAttackDamage;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !level().isClientSide)
			switch (getCurrentAttackType().genericAttackType) {
			case NORMAL -> {
				return super.doHurtTarget(target);
			}
			case HEAVY -> {
				target.hurt(damageSources().mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			}
		this.heal(1.0833f);
		return super.doHurtTarget(target);

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
		entityData.define(CURRENT_ATTACK_TYPE, AlienAttackType.NONE);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
	}

	@Override
	public boolean onClimbable() {
		setIsCrawling(this.horizontalCollision && !this.isNoGravity() && !this.level().getBlockState(this.blockPosition().above()).is(BlockTags.STAIRS) || this.isAggressive());
		return this.level().getBlockState(this.blockPosition().above()).is(BlockTags.STAIRS) || this.isAggressive() || this.fallDistance > 0.1 ? false : true;
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.moveControl = this.isNoGravity() ? roofMoveControl : landMoveControl;

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

}
