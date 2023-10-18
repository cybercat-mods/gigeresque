package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;

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
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.CrawlerAdultAlien;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
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

public class RunnerAlienEntity extends CrawlerAdultAlien implements SmartBrainOwner<RunnerAlienEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public RunnerAlienEntity(EntityType<? extends CrawlerAdultAlien> type, Level world) {
		super(type, world);
		this.vibrationUser = new AzureVibrationUser(this, 1.75F);
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : new ClimberMoveController<>(this);
		this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimLookControl : new ClimberLookController<>(this);

		if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.5));
			if (getTarget() == null)
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
		} else
			super.travel(movementInput);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.runnerXenoHealth).add(Attributes.ARMOR, Gigeresque.config.runnerXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 6.0).add(Attributes.KNOCKBACK_RESISTANCE, 7.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.13000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.runnerXenoAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE,
				0.5);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (target instanceof LivingEntity livingEntity && !this.level().isClientSide)
			if (this.getRandom().nextInt(0, 10) > 7) {
				if (target instanceof Player playerEntity) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), false);
					playerEntity.getInventory().setItem(playerEntity.getInventory().selected, ItemStack.EMPTY);
				}
				if (livingEntity instanceof Mob mobEntity)
					if (mobEntity.getMainHandItem() != null)
						mobEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
				livingEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
				livingEntity.hurt(damageSources().mobAttack(this), this.getRandom().nextInt(4) > 2 ? Gigeresque.config.runnerXenoTailAttackDamage : 0.0f);
				this.heal(1.0833f);
				return super.doHurtTarget(target);
			}
		if (target instanceof Creeper creeper)
			creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
		this.heal(1.0833f);
		return super.doHurtTarget(target);
	}

	@Override
	public float getGrowthMultiplier() {
		return Gigeresque.config.runnerAlienGrowthMultiplier;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt) {
		if (spawnReason != MobSpawnType.NATURAL)
			setGrowth(getMaxGrowth());
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
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
	public List<ExtendedSensor<RunnerAlienEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(), new NearbyLivingEntitySensor<RunnerAlienEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self)), new NearbyBlocksSensor<RunnerAlienEntity>().setRadius(7), new NearbyRepellentsSensor<RunnerAlienEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
				new NearbyLightsBlocksSensor<RunnerAlienEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(), new UnreachableTargetSensor<>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<RunnerAlienEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				// Looks at target
				new LookAtTarget<>().stopIf(entity -> this.isPassedOut()).startCondition(entity -> !this.isPassedOut() || !this.isSearching()),
				// Flee Fire
				new FleeFireTask<>(3.5F),
				// Move to target
				new MoveToWalkTarget<>().startCondition(entity -> !this.isPassedOut()).stopIf(entity -> this.isPassedOut()));
	}

	@Override
	public BrainActivityGroup<RunnerAlienEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				// Build Nest
				new BuildNestTask(90).stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.entityData.get(FLEEING_FIRE).booleanValue() == true)),
				// Kill Lights
				new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle())),
				// Do first
				new FirstApplicableBehaviour<RunnerAlienEntity>(
						// Targeting
						new TargetOrRetaliate<>(),
						// Look at players
						new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
						// Look around randomly
						new SetRandomLookTarget<>()),
				// Random
				new OneRandomBehaviour<>(
						// Randomly walk around
						new SetRandomWalkTarget<>().speedModifier(1.05f).startCondition(entity -> !this.isPassedOut()).stopIf(entity -> this.isPassedOut()),
						// Idle
						new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<RunnerAlienEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				// Invalidate Target
				new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this)),
				// Walk to Target
				new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> Gigeresque.config.runnerXenoAttackSpeed).stopIf(entity -> this.isPassedOut()),
				// Xeno attacking
				new AlienMeleeAttack(10));
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
			if (event.isMoving() && !this.isCrawling() && this.isExecuting() == false && !isDead && this.isPassedOut() == false && !this.swinging)
				if (!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && this.isExecuting() == false)
					if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
						return event.setAndContinue(GigAnimationsDefault.RUN);
					else if (this.isExecuting() == false && walkAnimation.speedOld < 0.35F || (!this.isCrawling() && !this.onGround()))
						return event.setAndContinue(GigAnimationsDefault.WALK);
					else if ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && this.isExecuting() == false && !this.isVehicle())
						if (this.isAggressive() && !this.isVehicle())
							return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
						else
							return event.setAndContinue(GigAnimationsDefault.SWIM);
			return event.setAndContinue(this.isNoAi() ? GigAnimationsDefault.STATIS_ENTER : (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
		}).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
				.triggerableAnim("idle", GigAnimationsDefault.IDLE_LAND) // idle
				.setSoundKeyframeHandler(event -> {
					if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
						if (this.level().isClientSide)
							this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP, SoundSource.HOSTILE, 0.5F, 1.0F, true);
					if (event.getKeyframeData().getSound().matches("idleSoundkey"))
						if (this.level().isClientSide)
							this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F, true);
				})).add(new AnimationController<>(this, "attackController", 1, event -> {
					if (event.getAnimatable().isPassedOut())
						return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
					return PlayState.STOP;
				}).triggerableAnim("alert", GigAnimationsDefault.AMBIENT) // reset hands
						.triggerableAnim("death", GigAnimationsDefault.DEATH) // death
						.triggerableAnim("alert", GigAnimationsDefault.HISS) // reset hands
						.triggerableAnim("passout", GigAnimationsDefault.STATIS_ENTER) // pass out
						.triggerableAnim("passoutloop", GigAnimationsDefault.STATIS_LOOP) // pass out
						.triggerableAnim("wakeup", GigAnimationsDefault.STATIS_LEAVE.then("idle_land", LoopType.PLAY_ONCE)) // wake up
						.triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
						.triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
						.triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
						.triggerableAnim("left_tail_basic", GigAnimationsDefault.LEFT_TAIL_BASIC) // attack
						.triggerableAnim("right_tail_basic", GigAnimationsDefault.RIGHT_TAIL_BASIC) // attack
						.setSoundKeyframeHandler(event -> {
							if (event.getKeyframeData().getSound().matches("clawSoundkey"))
								if (this.level().isClientSide)
									this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
							if (event.getKeyframeData().getSound().matches("tailSoundkey"))
								if (this.level().isClientSide)
									this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
						}))
				.add(new AnimationController<>(this, "hissController", 0, event -> {
					var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
					if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8))
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
