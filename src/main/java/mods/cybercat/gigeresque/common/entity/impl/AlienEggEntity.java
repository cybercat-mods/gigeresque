package mods.cybercat.gigeresque.common.entity.impl;

import java.util.List;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.common.util.SoundUtil;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AlienEggEntity extends AlienEntity implements IAnimatable, IAnimationTickable {

	private static final TrackedData<Boolean> IS_HATCHING = DataTracker.registerData(AlienEggEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_HATCHED = DataTracker.registerData(AlienEggEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HAS_FACEHUGGER = DataTracker.registerData(AlienEggEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	private long hatchProgress = 0L;
	private long ticksOpen = 0L;
	private final AnimationFactory animationFactory = new AnimationFactory(this);

	private static final long MAX_HATCH_PROGRESS = 50L;

	public AlienEggEntity(EntityType<? extends AlienEggEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
				.add(EntityAttributes.GENERIC_ARMOR, 1.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0);
	}

	public boolean isHatching() {
		return dataTracker.get(IS_HATCHING);
	}

	public void setIsHatching(boolean value) {
		dataTracker.set(IS_HATCHING, value);
	}

	public boolean isHatched() {
		return dataTracker.get(IS_HATCHED);
	}

	public void setIsHatched(boolean value) {
		dataTracker.set(IS_HATCHED, value);
	}

	public boolean hasFacehugger() {
		return dataTracker.get(HAS_FACEHUGGER);
	}

	public void setHasFacehugger(boolean value) {
		dataTracker.set(HAS_FACEHUGGER, value);
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(IS_HATCHING, false);
		dataTracker.startTracking(IS_HATCHED, false);
		dataTracker.startTracking(HAS_FACEHUGGER, true);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("isHatching", isHatching());
		nbt.putBoolean("isHatched", isHatched());
		nbt.putBoolean("hasFacehugger", hasFacehugger());
		nbt.putLong("hatchProgress", hatchProgress);
		nbt.putLong("ticksOpen", ticksOpen);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("isHatching")) {
			setIsHatching(nbt.getBoolean("isHatching"));
		}
		if (nbt.contains("isHatched")) {
			setIsHatched(nbt.getBoolean("isHatched"));
		}
		if (nbt.contains("hasFacehugger")) {
			setHasFacehugger(nbt.getBoolean("hasFacehugger"));
		}
		if (nbt.contains("hatchProgress")) {
			hatchProgress = nbt.getLong("hatchProgress");
		}
		if (nbt.contains("ticksOpen")) {
			ticksOpen = nbt.getLong("ticksOpen");
		}
	}

	@Override
	protected Box calculateBoundingBox() {
		return super.calculateBoundingBox();
	}

	@Override
	public Box getBoundingBox(EntityPose pose) {
		return this.getBoundingBox().expand(1);
	}

	@Override
	public void tick() {
		super.tick();
		if (isHatching() && hatchProgress < MAX_HATCH_PROGRESS) {
			hatchProgress++;
		}

		if (hatchProgress == 15L) {
			SoundUtil.playServerSound(world, null, this.getBlockPos(), GigSounds.EGG_OPEN, SoundCategory.NEUTRAL, 1.0f);
		}

		if (hatchProgress >= MAX_HATCH_PROGRESS) {
			setIsHatching(false);
			setIsHatched(true);
			ticksOpen++;
		}

		if (isHatched() && hasFacehugger()) {
			ticksOpen++;
		}

		if (ticksOpen >= 3L * Constants.TPS && hasFacehugger() && !world.isClient) {
			var facehugger = new FacehuggerEntity(Entities.FACEHUGGER, world);
			facehugger.refreshPositionAndAngles(getBlockPos().up(), getYaw(), getPitch());
			facehugger.setVelocity(0.0, 0.7, 0.0);
			facehugger.setEggSpawnState(true);
			world.spawnEntity(facehugger);
			setHasFacehugger(false);
		}
	}

	/**
	 * Prevents entity collisions from moving the egg.
	 */
	@Override
	public void pushAway(Entity entity) {
		if (!world.isClient && EntityUtils.isPotentialHost(entity)) {
			setIsHatching(true);
		}
		// this.pushAway(entity);
	}

	@Override
	public boolean isCollidable() {
		return this.isAlive();
	}

	/**
	 * Prevents the egg from being pushed.
	 */
	@Override
	public boolean isPushable() {
		return false;
	}

	/**
	 * Prevents fluids from moving the egg.
	 */
	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	/**
	 * Prevents the egg from moving on its own.
	 */
	@Override
	public boolean movesIndependently() {
		return false;
	}

	/**
	 * Prevents the egg moving when hit.
	 */
	@Override
	public void takeKnockback(double strength, double x, double z) {
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (source.getSource() != null || source != DamageSource.IN_WALL && !this.isHatched()) {
			setIsHatching(true);
		}
		return source == DamageSource.IN_WALL ? false : super.damage(source, amount);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		float q = 6.0F;
		int k = MathHelper.floor(this.getX() - (double) q - 1.0D);
		int l = MathHelper.floor(this.getX() + (double) q + 1.0D);
		int t = MathHelper.floor(this.getY() - (double) q - 1.0D);
		int u = MathHelper.floor(this.getY() + (double) q + 1.0D);
		int v = MathHelper.floor(this.getZ() - (double) q - 1.0D);
		int w = MathHelper.floor(this.getZ() + (double) q + 1.0D);
		List<Entity> list = this.world.getOtherEntities(this,
				new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
		Vec3d vec3d1 = new Vec3d(this.getX(), this.getY(), this.getZ());

		for (int x = 0; x < list.size(); ++x) {
			Entity entity = (Entity) list.get(x);
			double y = (double) (MathHelper.sqrt((float) entity.squaredDistanceTo(vec3d1)) / q);
			if (y <= 1.0D && !ConfigAccessor.isTargetBlacklisted(this, entity) && entity.isAlive()) {
				if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)
						&& !(entity instanceof AlienEntity)
						&& !(ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, entity))) {
					if (((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing()
							&& !(entity instanceof AmbientEntity)
							&& ((LivingEntity) entity).getGroup() != EntityGroup.UNDEAD) {
						if (EntityUtils.isPotentialHost(entity))
							setIsHatching(true);
					}
				}
				if (entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative()
						&& !((PlayerEntity) entity).isSpectator()) {
					setIsHatching(true);
				}
			}
		}
	}

	/**
	 * Prevents the egg from drowning.
	 */
	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public boolean cannotDespawn() {
		return (this.isHatched() && !this.hasFacehugger()) ? false : super.cannotDespawn();
	}

	@Override
	public void checkDespawn() {
		if (this.isHatched() && !this.hasFacehugger())
			super.checkDespawn();
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (isHatched() && !this.isDead()) {
			if (!hasFacehugger()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("hatched_empty", true));
				return PlayState.CONTINUE;
			}

			event.getController().setAnimation(new AnimationBuilder().addAnimation("hatched", true));
			return PlayState.CONTINUE;
		}

		if (this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		}

		if (isHatching() && !this.isDead()) {
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("hatch", false).addAnimation("hatched"));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 5f, this::predicate));
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
