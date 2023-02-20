package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
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

public class RunnerAlienEntity extends AdultAlienEntity implements SmartBrainOwner<RunnerAlienEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public RunnerAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
		super(type, world);
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (isEffectiveAi() && this.isInWater()) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.5));
			if (getTarget() == null)
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
		} else
			super.travel(movementInput);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, GigeresqueConfig.runnerXenoHealth)
				.add(Attributes.ARMOR, GigeresqueConfig.runnerXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 6.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 7.0).add(Attributes.FOLLOW_RANGE, 32.0)
				.add(Attributes.MOVEMENT_SPEED, 0.13000000417232513)
				.add(Attributes.ATTACK_DAMAGE, GigeresqueConfig.runnerXenoAttackDamage)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
	}

	@Override
	public void tick() {
		super.tick();

		// Attack logic

		if (attackProgress > 0) {
			attackProgress--;

			if (!level.isClientSide && attackProgress <= 0)
				setCurrentAttackType(AlienAttackType.NONE);
		}

		if (attackProgress == 0 && swinging)
			attackProgress = 10;

		if (!level.isClientSide && getCurrentAttackType() == AlienAttackType.NONE)
			setCurrentAttackType(switch (random.nextInt(5)) {
			case 0 -> AlienAttackType.CLAW_LEFT_MOVING;
			case 1 -> AlienAttackType.CLAW_RIGHT_MOVING;
			case 2 -> AlienAttackType.TAIL_LEFT;
			case 3 -> AlienAttackType.TAIL_RIGHT;
			default -> AlienAttackType.CLAW_LEFT_MOVING;
			});
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.runnerAlienGrowthMultiplier;
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
	}

	@Override
	public List<ExtendedSensor<RunnerAlienEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<RunnerAlienEntity>().setPredicate((entity,
						target) -> !((entity instanceof AlienEntity || entity instanceof Warden
								|| entity instanceof ArmorStand || entity instanceof Bat)
								|| !target.hasLineOfSight(entity)
								|| (entity.getVehicle() != null && entity.getVehicle().getSelfAndPassengers()
										.anyMatch(AlienEntity.class::isInstance))
								|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding()
								|| ((Host) entity).hasParasite() || ((Eggmorphable) entity).isEggmorphing()
								|| (EntityUtils.isFacehuggerAttached(entity))
								|| (entity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)
										&& entity.isAlive() && entity.hasLineOfSight(target))),
				new NearbyBlocksSensor<RunnerAlienEntity>().setRadius(7),
				new NearbyRepellentsSensor<RunnerAlienEntity>().setRadius(15)
						.setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS)),
				new NearbyLightsBlocksSensor<RunnerAlienEntity>().setRadius(7)
						.setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)),
				new HurtBySensor<>(), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<RunnerAlienEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(3.5F), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<RunnerAlienEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new BuildNestTask(90).stopIf(entity -> this.entityData.get(FLEEING_FIRE).booleanValue() == true)
						.stopIf(target -> (this.isAggressive() || this.isVehicle())),
				new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle())),
				new FirstApplicableBehaviour<RunnerAlienEntity>(new TargetOrRetaliate<>(),
						new SetPlayerLookTarget<>().stopIf(target -> !target.isAlive()
								|| target instanceof Player && ((Player) target).isCreative()),
						new SetRandomLookTarget<>()),
				new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(1.05f),
						new Idle<>().startCondition(entity -> !this.isAggressive())
								.runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<RunnerAlienEntity> getFightTasks() {
		return BrainActivityGroup
				.fightTasks(
						new InvalidateAttackTarget<>().invalidateIf((entity, target) -> ((target instanceof AlienEntity
								|| target instanceof Warden || target instanceof ArmorStand || target instanceof Bat)
								|| !this.hasLineOfSight(target)
								|| (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers()
										.anyMatch(AlienEntity.class::isInstance))
								|| (target instanceof AlienEggEntity) || ((Host) target).isBleeding()
								|| ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing()
								|| (EntityUtils.isFacehuggerAttached(target))
								|| (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)
										&& !target.isAlive())),
						new SetWalkTargetToAttackTarget<>().speedMod(GigeresqueConfig.runnerXenoAttackSpeed),
						new AnimatableMeleeAttack(10));
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
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (isDead)
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			if (event.isMoving() && !this.isCrawling() && this.isExecuting() == false && !isDead
					&& this.isStatis() == false && !this.swinging) {
				if (!this.isInWater() && this.isExecuting() == false)
					if (animationSpeedOld > 0.35F && this.getFirstPassenger() == null)
						return event.setAndContinue(GigAnimationsDefault.RUN);
					else if (this.isExecuting() == false && animationSpeedOld < 0.35F
							|| (!this.isCrawling() && !this.onGround))
						return event.setAndContinue(GigAnimationsDefault.WALK);
					else if (this.wasEyeInWater && this.isExecuting() == false && !this.isVehicle())
						if (this.isAggressive() && !this.isVehicle())
							return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
						else
							return event.setAndContinue(GigAnimationsDefault.SWIM);
			} else if (getCurrentAttackType() == AlienAttackType.NONE)
				if (this.isStatis() == true || this.isNoAi() && !isDead)
					return event.setAndContinue(GigAnimationsDefault.STATIS_ENTER);
			return event.setAndContinue(
					this.isInWater() ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_FOOTSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("idleSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
		})).add(new AnimationController<>(this, "attackController", 1, event -> {
			if (this.entityData.get(IS_BREAKING) == true && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.LEFT_CLAW);
			if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.isVehicle()
					&& this.isExecuting() == false)
				return event.setAndContinue(RawAnimation.begin()
						.thenPlayXTimes(AlienAttackType.animationMappings.get(getCurrentAttackType()), 1));
			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("clawSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("tailSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
		})).add(new AnimationController<>(this, "hissController", 0, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead
					&& !this.isInWater())
				return event.setAndContinue(GigAnimationsDefault.HISS);
			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("hissSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_HISS, SoundSource.HOSTILE, 1.0F, 1.0F, true);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
