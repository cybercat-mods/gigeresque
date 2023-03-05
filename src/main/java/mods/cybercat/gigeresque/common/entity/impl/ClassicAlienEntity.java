package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;

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
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyNestBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.ClassicXenoMeleeAttackTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.EggmorpthTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.JumpToTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
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

public class ClassicAlienEntity extends AdultAlienEntity implements SmartBrainOwner<ClassicAlienEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public ClassicAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
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
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, GigeresqueConfig.classicXenoHealth)
				.add(Attributes.ARMOR, GigeresqueConfig.classicXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 7.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 8.0).add(Attributes.FOLLOW_RANGE, 32.0)
				.add(Attributes.MOVEMENT_SPEED, 0.13000000417232513)
				.add(Attributes.ATTACK_DAMAGE, GigeresqueConfig.classicXenoAttackDamage)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 1.0);
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
			if (this.isCrawling() || this.isInWater())
				setCurrentAttackType(switch (random.nextInt(5)) {
				case 0 -> AlienAttackType.CLAW_LEFT;
				case 1 -> AlienAttackType.CLAW_RIGHT;
				case 2 -> AlienAttackType.TAIL_LEFT;
				case 3 -> AlienAttackType.TAIL_RIGHT;
				default -> AlienAttackType.CLAW_LEFT;
				});
			else
				setCurrentAttackType(switch (random.nextInt(5)) {
				case 0 -> AlienAttackType.CLAW_LEFT_MOVING;
				case 1 -> AlienAttackType.CLAW_RIGHT_MOVING;
				case 2 -> AlienAttackType.TAIL_LEFT_MOVING;
				case 3 -> AlienAttackType.TAIL_RIGHT_MOVING;
				default -> AlienAttackType.CLAW_LEFT_MOVING;
				});

		if (this.isAggressive())
			this.setPose(Pose.CROUCHING);
		else
			this.setPose(Pose.STANDING);

		if (this.isVehicle()) {
			var yOffset = this.getEyeY()
					- ((this.getFirstPassenger().getEyeY() - this.getFirstPassenger().blockPosition().getY()) / 2.0);
			var e = this.getFirstPassenger().getX()
					+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
			var f = this.getFirstPassenger().getZ()
					+ ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
			if (this.isBiting()) {
				biteCounter++;
				if (biteCounter == 5) {
					this.getNavigation().stop();
					this.setAggressive(false);
				}
				if (biteCounter >= 85) {
					this.getFirstPassenger().hurt(GigDamageSources.EXECUTION, Float.MAX_VALUE);
					this.heal(50);
					if (this.level.isClientSide)
						this.getFirstPassenger().level.addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0,
								-0.15, 0.0);
					this.setIsBiting(false);
					biteCounter = 0;
				}
			} else {
				holdingCounter++;
				if (holdingCounter == 760) {
					this.getNavigation().stop();
					this.setIsExecuting(true);
					this.setAggressive(false);
				}
				if (holdingCounter >= 840) {
					this.getFirstPassenger().hurt(GigDamageSources.EXECUTION, Float.MAX_VALUE);
					this.heal(50);
					if (this.level.isClientSide)
						this.getFirstPassenger().level.addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0,
								-0.15, 0.0);
					this.setIsExecuting(false);
					holdingCounter = 0;
				}
			}
		}
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.alienGrowthMultiplier;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		var additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case TAIL -> GigeresqueConfig.classicXenoTailAttackDamage;
		case EXECUTION -> Float.MAX_VALUE;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !level.isClientSide)
			switch (getAttckingState()) {
			case 1 -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 2 -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 3 -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty())
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 4 -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty())
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				target.hurt(DamageSource.mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
//			case 5 -> {
//				var health = ((LivingEntity) target).getHealth();
//				var maxhealth = ((LivingEntity) target).getMaxHealth();
//				if (health >= (maxhealth * 0.10)) {
//					target.hurt(DamageSource.mobAttack(this), Float.MAX_VALUE);
//					this.grabTarget(target);
//				}
//				return super.doHurtTarget(target);
//			}
			}
		this.heal(1.0833f);
		return super.doHurtTarget(target);
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

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
		var serverLevel = (ServerLevel) this.level;
		super.customServerAiStep();
		if (this.tickCount % 20 == 0) {
			this.angerManagement.tick(serverLevel, this::canTargetEntity);
			this.syncClientAngerLevel();
		}
	}

	@Override
	public List<ExtendedSensor<ClassicAlienEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<ClassicAlienEntity>().setPredicate((entity,
						target) -> !((entity instanceof AlienEntity || entity instanceof Warden
								|| entity instanceof ArmorStand || entity instanceof Bat)
								|| !target.hasLineOfSight(entity)
								|| (entity.getVehicle() != null && entity.getVehicle().getSelfAndPassengers()
										.anyMatch(AlienEntity.class::isInstance))
								|| (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)
								|| (target.getBlockStateOn().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)
								|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding()
								|| ((Host) entity).hasParasite() || ((Eggmorphable) entity).isEggmorphing()
								|| this.isVehicle() || (EntityUtils.isFacehuggerAttached(entity)) && entity.isAlive())),
				new NearbyBlocksSensor<ClassicAlienEntity>().setRadius(7),
				new NearbyRepellentsSensor<ClassicAlienEntity>().setRadius(15)
						.setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS)),
				new NearbyLightsBlocksSensor<ClassicAlienEntity>().setRadius(7)
						.setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)),
				new NearbyNestBlocksSensor<ClassicAlienEntity>().setRadius(30)
						.setPredicate((block, entity) -> block.is(GIgBlocks.NEST_RESIN_WEB_CROSS)),
				new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new FleeFireTask<ClassicAlienEntity>(3.5F).whenStarting(entity -> entity.setFleeingStatus(true))
						.whenStarting(entity -> entity.setFleeingStatus(false)),
				new EggmorpthTargetTask<>().stopIf(entity -> this.entityData.get(FLEEING_FIRE).booleanValue() == true),
				new LookAtTarget<>().startCondition(entity -> !this.isStatis() || !this.isSearching),
				new MoveToWalkTarget<>().stopIf(entity -> this.entityData.get(FLEEING_FIRE).booleanValue() == true));
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new BuildNestTask(90).stopIf(target -> (this.isAggressive() || this.isVehicle()
						|| this.entityData.get(FLEEING_FIRE).booleanValue() == true)),
				new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle()
						|| this.entityData.get(FLEEING_FIRE).booleanValue() == true)),
				new FirstApplicableBehaviour<ClassicAlienEntity>(
						new TargetOrRetaliate<>().stopIf(target -> (this.isAggressive() || this.isVehicle()
								|| this.entityData.get(FLEEING_FIRE).booleanValue() == true)),
						new SetPlayerLookTarget<>().predicate(target -> target.isAlive()
								&& !(((Player) target).isCreative() || ((Player) target).isSpectator())),
						new SetRandomLookTarget<>().startCondition(entity -> !this.isStatis() || !this.isSearching)),
				new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(1.05f), new Idle<>().startCondition(
						entity -> (!this.isAggressive() || this.entityData.get(FLEEING_FIRE).booleanValue() == true))
						.runFor(entity -> entity.getRandom().nextInt(1800, 2400))));
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>().invalidateIf((entity,
						target) -> ((target instanceof AlienEntity || target instanceof Warden
								|| target instanceof ArmorStand || target instanceof Bat)
								|| !(entity instanceof LivingEntity)
								|| (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers()
										.anyMatch(AlienEntity.class::isInstance))
								|| (target instanceof AlienEggEntity) || ((Host) target).isBleeding()
								|| ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing()
								|| (EntityUtils.isFacehuggerAttached(target))
								|| (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS)
										&& !target.isAlive())),
				new SetWalkTargetToAttackTarget<>().speedMod(GigeresqueConfig.classicXenoAttackSpeed)
						.stopIf(entity -> (this.entityData.get(FLEEING_FIRE).booleanValue() == true
								|| !this.hasLineOfSight(entity))),
				new JumpToTargetTask<>(10), new ClassicXenoMeleeAttackTask(10)
						.stopIf(entity -> (this.entityData.get(FLEEING_FIRE).booleanValue() == true)));
	}

	@Override
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		if (passenger instanceof LivingEntity) {
			var mob = (LivingEntity) passenger;
			SplittableRandom random = new SplittableRandom();
			mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10, true, true));
			var f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180));
			var g = Mth.cos(this.yBodyRot * ((float) Math.PI / 180));
			passenger.setPos(this.getX() + (double) ((this.isExecuting() == true ? -2.4f : -1.85f) * f),
					this.getY() + (double) (this.isExecuting() == true ? random.nextFloat(0.74F, 0.75f)
							: random.nextFloat(0.14F, 0.15F)),
					this.getZ() - (double) ((this.isExecuting() == true ? -2.4f : -1.85f) * g));
			mob.yBodyRot = this.yBodyRot;
		}
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (event.isMoving() && !this.isCrawling() && this.isExecuting() == false && !isDead
					&& this.isStatis() == false) {
				if (this.isOnGround() && !this.isInWater() && this.isExecuting() == false) {
					if (animationSpeedOld > 0.35F && this.getFirstPassenger() == null)
						return event.setAndContinue(GigAnimationsDefault.RUN);
					else if (!this.isCrawling())
						if (this.isVehicle())
							return event.setAndContinue(GigAnimationsDefault.WALK_CARRYING);
						else
							return event.setAndContinue(GigAnimationsDefault.WALK);
				} else if (this.isInWater() && this.isExecuting() == false && !this.isVehicle())
					if (this.isAggressive() && !this.isVehicle())
						return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
					else
						return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
			} else if (isDead && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			else if (!this.isInWater() && !this.isOnGround() && this.isExecuting() == false && this.isStatis() == false
					&& !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			else if (this.isCrawling() && this.isExecuting() == false && this.isStatis() == false && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			else if (getCurrentAttackType() == AlienAttackType.NONE)
				if (this.isExecuting() == true && !this.isVehicle() && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.EXECUTION_GRAB);
			return event.setAndContinue(this.isStatis() == true || this.isNoAi() ? GigAnimationsDefault.STATIS_ENTER
					: this.isSearching == true && !this.isVehicle() && !this.isAggressive()
							? GigAnimationsDefault.AMBIENT
							: this.isExecuting() == true && this.isVehicle() ? GigAnimationsDefault.EXECUTION_CARRY
									: this.isInWater() ? GigAnimationsDefault.IDLE_WATER
											: GigAnimationsDefault.IDLE_LAND);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("footstepSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_FOOTSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("handstepSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_HANDSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("ambientSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("thudSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_DEATH_THUD, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("biteSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_HEADBITE, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("crunchSoundkey")) {
				if (this.level.isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
							GigSounds.ALIEN_CRUNCH, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
		})).add(new AnimationController<>(this, "attackController", 1, event -> {
			if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.isVehicle()
					&& this.isExecuting() == false)
				return event.setAndContinue(RawAnimation.begin()
						.then(AlienAttackType.animationMappings.get(getCurrentAttackType()), LoopType.PLAY_ONCE));
			if (this.isVehicle() && this.isExecuting() == false)
				return event.setAndContinue(GigAnimationsDefault.KIDNAP);
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
		}));
		controllers.add(new AnimationController<>(this, "hissController", 0, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead)
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
