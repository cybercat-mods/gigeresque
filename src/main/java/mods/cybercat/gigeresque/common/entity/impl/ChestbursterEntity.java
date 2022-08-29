package mods.cybercat.gigeresque.common.entity.impl;

import java.util.function.Predicate;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.goal.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goal.busters.EatFoodGoal;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
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
	public static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = item -> {
		ItemStack itemStack = item.getStack();
		return itemStack.isIn(GigTags.BUSTER_FOOD) && item.isAlive() && !item.cannotPickup();
	};
	public int bloodRendering = 0;

	private final AnimationFactory animationFactory = new AnimationFactory(this);
	protected String hostId = null;

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
	public void tick() {
		super.tick();
		if (!world.isClient && this.isAlive()) {
			setBlood(bloodRendering++);
			grow(this, 1 * getGrowthMultiplier());
		}
		if (this.isBirthed() == true && this.age > 1200 && this.getGrowth() > 200) {
			this.setBirthStatus(false);
		}
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
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.6D));
		this.goalSelector.add(5, new FleeFireGoal<ChestbursterEntity>(this));
		this.goalSelector.add(5, new EatFoodGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0));
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
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (velocityLength >= 0.000000001 && !isDead && lastLimbDistance > 0.15F) {
			if (lastLimbDistance >= 0.35F) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_slither", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", true));
				return PlayState.CONTINUE;
			}
		} else if (this.dataTracker.get(EAT) == true && !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", false));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		} else {
			if (this.age < 60 && this.dataTracker.get(BIRTHED) == true) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
				return PlayState.CONTINUE;
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("stepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.BURSTER_CRAWL,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<ChestbursterEntity> controller = new AnimationController<ChestbursterEntity>(this,
				"controller", 10f, this::predicate);
		controller.registerSoundListener(this::soundListener);
		data.addAnimationController(controller);
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
