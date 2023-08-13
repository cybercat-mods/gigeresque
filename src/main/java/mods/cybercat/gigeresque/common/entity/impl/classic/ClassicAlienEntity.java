package mods.cybercat.gigeresque.common.entity.impl.classic;

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
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
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
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
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
		this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimLookControl : landLookControl;

		if (this.tickCount % 10 == 0)
			this.refreshDimensions();

		if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.5));
			if (getTarget() == null)
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
		} else
			super.travel(movementInput);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.wasEyeInWater ? EntityDimensions.scalable(3.0f, 1.0f) : this.isCrawling() ? EntityDimensions.scalable(0.9f, 0.9f) : super.getDimensions(pose);
	}

	@Override
	public void refreshDimensions() {
		super.refreshDimensions();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.classicXenoHealth).add(Attributes.ARMOR, Gigeresque.config.classicXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 7.0).add(Attributes.KNOCKBACK_RESISTANCE, 8.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.13000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.classicXenoAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 1.0);
	}

	@Override
	public void tick() {
		super.tick();
		
		if (!this.isVehicle()) 
			this.setIsExecuting(false);

		// Attack logic

		if (attackProgress > 0) {
			attackProgress--;
			if (!level().isClientSide && attackProgress <= 0)
				setCurrentAttackType(AlienAttackType.NONE);
		}

		if (attackProgress == 0 && swinging)
			attackProgress = 10;

		if (!level().isClientSide && getCurrentAttackType() == AlienAttackType.NONE)
			if (this.isCrawling() || this.wasEyeInWater)
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

		if (this.isExecuting())
			this.setDeltaMovement(0, 0, 0);

		if (this.isVehicle()) {
			var yOffset = this.getEyeY() - ((this.getFirstPassenger().getEyeY() - this.getFirstPassenger().blockPosition().getY()) / 2.0);
			var e = this.getFirstPassenger().getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
			var f = this.getFirstPassenger().getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
			if (this.isBiting()) {
				biteCounter++;
				if (biteCounter == 5) {
					this.getNavigation().stop();
					this.setDeltaMovement(0, 0, 0);
					this.setIsExecuting(true);
					this.setAggressive(false);
				}
				if (biteCounter >= 88) {
					this.getFirstPassenger().hurt(damageSources().generic(), Float.MAX_VALUE);
					this.heal(50);
					if (this.level().isClientSide)
						this.getFirstPassenger().level().addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
					this.setIsBiting(false);
					this.setIsExecuting(false);
					biteCounter = 0;
				}
			} else {
				holdingCounter++;
				if (holdingCounter == 760) {
					this.getNavigation().stop();
					this.setDeltaMovement(0, 0, 0);
					this.setIsExecuting(true);
					this.setAggressive(false);
				}
				if (holdingCounter >= 843) {
					this.getFirstPassenger().hurt(damageSources().generic(), Float.MAX_VALUE);
					this.heal(50);
					if (this.level().isClientSide)
						this.getFirstPassenger().level().addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
					this.setIsExecuting(false);
					holdingCounter = 0;
				}
			}
		}
	}

	@Override
	public float getGrowthMultiplier() {
		return Gigeresque.config.alienGrowthMultiplier;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt) {
		if (spawnReason != MobSpawnType.NATURAL)
			setGrowth(getMaxGrowth());
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		var additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case TAIL -> Gigeresque.config.classicXenoTailAttackDamage;
		case EXECUTION -> Float.MAX_VALUE;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !level().isClientSide)
			switch (getAttckingState()) {
			case 1 -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
				target.hurt(damageSources().mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 2 -> {
				if (target instanceof Player playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
					playerEntity.getInventory().removeItem(playerEntity.getInventory().getSelected());
				}
				target.hurt(damageSources().mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 3 -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false).collect(Collectors.toList());
				if (!armorItems.isEmpty())
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				target.hurt(damageSources().mobAttack(this), additionalDamage);
				return super.doHurtTarget(target);
			}
			case 4 -> {
				var armorItems = StreamSupport.stream(target.getArmorSlots().spliterator(), false).collect(Collectors.toList());
				if (!armorItems.isEmpty())
					armorItems.get(new Random().nextInt(armorItems.size())).hurtAndBreak(10, this, it -> {
					});
				target.hurt(damageSources().mobAttack(this), additionalDamage);
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
		return this.getBbWidth() * ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.0f : 3.0f) * (this.getBbWidth() * ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.0f : 3.0f)) + livingEntity.getBbWidth();
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
		super.customServerAiStep();
	}

	@Override
	public List<ExtendedSensor<ClassicAlienEntity>> getSensors() {
		return ObjectArrayList.of(
				// Player Sensor
				new NearbyPlayersSensor<>(),
				// Living Sensor
				new NearbyLivingEntitySensor<ClassicAlienEntity>().setPredicate((entity,
						target) -> !((entity instanceof AlienEntity || entity instanceof Warden || entity instanceof ArmorStand || entity instanceof Bat) || !target.hasLineOfSight(entity) || (entity.getVehicle() != null && entity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) || (target.getBlockStateOn().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) || (entity instanceof AlienEggEntity)
								|| entity.getCommandSenderWorld().getBlockStates(entity.getBoundingBox().inflate(1)).anyMatch(state -> state.is(GIgBlocks.NEST_RESIN_WEB_CROSS)) || ((Host) entity).isBleeding() || ((Host) entity).hasParasite() || ((Eggmorphable) entity).isEggmorphing() || this.isVehicle() || (GigEntityUtils.isFacehuggerAttached(entity)) && entity.isAlive())),
				// Block Sensor
				new NearbyBlocksSensor<ClassicAlienEntity>().setRadius(7), 
				// Fire Sensor
				new NearbyRepellentsSensor<ClassicAlienEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), 
				// Lights Sensor
				new NearbyLightsBlocksSensor<ClassicAlienEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)),
				// Nest Sensor
				new NearbyNestBlocksSensor<ClassicAlienEntity>().setRadius(30).setPredicate((block, entity) -> block.is(GIgBlocks.NEST_RESIN_WEB_CROSS)), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				// Flee Fire
				new FleeFireTask<ClassicAlienEntity>(3.5F).whenStarting(entity -> entity.setFleeingStatus(true)).whenStarting(entity -> entity.setFleeingStatus(false)), 
				// Take target to nest
				new EggmorpthTargetTask<>().stopIf(entity -> this.entityData.get(FLEEING_FIRE).booleanValue() == true), 
				// Looks at target
				new LookAtTarget<>().startCondition(entity -> !this.isStatis() || !this.isSearching), 
				// Move to target
				new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting()).stopIf(entity -> this.isExecuting()));
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				// Build Nest
				new BuildNestTask(90).stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isStatis() || this.entityData.get(FLEEING_FIRE).booleanValue() == true)), 
				// Kill Lights
				new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.entityData.get(FLEEING_FIRE).booleanValue() == true)),
				// Do first
				new FirstApplicableBehaviour<ClassicAlienEntity>(
						// Targeting
						new TargetOrRetaliate<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.entityData.get(FLEEING_FIRE).booleanValue() == true)), 
						// Look at players
						new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && !(((Player) target).isCreative() || ((Player) target).isSpectator())), 
						// Look around randomly
						new SetRandomLookTarget<>().startCondition(entity -> !this.isStatis() || !this.isSearching)),
				// Random
				new OneRandomBehaviour<>(
						// Randomly walk around
						new SetRandomWalkTarget<>().speedModifier(1.05f).startCondition(entity -> !this.isExecuting()).stopIf(entity -> this.isExecuting() || !this.isStatis()), 
						// Idle
						new Idle<>().startCondition(entity -> (!this.isAggressive() || this.entityData.get(FLEEING_FIRE).booleanValue() == true)).runFor(entity -> entity.getRandom().nextInt(1800, 2400))));
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>().invalidateIf((entity,
						target) -> ((target instanceof AlienEntity || target instanceof Warden || target instanceof ArmorStand || target instanceof Bat) || !(entity instanceof LivingEntity) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target instanceof AlienEggEntity) || ((Host) target).isBleeding() || ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing()
								|| (GigEntityUtils.isFacehuggerAttached(target)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) && !target.isAlive())),
				new SetWalkTargetToAttackTarget<>().speedMod(Gigeresque.config.classicXenoAttackSpeed).stopIf(entity -> (this.entityData.get(FLEEING_FIRE).booleanValue() == true || !this.hasLineOfSight(entity))), new JumpToTargetTask<>(10), new ClassicXenoMeleeAttackTask(10).stopIf(entity -> (this.entityData.get(FLEEING_FIRE).booleanValue() == true)));
	}

	@Override
	public void positionRider(Entity entity, MoveFunction moveFunction) {
		if (entity instanceof LivingEntity mob) {
			SplittableRandom random = new SplittableRandom();
			mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, true, true));
			var f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180));
			var g = Mth.cos(this.yBodyRot * ((float) Math.PI / 180));
			var y1 = random.nextFloat(0.14F, 0.15F);
			var y3 = random.nextFloat(0.44F, 0.45F);
			var y = random.nextFloat(0.74F, 0.75f);
			var y2 = random.nextFloat(1.14F, 1.15f);
			mob.setPos(this.getX() + (double) ((this.isExecuting() == true ? -2.4f : -1.85f) * f), this.getY() + (double) (this.isExecuting() == true ? (mob.getBbHeight() < 1.4 ? y2 : y) : (mob.getBbHeight() < 1.4 ? y3 : y1)), this.getZ() - (double) ((this.isExecuting() == true ? -2.4f : -1.85f) * g));
			mob.yBodyRot = this.yBodyRot;
			mob.xxa = this.xxa;
			mob.zza = this.zza;
			mob.yya = this.yya;
		}
    }

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (event.isMoving() && !this.isCrawling() && this.isExecuting() == false && !isDead && this.isStatis() == false) {
				if (this.onGround() && !this.wasEyeInWater && this.isExecuting() == false) {
					if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
						return event.setAndContinue(GigAnimationsDefault.RUN);
					else if (!this.isCrawling())
						if (this.isVehicle())
							return event.setAndContinue(GigAnimationsDefault.WALK_CARRYING);
						else
							return event.setAndContinue(GigAnimationsDefault.WALK);
				} else if (this.wasEyeInWater && this.isExecuting() == false && !this.isVehicle())
					if (this.isAggressive() && !this.isVehicle())
						return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
					else
						return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
			} else if (isDead && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			else if (!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && !this.onGround() && this.isExecuting() == false && this.isStatis() == false && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			else if (this.isCrawling() && this.isExecuting() == false && this.isStatis() == false && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			else if (getCurrentAttackType() == AlienAttackType.NONE)
				if (this.isExecuting() == true && !this.isVehicle() && this.isStatis() == false)
					return event.setAndContinue(GigAnimationsDefault.EXECUTION_GRAB);
			return event.setAndContinue(this.isStatis() == true || this.isNoAi() ? GigAnimationsDefault.STATIS_ENTER : this.isSearching == true && !this.isVehicle() && !this.isAggressive() ? GigAnimationsDefault.AMBIENT : this.isExecuting() == true && this.isVehicle() ? GigAnimationsDefault.EXECUTION_CARRY : this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("footstepSoundkey")) {
				if (this.level().isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("handstepSoundkey")) {
				if (this.level().isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("ambientSoundkey")) {
				if (this.level().isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("thudSoundkey")) {
				if (this.level().isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("biteSoundkey")) {
				if (this.level().isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HEADBITE, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
			if (event.getKeyframeData().getSound().matches("crunchSoundkey")) {
				if (this.level().isClientSide) {
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CRUNCH, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}
			}
		})).add(new AnimationController<>(this, "attackController", 1, event -> {
			if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0 && !this.isVehicle() && this.isExecuting() == false)
				return event.setAndContinue(RawAnimation.begin().then(AlienAttackType.animationMappings.get(getCurrentAttackType()), LoopType.PLAY_ONCE));
			if (this.isVehicle() && this.isExecuting() == false)
				return event.setAndContinue(GigAnimationsDefault.KIDNAP);
			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("clawSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("tailSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
		}));
		controllers.add(new AnimationController<>(this, "hissController", 0, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead)
				return event.setAndContinue(GigAnimationsDefault.HISS);
			return PlayState.STOP;
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("hissSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS, SoundSource.HOSTILE, 1.0F, 1.0F, true);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

}
