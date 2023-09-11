package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AlienEggEntity extends AlienEntity implements GeoEntity {

	private static final EntityDataAccessor<Boolean> IS_HATCHING = SynchedEntityData.defineId(AlienEggEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_HATCHED = SynchedEntityData.defineId(AlienEggEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HAS_FACEHUGGER = SynchedEntityData.defineId(AlienEggEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> NEST_TICKS = SynchedEntityData.defineId(AlienEggEntity.class, EntityDataSerializers.FLOAT);
	private long hatchProgress = 0L;
	private long ticksOpen = 0L;
	public float ticksUntilNest = -1.0f;
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private static final long MAX_HATCH_PROGRESS = 50L;

	public AlienEggEntity(EntityType<? extends AlienEggEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	public static boolean canSpawn(EntityType<? extends AlienEntity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
		if (world.getDifficulty() == Difficulty.PEACEFUL)
			return false;
		if ((reason != MobSpawnType.CHUNK_GENERATION && reason != MobSpawnType.NATURAL))
			return !world.getBlockState(pos.below()).is(BlockTags.LOGS);
		return !world.getBlockState(pos.below()).is(BlockTags.LOGS);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.alieneggHealth).add(Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 0.0).add(Attributes.MOVEMENT_SPEED, 0.0);
	}

	public boolean isHatching() {
		return entityData.get(IS_HATCHING);
	}

	public void setIsHatching(boolean value) {
		entityData.set(IS_HATCHING, value);
	}

	public boolean isHatched() {
		return entityData.get(IS_HATCHED);
	}

	public void setIsHatched(boolean value) {
		entityData.set(IS_HATCHED, value);
	}

	public boolean hasFacehugger() {
		return entityData.get(HAS_FACEHUGGER);
	}

	public void setHasFacehugger(boolean value) {
		entityData.set(HAS_FACEHUGGER, value);
	}

	public float getTicksUntilNest() {
		return entityData.get(NEST_TICKS);
	}

	public void setTicksUntilNest(float ticksUntilEggmorphed) {
		this.entityData.set(NEST_TICKS, ticksUntilEggmorphed);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(IS_HATCHING, false);
		entityData.define(IS_HATCHED, false);
		entityData.define(HAS_FACEHUGGER, true);
		entityData.define(NEST_TICKS, -1.0f);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("isHatching", isHatching());
		nbt.putBoolean("isHatched", isHatched());
		nbt.putBoolean("hasFacehugger", hasFacehugger());
		nbt.putLong("hatchProgress", hatchProgress);
		nbt.putLong("ticksOpen", ticksOpen);
		nbt.putFloat("ticksUntilEggmorphed", getTicksUntilNest());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		setIsHatching(nbt.getBoolean("isHatching"));
		setIsHatched(nbt.getBoolean("isHatched"));
		setHasFacehugger(nbt.getBoolean("hasFacehugger"));
		hatchProgress = nbt.getLong("hatchProgress");
		ticksOpen = nbt.getLong("ticksOpen");
		setTicksUntilNest(nbt.getInt("ticksUntilEggmorphed"));
	}

	@Override
	protected AABB makeBoundingBox() {
		return super.makeBoundingBox();
	}

	@Override
	public AABB getLocalBoundsForPose(Pose pose) {
		return this.isHatched() ? this.getBoundingBox().inflate(1.7) : this.getBoundingBox().inflate(1.2);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return (this.isHatched() && !this.isDeadOrDying()) ? EntityDimensions.scalable(0.7f, 1.0f) : this.isDeadOrDying() ? EntityDimensions.scalable(0.7f, 0.6f) : super.getDimensions(pose);
	}

	@Override
	public void refreshDimensions() {
		super.refreshDimensions();
	}

	@Override
	public void travel(Vec3 vec3) {
		if (this.tickCount % 10 == 0)
			this.refreshDimensions();
		super.travel(vec3);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isNoAi())
			return;

		if (this.isHatched() && this.isAlive())
			if (!this.getLevel().isClientSide)
				this.setTicksUntilNest(ticksUntilNest++);
		if (this.getTicksUntilNest() == 6000f) {
			if (this.getLevel().isClientSide)
				for (var i = 0; i < 2; i++)
					this.getLevel().addAlwaysVisibleParticle(Particles.GOO, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 0.0, 0.0, 0.0);
			this.getLevel().setBlockAndUpdate(this.blockPosition(), GIgBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
			this.kill();
		}

		if (isHatching() && hatchProgress < MAX_HATCH_PROGRESS)
			hatchProgress++;

		if (hatchProgress == 40L)
			if (!getLevel().isClientSide)
				this.getCommandSenderWorld().playSound(this, blockPosition(), GigSounds.EGG_OPEN, SoundSource.HOSTILE, 1.0F, 1.0F);

		if (hatchProgress >= MAX_HATCH_PROGRESS) {
			setIsHatching(false);
			setIsHatched(true);
			ticksOpen++;
		}

		if (isHatched() && hasFacehugger())
			ticksOpen++;

		if (ticksOpen >= 3L * Constants.TPS && hasFacehugger() && !getLevel().isClientSide && !this.isDeadOrDying()) {
			var facehugger = Entities.FACEHUGGER.create(getLevel());
			facehugger.setPos(this.position().x, this.position().y + 1, this.position().z);
			facehugger.setDeltaMovement(Mth.nextFloat(facehugger.getRandom(), -0.5f, 0.5f), 0.7, Mth.nextFloat(facehugger.getRandom(), -0.5f, 0.5f));
			facehugger.setEggSpawnState(true);
			getLevel().addFreshEntity(facehugger);
			setHasFacehugger(false);
		}
	}

	/**
	 * Prevents entity collisions from moving the egg.
	 */
	@Override
	public void doPush(Entity entity) {
		if (!getLevel().isClientSide && (entity instanceof LivingEntity living && GigEntityUtils.faceHuggerTest(living, this)))
			setIsHatching(true);
	}

	@Override
	public boolean canBeCollidedWith() {
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
	public boolean isPushedByFluid() {
		return false;
	}

	/**
	 * Prevents the egg from moving on its own.
	 */
	@Override
	public boolean shouldPassengersInheritMalus() {
		return false;
	}

	/**
	 * Prevents the egg moving when hit.
	 */
	@Override
	public void knockback(double strength, double x, double z) {
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source != damageSources().generic())
			if (source.getDirectEntity() != null || source != damageSources().inWall() && !this.isHatched())
				setIsHatching(true);
		return source == damageSources().inWall() ? false : super.hurt(source, amount);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(Gigeresque.config.alieneggHatchRange)).forEach(target -> {
			if (target.isAlive())
				if (GigEntityUtils.faceHuggerTest(target, this))
					if (!(target instanceof Player))
						setIsHatching(true);
					else if (target instanceof Player player && !(player.isCreative() || player.isSpectator()))
						setIsHatching(true);
		});
	}

	/**
	 * Prevents the egg from drowning.
	 */
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean requiresCustomPersistence() {
		return (this.isHatched() && !this.hasFacehugger()) ? false : super.requiresCustomPersistence();
	}

	@Override
	public void checkDespawn() {
		if (this.isHatched() && !this.hasFacehugger())
			super.checkDespawn();
	}

	@Override
	public void onSignalReceive(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4, Entity var5, Entity var6, float var7) {
		if (this.isDeadOrDying())
			return;
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			if (isHatched() && !this.isDeadOrDying()) {
				if (!hasFacehugger())
					return event.setAndContinue(GigAnimationsDefault.HATCHED_EMPTY);
				return event.setAndContinue(GigAnimationsDefault.HATCHED);
			}
			if (this.isDeadOrDying())
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			if (isHatching() && !this.isDeadOrDying())
				event.getController().setAnimation(GigAnimationsDefault.HATCHING);
			return event.setAndContinue(GigAnimationsDefault.IDLE);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("hatching"))
				if (this.getLevel().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.EGG_OPEN, SoundSource.HOSTILE, 0.75F, 0.1F, true);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
