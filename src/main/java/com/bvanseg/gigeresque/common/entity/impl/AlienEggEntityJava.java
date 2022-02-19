package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.EntitiesJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.AlienEggBrainJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypesJava;
import com.bvanseg.gigeresque.common.sound.SoundsJava;
import com.bvanseg.gigeresque.common.util.EntityUtils;
import com.bvanseg.gigeresque.common.util.SoundUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class AlienEggEntityJava extends AlienEntityJava implements IAnimatable {
    public AlienEggEntityJava(EntityType<? extends AlienEggEntityJava> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ARMOR, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0);
    }

    private static final TrackedData<Boolean> IS_HATCHING = DataTracker.registerData(AlienEggEntityJava.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_HATCHED = DataTracker.registerData(AlienEggEntityJava.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_FACEHUGGER = DataTracker.registerData(AlienEggEntityJava.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final long MAX_HATCH_PROGRESS = 50L;

    private static final List<SensorType<? extends Sensor<? super LivingEntity>>> SENSOR_TYPES = List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorTypesJava.NEAREST_EGGS,
            SensorTypesJava.NEAREST_FACEHUGGER,
            SensorTypesJava.NEAREST_HOSTS
    );

    private static final List<MemoryModuleType<?>> MEMORY_MODULE_TYPES = List.of(
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.MOBS,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleTypesJava.NEAREST_FACEHUGGERS,
            MemoryModuleTypesJava.NEAREST_EGGS
    );

    @Override
    protected int getAcidDiameter() {
        return 1;
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

    private long hatchProgress = 0L;
    private long ticksOpen = 0L;

    private final AnimationFactory animationFactory = new AnimationFactory(this);

    private AlienEggBrainJava complexBrain;

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return this.isHatched() && !this.hasFacehugger();
    }

    @Override
    public boolean cannotDespawn() {
        return !this.isHatched() && this.hasFacehugger();
    }

    @Override
    protected Brain.Profile<? extends AlienEggEntityJava> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<? extends AlienEggEntityJava> deserializeBrain(Dynamic<?> dynamic) {
        complexBrain = new AlienEggBrainJava(this);
        return complexBrain.initialize(createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<AlienEggEntityJava> getBrain() {
        return (Brain<AlienEggEntityJava>) super.getBrain();
    }

    @Override
    protected void mobTick() {
        world.getProfiler().push("alienEggBrain");
        complexBrain.tick();
        world.getProfiler().pop();
        complexBrain.tickActivities();
        super.mobTick();
    }

    @Override
    protected void initDataTracker() {
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
    public void tick() {
        super.tick();

        if (isHatching() && hatchProgress < MAX_HATCH_PROGRESS) {
            hatchProgress++;
        }

        if (hatchProgress == 15L) {
            SoundUtil.playServerSound(world, null, this.getBlockPos(), SoundsJava.EGG_OPEN, SoundCategory.NEUTRAL, 1.0f);
        }

        if (hatchProgress >= MAX_HATCH_PROGRESS) {
            setIsHatching(false);
            setIsHatched(true);
        }

        if (isHatched() && hasFacehugger()) {
            ticksOpen++;
        }

        if (ticksOpen >= 3L * ConstantsJava.TPS && hasFacehugger() && !world.isClient) {
            var facehugger = new FacehuggerEntityJava(EntitiesJava.FACEHUGGER, world);
            facehugger.refreshPositionAndAngles(getBlockPos(), getYaw(), getPitch());
            facehugger.setVelocity(0.0, 0.7, 0.0);
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
        if (source.getSource() != null) {
            setIsHatching(true);
        }
        return super.damage(source, amount);
    }

    /**
     * Prevents the egg from drowning.
     */
    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    /*
        ANIMATIONS
     */

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (isHatched()) {
            if (hasFacehugger()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("open_loop_nobag", true));
                return PlayState.CONTINUE;
            }

            event.getController().setAnimation(new AnimationBuilder().addAnimation("open_loop", true));
            return PlayState.CONTINUE;
        }

        if (isHatching()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hatching", false).addAnimation("open_loop"));
            return PlayState.CONTINUE;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0f, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }
}
