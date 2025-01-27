package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.states.EggStates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.NewAlienEntity;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
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

    public static boolean canSpawn(
        EntityType<? extends AlienEntity> type,
        ServerLevelAccessor world,
        MobSpawnType reason,
        BlockPos pos,
        RandomSource random
    ) {
        if (world.getDifficulty() == Difficulty.PEACEFUL)
            return false;
        return !world.getBlockState(pos.below()).is(BlockTags.LOGS);
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

        if (this.getEggState() == EggStates.HATCHED.ordinal() && this.isAlive() && !this.level().isClientSide)
            this.setTicksUntilNest(ticksUntilNest++);
        if (this.getTicksUntilNest() == 6000f) {
            if (this.level().isClientSide) {
                for (var i = 0; i < 2; i++)
                    this.level()
                        .addAlwaysVisibleParticle(
                            GigParticles.GOO.get(),
                            this.getRandomX(1.0),
                            this.getRandomY(),
                            this.getRandomZ(1.0),
                            0.0,
                            0.0,
                            0.0
                        );
            }
            this.level().setBlockAndUpdate(this.blockPosition(), GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
            this.kill();
        }

        GigCommonMethods.handleHatchingProgress(this);

        if (ticksOpen >= 3L * Constants.TPS && hasFacehugger() && !level().isClientSide && !this.isDeadOrDying()) {
            GigCommonMethods.handleFacehuggerSpawn(this);
        }

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
        if (source != damageSources().genericKill() && source.getDirectEntity() != null && this.getEggState() != EggStates.HATCHED.ordinal()) {
            this.setEggState(EggStates.HATCHING.ordinal());
        }
        return source != damageSources().inWall() && super.hurt(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        // Increment the hatch check timer
        hatchCheckTimer++;

        if (this.getLastHurtMob() != null) {
            this.setEggState(EggStates.HATCHING.ordinal());
        }

        // Perform hatching check once every second (20 ticks)
        if (hatchCheckTimer >= 20) {
            hatchCheckTimer = 0; // Reset the timer

            // Get nearby entities within normal hatch range
            this.level()
                .getEntitiesOfClass(
                    LivingEntity.class,
                    this.getBoundingBox().inflate(CommonMod.config.eggConfigs.alieneggHatchRange)
                )
                .forEach(target -> {
                    // If the entity is alive and can be facehugged
                    if (target.isAlive() && GigEntityUtils.faceHuggerTest(target)) {
                        // Apply random chance to hatch
                        if (this.level().random.nextFloat() < 0.2f) { // 20% chance to hatch every second
                            if (!target.isSteppingCarefully() && Constants.isNotCreativeSpecPlayer.test(target)) {
                                this.setEggState(EggStates.HATCHING.ordinal());
                            }
                        }
                    }
                });

            // Smaller range for closer entities
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3)).forEach(target -> {
                if (
                    target.isAlive() && GigEntityUtils.faceHuggerTest(target) && this.level().random.nextFloat() < 0.8f
                        && (target instanceof Player player && !(player.isCreative() || player.isSpectator())
                            || !(target instanceof Player))
                ) {
                    this.setEggState(EggStates.HATCHING.ordinal());
                }
            });
        }

        if (this.getLastHurtMob() == null)
            // Loop through nearby blocks in different directions (this logic remains the same)
            for (var testPos : BlockPos.betweenClosed(this.blockPosition().above(1), this.blockPosition().above(1))) {
                for (var testPos1 : BlockPos.betweenClosed(this.blockPosition().below(1), this.blockPosition().below(1))) {
                    for (var testPos2 : BlockPos.betweenClosed(this.blockPosition().east(1), this.blockPosition().east(1))) {
                        for (var testPos3 : BlockPos.betweenClosed(this.blockPosition().west(1), this.blockPosition().west(1))) {
                            for (var testPos4 : BlockPos.betweenClosed(this.blockPosition().south(1), this.blockPosition().south(1))) {
                                for (var testPos5 : BlockPos.betweenClosed(this.blockPosition().north(1), this.blockPosition().north(1))) {
                                    // Check if any nearby blocks are not air
                                    boolean isAnyBlockNotAir = !this.level().getBlockState(testPos).isAir() &&
                                        !this.level().getBlockState(testPos1).isAir() &&
                                        !this.level().getBlockState(testPos2).isAir() &&
                                        !this.level().getBlockState(testPos3).isAir() &&
                                        !this.level().getBlockState(testPos4).isAir() &&
                                        !this.level().getBlockState(testPos5).isAir();

                                    // Check if any nearby blocks are solid
                                    boolean isAnyBlockSolid = !this.level()
                                        .getBlockState(testPos)
                                        .isCollisionShapeFullBlock(level(), testPos) &&
                                        !this.level().getBlockState(testPos1).isCollisionShapeFullBlock(level(), testPos1) &&
                                        !this.level().getBlockState(testPos2).isCollisionShapeFullBlock(level(), testPos2) &&
                                        !this.level().getBlockState(testPos3).isCollisionShapeFullBlock(level(), testPos3) &&
                                        !this.level().getBlockState(testPos4).isCollisionShapeFullBlock(level(), testPos4) &&
                                        !this.level().getBlockState(testPos5).isCollisionShapeFullBlock(level(), testPos5);

                                    // Set isHatching to false if conditions are met
                                    if (isAnyBlockSolid || isAnyBlockNotAir) {
                                        this.setEggState(EggStates.IDLE.ordinal());
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
