package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.EntitiesJava;
import com.bvanseg.gigeresque.common.entity.GrowableJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.ChestbursterBrainJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypesJava;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ChestbursterEntityJava extends AlienEntityJava implements IAnimatable, GrowableJava {
    public ChestbursterEntityJava(EntityType<? extends ChestbursterEntityJava> type, World world) {
        super (type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes()  {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.43000000417232515)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3);
    }

    private static final List<SensorType<? extends Sensor<? super ChestbursterEntityJava>>> SENSOR_TYPES = List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorTypesJava.ALIEN_REPELLENT,
            SensorTypesJava.NEAREST_FOOD_ITEM,
            SensorTypesJava.NEAREST_LARGER_THREAT,
            SensorTypesJava.NEAREST_SMALLER_TARGET
    );

    private static final List<MemoryModuleType<?>> MEMORY_MODULE_TYPES = List.of(
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.MOBS,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.PATH,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.WALK_TARGET
    );

    private static final TrackedData<Float> GROWTH = DataTracker.registerData(ChestbursterEntityJava.class, TrackedDataHandlerRegistry.FLOAT);

    private final AnimationFactory animationFactory = new AnimationFactory(this);

    @Override
    public float getGrowth() {
        return dataTracker.get(GROWTH);
    }

    @Override
    public void setGrowth(float growth) {
        dataTracker.set(GROWTH, growth);
    }

    @Override
    protected int getAcidDiameter() {
        return 1;
    }

    private ChestbursterBrainJava complexBrain;
    protected String hostId = null;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(GROWTH, 0.0f);
    }

    @Override
    protected Brain.Profile<? extends ChestbursterEntityJava> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<? extends ChestbursterEntityJava> deserializeBrain(Dynamic<?> dynamic) {
        complexBrain = new ChestbursterBrainJava(this);
        return complexBrain.initialize(createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<ChestbursterEntityJava> getBrain() {
        return (Brain<ChestbursterEntityJava>) super.getBrain();
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient && this.isAlive()) {
            grow(this, 1 * getGrowthMultiplier());
        }
    }

    @Override
    protected void mobTick() {
        world.getProfiler().push("chestbursterBrain");
        complexBrain.tick();
        world.getProfiler().pop();
        complexBrain.tickActivities();
        super.mobTick();
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

    /*
     * GROWTH
     */

    @Override
    public float getGrowthMultiplier() {
        return GigeresqueJava.config.miscellaneous.chestbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return ConstantsJava.TPD / 2.0f;
    }

    @Override
    public LivingEntity growInto() {
        var entity = new RunnerbursterEntityJava(EntitiesJava.RUNNERBURSTER, world);
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

        if (velocityLength > 0.0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("slither", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }
}
