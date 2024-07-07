package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AlienEggEntity extends AlienEntity {

    private static final EntityDataAccessor<Boolean> IS_HATCHING = SynchedEntityData.defineId(AlienEggEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HATCHED = SynchedEntityData.defineId(AlienEggEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_FACEHUGGER = SynchedEntityData.defineId(AlienEggEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> NEST_TICKS = SynchedEntityData.defineId(AlienEggEntity.class,
            EntityDataSerializers.FLOAT);
    private static final long MAX_HATCH_PROGRESS = 50L;
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    public float ticksUntilNest = -1.0f;
    private long hatchProgress = 0L;
    private long ticksOpen = 0L;

    public AlienEggEntity(EntityType<? extends AlienEggEntity> type, Level world) {
        super(type, world);
        this.vibrationUser = new AzureVibrationUser(this, 0.0F);
    }

    public static boolean canSpawn(EntityType<? extends AlienEntity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) return false;
        return !world.getBlockState(pos.below()).is(BlockTags.LOGS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, CommonMod.config.alieneggHealth).add(
                Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE,
                0.0).add(Attributes.FOLLOW_RANGE, 0.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public int getAcidDiameter() {
        return 1;
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
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HATCHING, false);
        builder.define(IS_HATCHED, false);
        builder.define(HAS_FACEHUGGER, true);
        builder.define(NEST_TICKS, -1.0f);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("isHatching", isHatching());
        nbt.putBoolean("isHatched", isHatched());
        nbt.putBoolean("hasFacehugger", hasFacehugger());
        nbt.putLong("hatchProgress", hatchProgress);
        nbt.putLong("ticksOpen", ticksOpen);
        nbt.putFloat("ticksUntilEggmorphed", getTicksUntilNest());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setIsHatching(nbt.getBoolean("isHatching"));
        setIsHatched(nbt.getBoolean("isHatched"));
        setHasFacehugger(nbt.getBoolean("hasFacehugger"));
        hatchProgress = nbt.getLong("hatchProgress");
        ticksOpen = nbt.getLong("ticksOpen");
        setTicksUntilNest(nbt.getInt("ticksUntilEggmorphed"));
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.isHatched() && !this.isDeadOrDying()) return EntityDimensions.scalable(0.7f, 1.0f);
        if (this.isDeadOrDying()) return EntityDimensions.scalable(0.7f, 0.6f);
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
        if (this.tickCount % 10 == 0) this.refreshDimensions();
        super.travel(vec3);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isNoAi()) return;

        if (this.isHatched() && this.isAlive() && !this.level().isClientSide) this.setTicksUntilNest(ticksUntilNest++);
        if (this.getTicksUntilNest() == 6000f) {
            if (this.level().isClientSide) {
                for (var i = 0; i < 2; i++)
                    this.level().addAlwaysVisibleParticle(GigParticles.GOO.get(), this.getRandomX(1.0), this.getRandomY(),
                            this.getRandomZ(1.0), 0.0, 0.0, 0.0);
            }
            this.level().setBlockAndUpdate(this.blockPosition(), GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
            this.kill();
        }

        if (isHatching() && hatchProgress < MAX_HATCH_PROGRESS) hatchProgress++;

        if (hatchProgress == 40L && !level().isClientSide)
            this.level().playSound(this, blockPosition(), GigSounds.EGG_OPEN.get(), SoundSource.HOSTILE, 1.0F, 1.0F);

        if (hatchProgress >= MAX_HATCH_PROGRESS) {
            setIsHatching(false);
            setIsHatched(true);
            ticksOpen++;
        }

        if (isHatched() && hasFacehugger()) ticksOpen++;

        if (ticksOpen >= 3L * Constants.TPS && hasFacehugger() && !level().isClientSide && !this.isDeadOrDying()) {
            var facehugger = GigEntities.FACEHUGGER.get().create(level());
            assert facehugger != null;
            facehugger.setPos(this.position().x, this.position().y + 1, this.position().z);
            facehugger.setDeltaMovement(Mth.nextFloat(facehugger.getRandom(), -0.5f, 0.5f), 0.7,
                    Mth.nextFloat(facehugger.getRandom(), -0.5f, 0.5f));
            facehugger.setEggSpawnState(true);
            facehugger.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 30, false, false));
            level().addFreshEntity(facehugger);
            setHasFacehugger(false);
        }
    }

    /**
     * Prevents entity collisions from moving the egg.
     */
    @Override
    public void doPush(@NotNull Entity entity) {
        if (!level().isClientSide && (entity instanceof LivingEntity living && GigEntityUtils.faceHuggerTest(living))) {
            setIsHatching(true);
        }
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
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source != damageSources().genericKill() && source.getDirectEntity() != null || source != damageSources().inWall() && !this.isHatched())
            setIsHatching(true);
        return source != damageSources().inWall() && super.hurt(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(CommonMod.config.alieneggHatchRange)).forEach(target -> {
            if (target.isAlive() && GigEntityUtils.faceHuggerTest(target)) {
                if (target instanceof Player player && !player.isSteppingCarefully() && !(player.isCreative() || player.isSpectator())) {
                    setIsHatching(true);
                }
                if (!(target instanceof Player)) {
                    setIsHatching(true);
                }
            }
        });
        this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3)).forEach(target -> {
            if (target.isAlive() && GigEntityUtils.faceHuggerTest(target)) {
                if (target instanceof Player player && !(player.isCreative() || player.isSpectator())) {
                    setIsHatching(true);
                }
                if (!(target instanceof Player)) {
                    setIsHatching(true);
                }
            }
        });
        // Loop through nearby blocks in different directions
        for (var testPos : BlockPos.betweenClosed(this.blockPosition().above(1), this.blockPosition().above(1))) {
            for (var testPos1 : BlockPos.betweenClosed(this.blockPosition().below(1), this.blockPosition().below(1))) {
                for (var testPos2 : BlockPos.betweenClosed(this.blockPosition().east(1),
                        this.blockPosition().east(1))) {
                    for (var testPos3 : BlockPos.betweenClosed(this.blockPosition().west(1),
                            this.blockPosition().west(1))) {
                        for (var testPos4 : BlockPos.betweenClosed(this.blockPosition().south(1),
                                this.blockPosition().south(1))) {
                            for (var testPos5 : BlockPos.betweenClosed(this.blockPosition().north(1),
                                    this.blockPosition().north(1))) {
                                // Check if any of the nearby blocks are not air
                                boolean isAnyBlockNotAir = !this.level().getBlockState(
                                        testPos).isAir() && !this.level().getBlockState(
                                        testPos1).isAir() && !this.level().getBlockState(
                                        testPos2).isAir() && !this.level().getBlockState(
                                        testPos3).isAir() && !this.level().getBlockState(
                                        testPos4).isAir() && !this.level().getBlockState(testPos5).isAir();

                                // Check if any of the nearby blocks are not solid
                                boolean isAnyBlockSolid = !this.level().getBlockState(
                                        testPos).isSolid() && !this.level().getBlockState(
                                        testPos1).isSolid() && !this.level().getBlockState(
                                        testPos2).isSolid() && !this.level().getBlockState(
                                        testPos3).isSolid() && !this.level().getBlockState(
                                        testPos4).isSolid() && !this.level().getBlockState(testPos5).isSolid();

                                // Set isHatching to false if conditions are met
                                if (isAnyBlockSolid || isAnyBlockNotAir) {
                                    setIsHatching(false);
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
        return (!this.isHatched() || this.hasFacehugger());
    }

    @Override
    public void checkDespawn() {
        if (this.isHatched() && !this.hasFacehugger()) super.checkDespawn();
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            if (isHatched() && !this.isDeadOrDying()) {
                if (!hasFacehugger()) return event.setAndContinue(GigAnimationsDefault.HATCHED_EMPTY);
                return event.setAndContinue(GigAnimationsDefault.HATCHED);
            }
            if (this.isDeadOrDying()) return event.setAndContinue(GigAnimationsDefault.DEATH);
            if (isHatching() && !this.isDeadOrDying())
                event.getController().setAnimation(GigAnimationsDefault.HATCHING);
            return event.setAndContinue(GigAnimationsDefault.IDLE);
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("hatching") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.EGG_OPEN.get(),
                        SoundSource.HOSTILE, 0.75F, 0.1F, true);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
