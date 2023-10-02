package mods.cybercat.gigeresque.common.entity.impl.classic;

import java.util.List;
import java.util.SplittableRandom;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azuredoom.bettercrawling.common.ClimberLookController;
import mod.azuredoom.bettercrawling.common.ClimberMoveController;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyNestBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.ClassicXenoMeleeAttackTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.EggmorpthTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FindDarknessTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.JumpToTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.CrawlerAdultAlien;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class ClassicAlienEntity extends CrawlerAdultAlien implements SmartBrainOwner<ClassicAlienEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public ClassicAlienEntity(@NotNull EntityType<? extends CrawlerAdultAlien> type, @NotNull Level world) {
		super(type, world);
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : new ClimberMoveController<>(this);
		this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimLookControl : new ClimberLookController<>(this);;

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
		return this.wasEyeInWater ? EntityDimensions.scalable(3.0f, 1.0f) : this.isCrawling() ? EntityDimensions.scalable(0.9f, 0.9f) : EntityDimensions.scalable(0.9f, 1.9f);
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

		if (!this.isInWater())
			this.setIsCrawling(this.horizontalCollision || !this.level().getBlockState(this.blockPosition().below()).isSolid());

		if (!this.isVehicle())
			this.setIsExecuting(false);

		if (this.isExecuting())
			this.setPassedOutStatus(false);

		// Attack logic

		if (this.isExecuting())
			this.setDeltaMovement(0, 0, 0);

		if (this.isVehicle()) {
			var yOffset = this.getEyeY() - ((this.getFirstPassenger().getEyeY() - this.getFirstPassenger().blockPosition().getY()) / 2.0);
			var e = this.getFirstPassenger().getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
			var f = this.getFirstPassenger().getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
			if (!this.isExecuting())
				this.triggerAnim("attackController", "kidnap");
			if (this.getFirstPassenger() instanceof Mob mob && !mob.isPersistenceRequired())
				mob.setPersistenceRequired();
			if (this.isBiting()) {
				biteCounter++;
				if (biteCounter == 5) {
					this.triggerAnim("livingController", "grab");
					this.setDeltaMovement(0, 0, 0);
					this.setIsExecuting(true);
					this.setAggressive(false);
				}
				if (biteCounter >= 88) {
					this.getFirstPassenger().hurt(GigDamageSources.of(this.level(), GigDamageSources.EXECUTION), Integer.MAX_VALUE);
					this.heal(50);
					if (this.level().isClientSide)
						this.getFirstPassenger().level().addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
					this.setIsBiting(false);
					this.setIsExecuting(false);
					this.triggerAnim("attackController", "reset");
					biteCounter = 0;
				}
			} else {
				holdingCounter++;
				if (holdingCounter == 760) {
					this.triggerAnim("livingController", "grab");
					this.setDeltaMovement(0, 0, 0);
					this.setIsExecuting(true);
					this.setAggressive(false);
				}
				if (holdingCounter >= 843) {
					this.getFirstPassenger().hurt(GigDamageSources.of(this.level(), GigDamageSources.EXECUTION), Integer.MAX_VALUE);
					this.heal(50);
					if (this.level().isClientSide)
						this.getFirstPassenger().level().addAlwaysVisibleParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
					this.setIsExecuting(false);
					this.triggerAnim("attackController", "reset");
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
		if (target instanceof LivingEntity livingEntity && !this.level().isClientSide)
			if (this.getRandom().nextInt(0, 10) > 7) {
				if (livingEntity instanceof Player playerEntity)
					playerEntity.drop(playerEntity.getInventory().getSelected(), true, false);
				if (livingEntity instanceof Mob mobEntity)
					if (mobEntity.getMainHandItem() != null)
						mobEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
				livingEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
				livingEntity.hurt(GigDamageSources.of(this.level(), GigDamageSources.XENO), this.getRandom().nextInt(4) > 2 ? Gigeresque.config.classicXenoTailAttackDamage : 0.0f);
				this.heal(1.0833f);
				return super.doHurtTarget(target);
			}
		if (target instanceof Creeper creeper)
			creeper.hurt(GigDamageSources.of(this.level(), GigDamageSources.XENO), creeper.getMaxHealth());
		this.heal(1.0833f);
		return super.doHurtTarget(target);
	}

	@Override
	public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
		return this.getBbWidth() * ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.0f : 3.0f) * (this.getBbWidth() * ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.0f : 3.0f)) + livingEntity.getBbWidth();
	}

	@Override
	public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
		return this.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()) <= this.getMeleeAttackRangeSqr(livingEntity);
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
				new NearbyLivingEntitySensor<ClassicAlienEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self)),
				// Block Sensor
				new NearbyBlocksSensor<ClassicAlienEntity>().setRadius(7),
				// Fire Sensor
				new NearbyRepellentsSensor<ClassicAlienEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
				// Lights Sensor
				new NearbyLightsBlocksSensor<ClassicAlienEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)),
				// Nest Sensor
				new NearbyNestBlocksSensor<ClassicAlienEntity>().setRadius(30).setPredicate((block, entity) -> block.is(GigBlocks.NEST_RESIN_WEB_CROSS)), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				// Flee Fire
				new FleeFireTask<ClassicAlienEntity>(3.5F).whenStarting(entity -> entity.setFleeingStatus(true)).whenStarting(entity -> entity.setFleeingStatus(false)),
				// Take target to nest
				new EggmorpthTargetTask<>().stopIf(entity -> this.isFleeing()),
				// Looks at target
				new LookAtTarget<>().stopIf(entity -> this.isPassedOut() || this.isExecuting()).startCondition(entity -> !this.isPassedOut() || !this.isSearching() || !this.isExecuting()),
				// Move to target
				new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting()).stopIf(entity -> this.isExecuting()));
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				// Build Nest
				new BuildNestTask(90).startCondition(entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()).stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())),
				// Kill Lights
				new KillLightsTask<>().startCondition(entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()).stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())),
				// Do first
				new FirstApplicableBehaviour<ClassicAlienEntity>(
						// Targeting
						new TargetOrRetaliate<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
						// Look at players
						new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())).stopIf(entity -> this.isPassedOut() || this.isExecuting()),
						// Look around randomly
						new SetRandomLookTarget<>().startCondition(entity -> !this.isPassedOut() || !this.isSearching())).stopIf(entity -> this.isPassedOut() || this.isExecuting()),
				// Random
				new OneRandomBehaviour<>(
						// Find Darkness
						new FindDarknessTask<>().startCondition(entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()),
						// Randomly walk around
						new SetRandomWalkTarget<>().speedModifier(1.05f).startCondition(entity -> !this.isPassedOut() || !this.isExecuting()).stopIf(entity -> this.isExecuting() || this.isPassedOut()),
						// Idle
						new Idle<>().startCondition(entity -> (!this.isPassedOut() || !this.isAggressive() || this.isFleeing())).runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<ClassicAlienEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				// Invalidate Target
				new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this)),
				// Walk to Target
				new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> Gigeresque.config.classicXenoAttackSpeed).startCondition(entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()).stopIf(entity -> this.isPassedOut() || (this.isFleeing() || !this.hasLineOfSight(entity))),
				// Jump to Target
				new JumpToTargetTask<>(10),
				// Classic Xeno attacking
				new ClassicXenoMeleeAttackTask(5).startCondition(entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()).stopIf(entity -> (this.isFleeing())));
	}

	@Override
	public void positionRider(Entity entity, MoveFunction moveFunction) {
		if (entity instanceof LivingEntity mob) {
			var random = new SplittableRandom();
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
			mob.setSpeed(0);
		}
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (event.isMoving() && !this.isCrawling() && this.isExecuting() == false && !isDead && this.isPassedOut() == false) {
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
			} else if (!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && !this.onGround() && this.isExecuting() == false && this.isPassedOut() == false && !this.isVehicle())
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			else if (this.isCrawling() && !this.isExecuting() && !this.isPassedOut() && !this.isVehicle() && !this.isInWater())
				return event.setAndContinue(GigAnimationsDefault.CRAWL);
			return event.setAndContinue(this.isNoAi() ? GigAnimationsDefault.STATIS_ENTER : this.isSearching() && !this.isVehicle() && !this.isAggressive() && this.level().getBlockState(this.blockPosition().below()).isSolid() ? GigAnimationsDefault.AMBIENT : this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("handstepSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("ambientSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("thudSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("biteSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HEADBITE, SoundSource.HOSTILE, 1.0F, 1.0F, true);
			if (event.getKeyframeData().getSound().matches("crunchSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CRUNCH, SoundSource.HOSTILE, 1.0F, 1.0F, true);
		}).triggerableAnim("carry", GigAnimationsDefault.EXECUTION_CARRY) // carry
				.triggerableAnim("death", GigAnimationsDefault.DEATH) // death
				.triggerableAnim("grab", GigAnimationsDefault.EXECUTION_GRAB) // grab
				.triggerableAnim("idle", GigAnimationsDefault.IDLE_LAND)); // idle
		controllers.add(new AnimationController<>(this, "attackController", 0, event -> {
			if (event.getAnimatable().isPassedOut())
				return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
			return PlayState.STOP;
		}).triggerableAnim("kidnap", RawAnimation.begin().thenPlayXTimes("kidnap", 1)) // trigger kidnap hands
				.triggerableAnim("run", RawAnimation.begin().then("run", LoopType.PLAY_ONCE)) // trigger kidnap hands
				.triggerableAnim("reset", RawAnimation.begin().then("idle_land", LoopType.PLAY_ONCE)) // reset
				.triggerableAnim("death", GigAnimationsDefault.DEATH) // death
				.triggerableAnim("alert", GigAnimationsDefault.AMBIENT) // reset hands
				.triggerableAnim("passout", GigAnimationsDefault.STATIS_ENTER) // pass out
				.triggerableAnim("passoutloop", GigAnimationsDefault.STATIS_LOOP) // pass out
				.triggerableAnim("wakeup", GigAnimationsDefault.STATIS_LEAVE.then(this.isInWater() ? "idle_water" : "idle_land", LoopType.LOOP)) // wake up
				.triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
				.triggerableAnim("swipe_left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
				.triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
				.triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
				.triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
				.triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
				.triggerableAnim("left_claw_basic", GigAnimationsDefault.LEFT_CLAW_BASIC) // attack
				.triggerableAnim("right_claw_basic", GigAnimationsDefault.RIGHT_CLAW_BASIC) // attack
				.triggerableAnim("left_tail_basic", GigAnimationsDefault.LEFT_TAIL_BASIC) // attack
				.triggerableAnim("right_tail_basic", GigAnimationsDefault.RIGHT_TAIL_BASIC) // attack
				.setSoundKeyframeHandler(event -> {
					if (event.getKeyframeData().getSound().matches("clawSoundkey"))
						if (this.level().isClientSide)
							this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
					if (event.getKeyframeData().getSound().matches("tailSoundkey"))
						if (this.level().isClientSide)
							this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
					if (event.getKeyframeData().getSound().matches("crunchSoundkey"))
						if (this.level().isClientSide)
							this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CRUNCH, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				}));
		controllers.add(new AnimationController<>(this, "hissController", 0, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (this.isHissing() == true && !this.isVehicle() && this.isExecuting() == false && !isDead)
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
