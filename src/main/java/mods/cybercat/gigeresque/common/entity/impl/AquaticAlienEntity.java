package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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

public class AquaticAlienEntity extends AdultAlienEntity implements SmartBrainOwner<AquaticAlienEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final GroundPathNavigation landNavigation = new GroundPathNavigation(this, level);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);

	public AquaticAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
		super(type, world);
		maxUpStep = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, GigeresqueConfig.aquaticXenoHealth).add(Attributes.ARMOR, GigeresqueConfig.aquaticXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 9.0).add(Attributes.KNOCKBACK_RESISTANCE, 9.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.2500000417232513).add(Attributes.ATTACK_DAMAGE, GigeresqueConfig.aquaticXenoAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE,
				0.85);
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.aquaticAlienGrowthMultiplier;
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (this.tickCount % 10 == 0)
			this.refreshDimensions();

		if (isEffectiveAi() && this.isInWater()) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(!this.wasTouchingWater ? 0.25 : 0.65));
			if (getTarget() == null) {
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else
			super.travel(movementInput);
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return swimNavigation;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.wasEyeInWater ? EntityDimensions.scalable(3.0f, 1.0f) : super.getDimensions(pose);
	}

	@Override
	public void refreshDimensions() {
		super.refreshDimensions();
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
	public List<ExtendedSensor<AquaticAlienEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<AquaticAlienEntity>().setRadius(30)
						.setPredicate((entity,
								target) -> !((entity instanceof AlienEntity || entity instanceof Warden || entity instanceof ArmorStand || entity instanceof Bat) || !target.hasLineOfSight(entity) || (entity.getVehicle() != null && entity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding() || ((Host) entity).hasParasite() || ((Eggmorphable) entity).isEggmorphing()
										|| (EntityUtils.isFacehuggerAttached(entity)) || (entity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) && entity.isAlive() && entity.hasLineOfSight(target))),
				new NearbyBlocksSensor<AquaticAlienEntity>().setRadius(7), new NearbyRepellentsSensor<AquaticAlienEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new NearbyLightsBlocksSensor<AquaticAlienEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<AquaticAlienEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(0.5F), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<AquaticAlienEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new KillLightsTask<>(), new FirstApplicableBehaviour<AquaticAlienEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().stopIf(target -> !target.isAlive() || target instanceof Player && ((Player) target).isCreative()), new SetRandomLookTarget<>()),
				new OneRandomBehaviour<>(new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(!this.wasTouchingWater ? 0.75F : 1.5f), new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<AquaticAlienEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((entity, target) -> ((target instanceof AlienEntity || target instanceof Warden || target instanceof ArmorStand || target instanceof Bat) || !(entity instanceof LivingEntity) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target instanceof AlienEggEntity) || ((Host) target).isBleeding() || ((Host) target).hasParasite()
				|| ((Eggmorphable) target).isEggmorphing() || (EntityUtils.isFacehuggerAttached(target)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) && !target.isAlive())), new SetWalkTargetToAttackTarget<>().speedMod(!this.wasTouchingWater ? 0.95F : 1.5F), new AnimatableMeleeAttack(10));
	}

	@Override
	protected void registerGoals() {
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
			case 2 -> AlienAttackType.TAIL_LEFT_MOVING;
			case 3 -> AlienAttackType.TAIL_RIGHT_MOVING;
			default -> AlienAttackType.CLAW_LEFT_MOVING;
			});
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public boolean doHurtTarget(Entity target) {
		var additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case TAIL -> GigeresqueConfig.aquaticXenoTailAttackDamage;
		case EXECUTION -> Float.MAX_VALUE;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !level.isClientSide)
			switch (getCurrentAttackType().genericAttackType) {
			case CLAW -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
			}
			case TAIL -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false).collect(Collectors.toList());
				if (!armorItems.isEmpty()) {
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				}
			}
			case EXECUTION -> {
				double yOffset = this.getEyeY() - ((target.getEyeY() - target.blockPosition().getY()) / 2.0);
				double e = target.getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				double f = target.getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
				holdingCounter++;
				if (holdingCounter == 760) {
					this.getNavigation().stop();
					this.setIsExecuting(true);
					this.setAggressive(false);
				}
				if (holdingCounter == 850) {
					target.hurt(GigDamageSources.EXECUTION, Float.MAX_VALUE);
					target.level.addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
					this.setIsExecuting(false);
					holdingCounter = 0;
				}
				return super.doHurtTarget(target);
			}
			}

		this.heal(1.0833f);
		target.hurt(DamageSource.mobAttack(this), additionalDamage);

		return super.doHurtTarget(target);
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.isUnderWater() && this.wasTouchingWater) {
				if (this.isAggressive() && event.isMoving() && !isDead && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
				else if (!this.isAggressive() && event.isMoving() && !isDead && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.SWIM);
				else if (isDead)
					return event.setAndContinue(GigAnimationsDefault.DEATH);
				else
					return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
			} else {
				if (this.isAggressive() && event.isMoving() && !isDead && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.CRAWL_RUSH);
				else if (!this.isAggressive() && event.isMoving() && !isDead && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.CRAWL);
				else if (isSearching && !this.isAggressive() && !isDead && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.AMBIENT);
				else if (isDead)
					return event.setAndContinue(GigAnimationsDefault.DEATH);
				else if (this.isStatis() == true || this.isNoAi())
					return event.setAndContinue(GigAnimationsDefault.STATIS_ENTER);
				else if (this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.IDLE_LAND2);
				return PlayState.CONTINUE;
			}
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("stepSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.AQUA_LANDMOVE, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("clawSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.AQUA_LANDCLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("idleSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 0.25F, 1.0F, true);
		})).add(new AnimationController<>(this, "attackController", 1, event -> {
			if (this.entityData.get(IS_BREAKING) == true && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.LEFT_CLAW);
			if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.isVehicle() && this.isExecuting() == false)
				return event.setAndContinue(RawAnimation.begin().then(AlienAttackType.animationMappings.get(getCurrentAttackType()), LoopType.PLAY_ONCE));
			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("clawSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("tailSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
				}
			}
		})).add(new AnimationController<>(this, "hissController", 0, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead)
				return event.setAndContinue(GigAnimationsDefault.HISS);

			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("hissSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
