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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ChestbursterEntity extends AlienEntity implements IAnimatable, Growable, IAnimationTickable {

	private static final EntityDataAccessor<Float> BLOOD = SynchedEntityData.defineId(ChestbursterEntity.class,
			EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(ChestbursterEntity.class,
			EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> BIRTHED = SynchedEntityData.defineId(ChestbursterEntity.class,
			EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> EAT = SynchedEntityData.defineId(ChestbursterEntity.class,
			EntityDataSerializers.BOOLEAN);
	public static final Predicate<ItemEntity> PICKABLE_DROP_FILTER = item -> {
		ItemStack itemStack = item.getItem();
		return itemStack.is(GigTags.BUSTER_FOOD) && item.isAlive() && !item.hasPickUpDelay();
	};
	public int bloodRendering = 0;

	private AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);
	protected String hostId = null;

	public ChestbursterEntity(EntityType<? extends ChestbursterEntity> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.ARMOR, 2.0)
				.add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
				.add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
				.add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.ATTACK_KNOCKBACK, 0.3);
	}

	public float getBlood() {
		return entityData.get(GROWTH);
	}

	public void setBlood(float growth) {
		entityData.set(GROWTH, growth);
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
		return this.entityData.get(BIRTHED);
	}

	public void setBirthStatus(boolean birth) {
		this.entityData.set(BIRTHED, Boolean.valueOf(birth));
	}

	public boolean isEating() {
		return this.entityData.get(EAT);
	}

	public void setEatingStatus(boolean birth) {
		this.entityData.set(EAT, Boolean.valueOf(birth));
	}

	public float getGrowth() {
		return entityData.get(BLOOD);
	}

	public void setGrowth(float growth) {
		entityData.set(BLOOD, growth);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(GROWTH, 0.0f);
		entityData.define(BLOOD, 0.0f);
		entityData.define(BIRTHED, false);
		entityData.define(EAT, false);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide && this.isAlive()) {
			setBlood(bloodRendering++);
			grow(this, 1 * getGrowthMultiplier());
		}
		if (this.isBirthed() == true && this.tickCount > 1200 && this.getGrowth() > 200) {
			this.setBirthStatus(false);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putFloat("growth", getGrowth());
		if (hostId != null) {
			nbt.putString("hostId", hostId);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("growth")) {
			setGrowth(nbt.getFloat("growth"));
		}
		if (nbt.contains("hostId")) {
			hostId = nbt.getString("hostId");
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(5, new FleeFireGoal<ChestbursterEntity>(this));
		this.goalSelector.addGoal(5, new EatFoodGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
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
		var entity = new RunnerbursterEntity(Entities.RUNNERBURSTER, level);
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
		var velocityLength = this.getDeltaMovement().horizontalDistance();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (velocityLength >= 0.000000001 && !isDead && animationSpeedOld > 0.15F) {
			if (animationSpeedOld >= 0.35F) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_slither", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
		} else if (this.entityData.get(EAT) == true && !this.isDeadOrDying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", EDefaultLoopTypes.PLAY_ONCE));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		} else {
			if (this.tickCount < 60 && this.entityData.get(BIRTHED) == true) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("stepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(),
						GigSounds.BURSTER_CRAWL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
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
		return tickCount;
	}
	
	@Override
	public void onSignalReceive(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4, Entity var5,
			Entity var6, float var7) {
		super.onSignalReceive(var1, var2, var3, var4, var5, var6, var7);
		this.getNavigation().moveTo(var3.getX(), var3.getY(), var3.getZ(), 1.2F);
	}
}
