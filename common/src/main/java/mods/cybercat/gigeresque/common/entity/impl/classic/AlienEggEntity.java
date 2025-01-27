package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.NewAlienEntity;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.states.EggStates;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class AlienEggEntity extends NewAlienEntity {

    private static final EntityDataAccessor<Boolean> HAS_FACEHUGGER = SynchedEntityData.defineId(
        AlienEggEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    private static final EntityDataAccessor<Float> NEST_TICKS = SynchedEntityData.defineId(
        AlienEggEntity.class,
        EntityDataSerializers.FLOAT
    );

    private static final EntityDataAccessor<Integer> EGG_STATE = SynchedEntityData.defineId(
        AlienEggEntity.class,
        EntityDataSerializers.INT
    );

    public static final long MAX_HATCH_PROGRESS = 50L;

    public float ticksUntilNest = -1.0f;

    public long hatchProgress = 0L;

    public long ticksOpen = 0L;

    public int hatchCheckTimer = 0;

    public AlienEggEntity(EntityType<? extends AlienEggEntity> type, Level world) {
        super(type, world);
        this.vibrationUser = new AzureVibrationUser(this, 0.0F);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, CommonMod.config.eggConfigs.alieneggHealth)
            .add(
                Attributes.ARMOR,
                1.0
            )
            .add(Attributes.ARMOR_TOUGHNESS, 0.0)
            .add(
                Attributes.KNOCKBACK_RESISTANCE,
                0.0
            )
            .add(Attributes.FOLLOW_RANGE, 0.0)
            .add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public int getAcidDiameter() {
        return 1;
    }

    public void setEggState(int value) {
        entityData.set(EGG_STATE, value);
    }

    public int getEggState() {
        return entityData.get(EGG_STATE);
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
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_FACEHUGGER, true);
        builder.define(NEST_TICKS, -1.0f);
        builder.define(EGG_STATE, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("hasFacehugger", hasFacehugger());
        nbt.putLong("hatchProgress", hatchProgress);
        nbt.putLong("ticksOpen", ticksOpen);
        nbt.putFloat("ticksUntilEggmorphed", getTicksUntilNest());
        nbt.putInt("eggState", getEggState());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setHasFacehugger(nbt.getBoolean("hasFacehugger"));
        hatchProgress = nbt.getLong("hatchProgress");
        ticksOpen = nbt.getLong("ticksOpen");
        setTicksUntilNest(nbt.getInt("ticksUntilEggmorphed"));
        setEggState(nbt.getInt("eggState"));
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if (this.getEggState() == EggStates.HATCHED.ordinal() && !this.isDeadOrDying())
            return EntityDimensions.scalable(0.7f, 1.0f);
        if (this.isDeadOrDying())
            return EntityDimensions.scalable(0.7f, 0.6f);
        return super.getDefaultDimensions(pose);
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource source) {
        return GigSounds.EGG_NOTICE.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected @NotNull SoundEvent getSwimSplashSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.tickCount % 10 == 0)
            this.refreshDimensions();
        super.travel(vec3);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isNoAi())
            return;

        GigCommonMethods.handleNestProgress(this);
        GigCommonMethods.handleHatchingProgress(this);
        GigCommonMethods.handleFacehuggerSpawn(this);

        /*
         * ANIMATIONS
         */
        if (this.level().isClientSide) {
            Runnable animationRunner;

            if (this.isDeadOrDying()) {
                animationRunner = animationDispatcher::sendDeath;
            } else if (this.getEggState() == EggStates.HATCHING.ordinal()) {
                animationRunner = animationDispatcher::sendHatching;
            } else if (this.getEggState() == EggStates.HATCHED.ordinal()) {
                animationRunner = animationDispatcher::sendHatchEmpty;
            } else {
                animationRunner = animationDispatcher::sendIdle;
            }

            animationRunner.run();
        }
    }

    /**
     * Prevents entity collisions from moving the egg.
     */
    @Override
    public void doPush(@NotNull Entity entity) {
        if (!level().isClientSide && (entity instanceof LivingEntity living && GigEntityUtils.faceHuggerTest(living))) {
            this.setEggState(EggStates.HATCHING.ordinal());
        }
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
    public void knockback(double strength, double x, double z) {}

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (
            source != damageSources().genericKill() && source.getDirectEntity() != null && this.getEggState() != EggStates.HATCHED.ordinal()
        ) {
            this.setEggState(EggStates.HATCHING.ordinal());
        }
        return source != damageSources().inWall() && super.hurt(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        hatchCheckTimer++;

        GigCommonMethods.handleAoEEntityHatchCheck(this);
        GigCommonMethods.handleAoEBlockHatchCheck(this);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return (this.getEggState() != EggStates.HATCHED.ordinal() || this.hasFacehugger());
    }

    @Override
    public void checkDespawn() {
        if (this.getEggState() == EggStates.HATCHED.ordinal() && !this.hasFacehugger())
            super.checkDespawn();
    }
}
