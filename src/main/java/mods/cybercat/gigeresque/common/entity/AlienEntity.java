package mods.cybercat.gigeresque.common.entity;

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
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.item.ItemStack;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class AlienEntity extends Monster implements VibrationSystem, GeoEntity {

    public static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FLEEING_FIRE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> PASSED_OUT = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final Predicate<BlockState> NEST = state -> state.is(GigBlocks.NEST_RESIN_WEB_CROSS);
    protected static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(
            AlienEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
    protected AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    protected VibrationSystem.User vibrationUser;
    protected int slowticks = 0;
    private VibrationSystem.Data vibrationData;
    public static final EntityDataAccessor<Boolean> CRAWLING_ACCESSOR = SynchedEntityData.defineId(AlienEntity.class,
            EntityDataSerializers.BOOLEAN);

    protected AlienEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
        setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
        this.vibrationUser = new AzureVibrationUser(this, 0.0F);
        this.vibrationData = new VibrationSystem.Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
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
        this.entityData.set(FLEEING_FIRE, fleeing);
    }

    public boolean isUpsideDown() {
        return this.entityData.get(UPSIDE_DOWN);
    }

    public int getAttckingState() {
        return this.entityData.get(STATE);
    }

    public void setAttackingState(int time) {
        this.entityData.set(STATE, time);
    }

    public boolean isCrawling() {
        return this.entityData.get(IS_CLIMBING);
    }

    public void setIsCrawling(boolean shouldCrawl) {
        this.getEntityData().set(IS_CLIMBING, shouldCrawl);
        this.refreshDimensions();
    }

    public boolean isPassedOut() {
        return this.entityData.get(PASSED_OUT);
    }

    public void setPassedOutStatus(boolean passout) {
        this.entityData.set(PASSED_OUT, passout);
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
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isCrawling", isCrawling());
        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("listener", tag));
        AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("anger", tag));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("isCrawling")) setIsCrawling(compound.getBoolean("isCrawling"));
        if (compound.contains("anger")) {
            AngerManagement.codec(this::canTargetEntity).parse(
                    new Dynamic<>(NbtOps.INSTANCE, compound.get("anger"))).resultOrPartial(LOGGER::error).ifPresent(
                    angerM -> this.angerManagement = angerM);
            this.syncClientAngerLevel();
        }
        if (compound.contains("listener", 10))
            VibrationSystem.Data.CODEC.parse(
                    new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(
                    LOGGER::error).ifPresent(data -> this.vibrationData = data);
    }

    protected void syncClientAngerLevel() {
        this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
    }

    private int getActiveAnger() {
        return this.angerManagement.getActiveAnger(this.getTarget());
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
        if (!this.level().isClientSide) slowticks++;
        if (this.slowticks > 10 && !this.isCrawling() && this.getNavigation().isDone() && !this.isAggressive() && !(this.level().getFluidState(
                this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 100, false, false));
            slowticks = -60;
        }
        if (this.level() instanceof ServerLevel serverLevel)
            AzureTicker.tick(serverLevel, this.vibrationData, this.vibrationUser);
        if (!level().isClientSide && this.tickCount % Constants.TPS == 0)
            this.level().getBlockStates(this.getBoundingBox().inflate(3)).forEach(e -> {
                if (e.is(GigTags.NEST_BLOCKS)) this.heal(0.5833f);
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

        if (posState.getBlock() == Blocks.WATER) newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);

        if (!(posState.getBlock() instanceof AirBlock) && !(posState.getBlock() instanceof LiquidBlock && !(posState.is(
                GigTags.ACID_RESISTANT))) && !(posState.getBlock() instanceof TorchBlock))
            return;
        level().setBlockAndUpdate(pos, newState);
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (DamageSourceUtils.isDamageSourceNotPuncturing(source,
                this.damageSources()) || source == damageSources().genericKill()) {
            super.die(source);
            return;
        }

        var damageCheck = !this.level().isClientSide && source != damageSources().genericKill() || source != damageSources().generic();
        if (damageCheck) {
            if (getAcidDiameter() == 1) generateAcidPool(0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (var x = -radius; x <= radius; x++) {
                    for (var z = -radius; z <= radius; z++)
                        if (source != damageSources().genericKill() || source != damageSources().generic())
                            generateAcidPool(x, z);
                }
            }
        }
        super.die(source);
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        if (this.level() instanceof ServerLevel serverLevel)
            biConsumer.accept(this.dynamicGameEventListener, serverLevel);
    }

    @Override
    public VibrationSystem.@NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.@NotNull User getVibrationUser() {
        return this.vibrationUser;
    }

    /*
     * Enabled force condition propagation Lifted jumps to return sites
     */
    @Contract(value = "null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        if (this.level() != entity.level()) return false;
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) return false;
        if (((Host) livingEntity).hasParasite()) return false;
        if (this.isVehicle()) return false;
        if (this.isAlliedTo(entity)) return false;
        if (livingEntity.getMobType() == MobType.UNDEAD) return false;
        if (livingEntity.getFeetBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS) return false;
        if (livingEntity.getType() == EntityType.ARMOR_STAND) return false;
        if (livingEntity.getType() == EntityType.WARDEN) return false;
        if (livingEntity instanceof Bat) return false;
        if (GigEntityUtils.isFacehuggerAttached(livingEntity)) return false;
        if (livingEntity.isInvulnerable()) return false;
        if (livingEntity.isDeadOrDying()) return false;
        if (!this.level().getWorldBorder().isWithinBounds(livingEntity.getBoundingBox())) return false;
        var list2 = livingEntity.level().getBlockStatesIfLoaded(livingEntity.getBoundingBox().inflate(2.0, 2.0, 2.0));
        if (list2.anyMatch(NEST)) return false;
        if (livingEntity.getVehicle() != null && livingEntity.getVehicle().getSelfAndPassengers().anyMatch(
                AlienEntity.class::isInstance))
            return false;
        if (livingEntity instanceof AlienEntity) return false;
        if (this.isAggressive()) return false;
        return this.level().getBlockState(this.blockPosition().below()).isSolid();
    }

    public void drop(LivingEntity target, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;

        var d = target.getEyeY() - 0.3f;
        var itemEntity = new ItemEntity(target.level(), target.getX(), d, target.getZ(), itemStack);
        itemEntity.setPickUpDelay(40);
        float g = Mth.sin(this.getXRot() * ((float) Math.PI / 180));
        float h = Mth.cos(this.getXRot() * ((float) Math.PI / 180));
        float i = Mth.sin(this.getYRot() * ((float) Math.PI / 180));
        float j = Mth.cos(this.getYRot() * ((float) Math.PI / 180));
        float k = this.random.nextFloat() * ((float) Math.PI * 2);
        float l = 0.02f * this.random.nextFloat();
        itemEntity.setDeltaMovement((-i * h * 0.3f) + Math.cos(k) * l, -g * 0.3f + 0.1f * 0.1f,
                (j * h * 0.3f) + Math.sin(k) * l);
        target.level().addFreshEntity(itemEntity);
    }

    protected Vec3i getDirectionVector() {
        return new Vec3i(getDirection().getStepX(), getDirection().getStepY(), getDirection().getStepZ());
    }

}
