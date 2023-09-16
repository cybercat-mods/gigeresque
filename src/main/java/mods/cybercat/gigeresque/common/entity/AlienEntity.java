package mods.cybercat.gigeresque.common.entity;

import java.util.Collections;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;

import mod.azure.azurelib.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.helper.AzureTicker;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class AlienEntity extends Monster implements VibrationSystem, GeoEntity {

	public static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.defineId(AlienEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> FLEEING_FIRE = SynchedEntityData.defineId(AlienEntity.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(AlienEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(AlienEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(AlienEntity.class, EntityDataSerializers.BOOLEAN);
	public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS);
	private static final Logger LOGGER = LogUtils.getLogger();
	protected AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
	private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
	protected VibrationSystem.User vibrationUser;
	private VibrationSystem.Data vibrationData;
	public int attackstatetimer = 0;
	protected int slowticks = 0;

	protected AlienEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
		if (navigation != null)
			navigation.setCanFloat(true);
		this.vibrationUser = new AzureVibrationUser(this, 0.0F);
		this.vibrationData = new VibrationSystem.Data();
		this.dynamicGameEventListener = new DynamicGameEventListener<VibrationSystem.Listener>(new VibrationSystem.Listener(this));
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		this.triggerAnim("livingController", "death");
		this.triggerAnim("attackController", "death");
		if (this.deathTime == 150) {
			this.remove(Entity.RemovalReason.KILLED);
			super.tickDeath();
			this.dropExperience();
		}
	}

	protected int getAcidDiameter() {
		return 3;
	}

	public boolean isFleeing() {
		return this.entityData.get(FLEEING_FIRE);
	}

	public void setFleeingStatus(boolean fleeing) {
		this.entityData.set(FLEEING_FIRE, Boolean.valueOf(fleeing));
	}

	public boolean isUpsideDown() {
		return this.entityData.get(UPSIDE_DOWN);
	}

	public void setUpsideDown(boolean upsideDown) {
		this.entityData.set(UPSIDE_DOWN, Boolean.valueOf(upsideDown));
	}

	public int getAttckingState() {
		return this.entityData.get(STATE);
	}

	public void setAttackingState(int time) {
		this.entityData.set(STATE, time);
	}

	public boolean isCrawling() {
		return entityData.get(IS_CLIMBING);
	}

	public void setIsCrawling(boolean isHissing) {
		entityData.set(IS_CLIMBING, isHissing);
	}

	public void increaseAngerAt(@Nullable Entity entity) {
		this.increaseAngerAt(entity, 35, true);
	}

	@VisibleForTesting
	public void increaseAngerAt(@Nullable Entity entity, int i, boolean bl) {
		if (!this.isNoAi() && this.canTargetEntity(entity)) {
			boolean bl2 = !(this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null) instanceof Player);
			int j = this.angerManagement.increaseAnger(entity, i);
			if (entity instanceof Player && bl2 && AngerLevel.byAnger(j).isAngry())
				this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
		}
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(UPSIDE_DOWN, false);
		this.entityData.define(FLEEING_FIRE, false);
		this.entityData.define(IS_CLIMBING, false);
		this.entityData.define(STATE, 0);
		this.entityData.define(CLIENT_ANGER_LEVEL, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("isCrawling", isCrawling());
		VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(LOGGER::error).ifPresent(tag -> compound.put("listener", (Tag) tag));
		AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(LOGGER::error).ifPresent(tag -> compound.put("anger", (Tag) tag));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("isCrawling"))
			setIsCrawling(compound.getBoolean("isCrawling"));
		if (compound.contains("anger")) {
			AngerManagement.codec(this::canTargetEntity).parse(new Dynamic<Tag>(NbtOps.INSTANCE, compound.get("anger"))).resultOrPartial(LOGGER::error).ifPresent(angerManagement -> {
				this.angerManagement = angerManagement;
			});
			this.syncClientAngerLevel();
		}
		if (compound.contains("listener", 10))
			VibrationSystem.Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent(data -> {
				this.vibrationData = data;
			});
	}

	public int getClientAngerLevel() {
		return this.entityData.get(CLIENT_ANGER_LEVEL);
	}

	protected void syncClientAngerLevel() {
		this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
	}

	public AngerLevel getAngerLevel() {
		return AngerLevel.byAnger(this.getActiveAnger());
	}

	private int getActiveAnger() {
		return this.angerManagement.getActiveAnger(this.getTarget());
	}

	public void clearAnger(Entity entity) {
		this.angerManagement.clearAnger(entity);
	}

	@VisibleForTesting
	public AngerManagement getAngerManagement() {
		return this.angerManagement;
	}

	public Optional<LivingEntity> getEntityAngryAt() {
		if (this.getAngerLevel().isAngry())
			return this.angerManagement.getActiveEntity();
		return Optional.empty();
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	protected void customServerAiStep() {
		var serverLevel = (ServerLevel) this.level();
		super.customServerAiStep();
		if (this.tickCount % 20 == 0) {
			this.angerManagement.tick(serverLevel, this::canTargetEntity);
			this.syncClientAngerLevel();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide)
			slowticks++;
		if (this.slowticks > 10 && !this.isCrawling() && this.getNavigation().isDone() && !this.isAggressive() && !((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8))) {
			this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 100, false, false));
			slowticks = -60;
		}
		if (this.level() instanceof ServerLevel serverLevel)
			AzureTicker.tick(serverLevel, this.vibrationData, this.vibrationUser);
		if (!level().isClientSide && this.tickCount % Constants.TPS == 0)
			this.level().getBlockStates(this.getBoundingBox().inflate(3)).forEach(e -> {
				if (e.is(GigTags.NEST_BLOCKS))
					this.heal(0.5833f);
			});
	}

	@Override
	public boolean requiresCustomPersistence() {
		return true;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void checkDespawn() {
	}

	public void generateAcidPool(int xOffset, int zOffset) {
		var pos = this.blockPosition().offset(xOffset, 0, zOffset);
		var posState = level().getBlockState(pos);
		var newState = GigBlocks.ACID_BLOCK.defaultBlockState();

		if (posState.getBlock() == Blocks.WATER)
			newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);

		if (!(posState.getBlock() instanceof AirBlock) && !(posState.getBlock() instanceof LiquidBlock && !(posState.is(GigTags.ACID_RESISTANT))) && !(posState.getBlock() instanceof TorchBlock))
			return;
		level().setBlockAndUpdate(pos, newState);
	}

	@Override
	public void die(DamageSource source) {
		if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources())) {
			super.die(source);
			return;
		}
		if (source == damageSources().genericKill()) {
			super.die(source);
			return;
		}

		if (!this.level().isClientSide) {
			if (source != damageSources().genericKill() || source != damageSources().generic()) {
				if (getAcidDiameter() == 1)
					generateAcidPool(0, 0);
				else {
					var radius = (getAcidDiameter() - 1) / 2;
					for (int x = -radius; x <= radius; x++) {
						for (int z = -radius; z <= radius; z++)
							generateAcidPool(x, z);
					}
				}
			}
		}
		super.die(source);
	}

	@Override
	public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
		if (this.level() instanceof ServerLevel serverLevel)
			biConsumer.accept(this.dynamicGameEventListener, serverLevel);
	}

	@Override
	public VibrationSystem.Data getVibrationData() {
		return this.vibrationData;
	}

	@Override
	public VibrationSystem.User getVibrationUser() {
		return this.vibrationUser;
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	@Contract(value = "null->false")
	public boolean canTargetEntity(@Nullable Entity entity) {
		if (!(entity instanceof LivingEntity))
			return false;
		var livingEntity = (LivingEntity) entity;
		if (this.level() != entity.level())
			return false;
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity))
			return false;
		if (((Host) livingEntity).hasParasite())
			return false;
		if (this.isVehicle())
			return false;
		if (this.isAlliedTo(entity))
			return false;
		if (livingEntity.getMobType() == MobType.UNDEAD)
			return false;
		if (livingEntity.getFeetBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS)
			return false;
		if (livingEntity.getType() == EntityType.ARMOR_STAND)
			return false;
		if (livingEntity.getType() == EntityType.WARDEN)
			return false;
		if (livingEntity instanceof Bat)
			return false;
		if (entity instanceof Marker)
			return false;
		if (entity instanceof AreaEffectCloud)
			return false;
		if (GigEntityUtils.isFacehuggerAttached(livingEntity))
			return false;
		if (livingEntity.isInvulnerable())
			return false;
		if (livingEntity.isDeadOrDying())
			return false;
		if (!this.level().getWorldBorder().isWithinBounds(livingEntity.getBoundingBox()))
			return false;
		var list2 = livingEntity.level().getBlockStatesIfLoaded(livingEntity.getBoundingBox().inflate(2.0, 2.0, 2.0));
		if (list2.anyMatch(NEST))
			return false;
		if (livingEntity.getVehicle() != null && livingEntity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance))
			return false;
		if (livingEntity instanceof AlienEntity)
			return false;
		if (this.isAggressive())
			return false;
		return true;
	}
}
