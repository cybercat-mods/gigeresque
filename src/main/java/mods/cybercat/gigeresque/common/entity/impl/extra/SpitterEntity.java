package mods.cybercat.gigeresque.common.entity.impl.extra;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
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
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

public class SpitterEntity extends AdultAlienEntity implements GeoEntity, SmartBrainOwner<SpitterEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final AzureNavigation landNavigation = new AzureNavigation(this, level());
	public int breakingCounter = 0;

	public SpitterEntity(EntityType<? extends AlienEntity> entityType, Level world) {
		super(entityType, world);
		setMaxUpStep(1.5f);
		this.vibrationUser = new AzureVibrationUser(this, 1.9F);
		navigation = landNavigation;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (event.isMoving() && !this.isCrawling() && !isDead) {
				if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
					return event.setAndContinue(GigAnimationsDefault.RUN);
				else
					return event.setAndContinue(GigAnimationsDefault.WALK);
			}
			return event.setAndContinue(GigAnimationsDefault.IDLE);
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
		}).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
				.triggerableAnim("idle", GigAnimationsDefault.IDLE)) // idle
				.add(new AnimationController<>(this, "attackController", 0, event -> {
					if (event.getAnimatable().isPassedOut())
						return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
					return PlayState.STOP;
				}).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
						.triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
						.triggerableAnim("swipe_left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
						.triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
						.triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
						.triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
						.triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
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
						}))
				.add(new AnimationController<>(this, "hissController", 0, event -> {
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
	public List<ExtendedSensor<SpitterEntity>> getSensors() {
		return ObjectArrayList.of(
				// Player Sensor
				new NearbyPlayersSensor<>(),
				// Living Sensor
				new NearbyLivingEntitySensor<SpitterEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self)),
				// Block Sensor
				new NearbyBlocksSensor<SpitterEntity>().setRadius(7), 
				// Fire Sensor
				new NearbyRepellentsSensor<SpitterEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
				// Lights Sensor
				new NearbyLightsBlocksSensor<SpitterEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), 
				// Nest Sensor
				new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<SpitterEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				// Looks at target
				new LookAtTarget<>(), 
				// Flee Fire
				new FleeFireTask<>(1.3F),
//				new StrafeTarget<>().speedMod(0.25F), 
				// Move to target
				new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<SpitterEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				// Kill Lights
				new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())), 
				// Do first
				new FirstApplicableBehaviour<SpitterEntity>(
						// Targeting
						new TargetOrRetaliate<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
						// Look at players
						new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())).stopIf(entity -> this.isPassedOut() || this.isExecuting()),
						// Look around randomly
						new SetRandomLookTarget<>().startCondition(entity -> !this.isPassedOut() || !this.isSearching())).stopIf(entity -> this.isPassedOut() || this.isExecuting()),
				// Random
				new OneRandomBehaviour<>(
						// Randomly walk around
						new SetRandomWalkTarget<>().speedModifier(1.15f), 
						// Idle
						new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<SpitterEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				// Invalidate Target
				new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this)), 
				// Walk to Target
				new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 2.5F), 
				// Xeno attacking
				new AlienMeleeAttack(5));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.spitterXenoHealth).add(Attributes.ARMOR, Gigeresque.config.spitterXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.spitterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
	}

	@Override
	public void tick() {
		super.tick();

		if (level().getBlockState(this.blockPosition()).is(GigBlocks.ACID_BLOCK))
			this.level().removeBlock(this.blockPosition(), false);

		if (!this.isVehicle() && !this.isDeadOrDying() && !this.isInWater() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) == true) {
			if (!this.level().isClientSide)
				breakingCounter++;
			if (breakingCounter > 10)
				for (BlockPos testPos : BlockPos.betweenClosed(blockPosition().relative(getDirection()), blockPosition().relative(getDirection()).above(3))) {
					if (level().getBlockState(testPos).is(GigTags.WEAK_BLOCKS) && !level().getBlockState(testPos).isAir()) {
						if (!level().isClientSide)
							this.level().destroyBlock(testPos, true, null, 512);
						this.swing(InteractionHand.MAIN_HAND);
						breakingCounter = -90;
						if (level().isClientSide()) {
							for (var i = 2; i < 10; i++)
								level().addAlwaysVisibleParticle(Particles.ACID, this.getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0), this.getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1), 0.0, -0.15, 0.0);
							level().playLocalSound(testPos.getX(), testPos.getY(), testPos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
						}
					} else if (!level().getBlockState(testPos).is(GigTags.ACID_RESISTANT) && !level().getBlockState(testPos).isAir() && (getHealth() >= (getMaxHealth() * 0.50))) {
						if (!level().isClientSide)
							this.level().setBlockAndUpdate(testPos.above(), GigBlocks.ACID_BLOCK.defaultBlockState());
						this.hurt(damageSources().generic(), 5);
						breakingCounter = -90;
					}
				}
			if (breakingCounter >= 25)
				breakingCounter = 0;
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt) {
		if (spawnReason != MobSpawnType.NATURAL)
			setGrowth(getMaxGrowth());
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
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
	public boolean doHurtTarget(Entity target) {
		if (target instanceof LivingEntity livingEntity && !this.level().isClientSide)
			if (this.getRandom().nextInt(0, 10) > 7) {
				if (livingEntity instanceof Mob mobEntity)
					if (mobEntity.getMainHandItem() != null)
				if (target instanceof Player playerEntity) {
					playerEntity.drop(playerEntity.getInventory().getSelected(), false);
					playerEntity.getInventory().setItem(playerEntity.getInventory().selected, ItemStack.EMPTY);
				}
						mobEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
				livingEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
				livingEntity.hurt(damageSources().mobAttack(this), this.getRandom().nextInt(4) > 2 ? Gigeresque.config.stalkerTailAttackDamage : 0.0f);
				this.heal(1.0833f);
				return super.doHurtTarget(target);
			}
		if (target instanceof Creeper creeper)
			creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
		this.heal(1.0833f);
		return super.doHurtTarget(target);
	}

}
