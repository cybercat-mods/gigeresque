package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;

import com.mojang.serialization.Dynamic;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.Growable;
import mods.cybercat.gigeresque.common.entity.ai.brain.ChestbursterBrain;
import mods.cybercat.gigeresque.common.entity.ai.brain.sensor.SensorTypes;
import mods.cybercat.gigeresque.common.entity.ai.goal.FleeFireGoal;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ChestbursterEntity extends AlienEntity implements IAnimatable, Growable, IAnimationTickable {

	private static final TrackedData<Float> BLOOD = DataTracker.registerData(ChestbursterEntity.class,
			TrackedDataHandlerRegistry.FLOAT);

	private static final TrackedData<Float> GROWTH = DataTracker.registerData(ChestbursterEntity.class,
			TrackedDataHandlerRegistry.FLOAT);

	public static final TrackedData<Boolean> BIRTHED = DataTracker.registerData(ChestbursterEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	public static final TrackedData<Boolean> EAT = DataTracker.registerData(ChestbursterEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	private final AnimationFactory animationFactory = new AnimationFactory(this);
	protected String hostId = null;
	private ChestbursterBrain complexBrain;

	private static final List<SensorType<? extends Sensor<? super ChestbursterEntity>>> SENSOR_TYPES = List.of(
			SensorType.NEAREST_LIVING_ENTITIES, SensorTypes.ALIEN_REPELLENT, SensorTypes.NEAREST_FOOD_ITEM,
			SensorTypes.NEAREST_LARGER_THREAT, SensorTypes.NEAREST_SMALLER_TARGET);

	private static final List<MemoryModuleType<?>> MEMORY_MODULE_TYPES = List.of(MemoryModuleType.ATTACK_TARGET,
			MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.AVOID_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LOOK_TARGET, MemoryModuleType.MOBS,
			MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
			MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PATH, MemoryModuleType.VISIBLE_MOBS,
			MemoryModuleType.WALK_TARGET);

	public ChestbursterEntity(EntityType<? extends ChestbursterEntity> type, World world) {
		super(type, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
				.add(EntityAttributes.GENERIC_ARMOR, 2.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3300000041723251)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3);
	}

	public float getBlood() {
		return dataTracker.get(GROWTH);
	}

	public void setBlood(float growth) {
		dataTracker.set(GROWTH, growth);
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public boolean isBirthed() {
		return this.dataTracker.get(BIRTHED);
	}

	public void setBirthStatus(boolean birth) {
		this.dataTracker.set(BIRTHED, Boolean.valueOf(birth));
	}

	public boolean isEating() {
		return this.dataTracker.get(EAT);
	}

	public void setEatingStatus(boolean birth) {
		this.dataTracker.set(EAT, Boolean.valueOf(birth));
	}

	public float getGrowth() {
		return dataTracker.get(BLOOD);
	}

	public void setGrowth(float growth) {
		dataTracker.set(BLOOD, growth);
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(GROWTH, 0.0f);
		dataTracker.startTracking(BLOOD, 0.0f);
		dataTracker.startTracking(BIRTHED, false);
		dataTracker.startTracking(EAT, false);
	}

	@Override
	protected Brain.Profile<? extends ChestbursterEntity> createBrainProfile() {
		return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
	}

	@Override
	protected Brain<? extends ChestbursterEntity> deserializeBrain(Dynamic<?> dynamic) {
		complexBrain = new ChestbursterBrain(this);
		return complexBrain.initialize(createBrainProfile().deserialize(dynamic));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Brain<ChestbursterEntity> getBrain() {
		return (Brain<ChestbursterEntity>) super.getBrain();
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && this.isAlive()) {
			grow(this, 1 * getGrowthMultiplier());
		}
	}

	@Override
	protected void mobTick() {
		world.getProfiler().push("chestbursterBrain");
		complexBrain.tick();
		world.getProfiler().pop();
		complexBrain.tickActivities();
		super.mobTick();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putFloat("growth", getGrowth());
		if (hostId != null) {
			nbt.putString("hostId", hostId);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("growth")) {
			setGrowth(nbt.getFloat("growth"));
		}
		if (nbt.contains("hostId")) {
			hostId = nbt.getString("hostId");
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(5, new FleeFireGoal<ChestbursterEntity>(this));
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.35, false));
		this.targetSelector.add(2,
				new ActiveTargetGoal<>(this, LivingEntity.class, true,
						entity -> !((entity instanceof AlienEntity) || (entity instanceof WardenEntity)
								|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding()
								|| ((Eggmorphable) entity).isEggmorphing() || (EntityUtils.isFacehuggerAttached(entity))
								|| (entity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS))));
	}

	/*
	 * GROWTH
	 */

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.chestbursterGrowthMultiplier;
	}

	@Override
	public float getMaxGrowth() {
		return Constants.TPD / 2.0f;
	}

	@Override
	public LivingEntity growInto() {
		var entity = new RunnerbursterEntity(Entities.RUNNERBURSTER, world);
		entity.hostId = this.hostId;

		if (hasCustomName()) {
			entity.setCustomName(this.getCustomName());
		}

		return entity;
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();

		if (velocityLength > 0.0 && !this.isAttacking()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", true));
			return PlayState.CONTINUE;
		}
		if (velocityLength > 0.0 && this.isAttacking()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_slither", true));
			return PlayState.CONTINUE;
		}
		if (this.getTarget() != null && this.tryAttack(getTarget())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", false));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(EAT) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", false));
			return PlayState.CONTINUE;
		}
		if (this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		}
		if (this.age < 5 && this.dataTracker.get(BIRTHED) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}

	@Override
	public int tickTimer() {
		return age;
	}
}
