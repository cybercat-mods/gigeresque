package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.common.entity.AlienEntity;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.Growable;
import com.bvanseg.gigeresque.common.entity.ai.brain.AdultAlienBrainJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypesJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypesJava;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.List;

public class AdultAlienEntityJava extends AlienEntityJava implements IAnimatable, Growable {

    private static final TrackedData<Float> GROWTH = DataTracker.registerData(AdultAlienEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> IS_HISSING = DataTracker.registerData(AdultAlienEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final List<SensorType<? extends Sensor<? extends LivingEntity>>> SENSOR_TYPES = List.of(
            SensorTypesJava.NEAREST_ALIEN_WEBBING,
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorTypesJava.NEAREST_ALIEN_TARGET,
            SensorTypesJava.ALIEN_REPELLENT,
            SensorTypesJava.DESTRUCTIBLE_LIGHT
    );

    private static final List<MemoryModuleType<?>> MEMORY_MODULE_TYPES = List.of(
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleTypesJava.EGGMORPH_TARGET,
            MemoryModuleType.HOME,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.MOBS,
            MemoryModuleTypesJava.NEAREST_ALIEN_WEBBING,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleTypesJava.NEAREST_LIGHT_SOURCE,
            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.PATH,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.WALK_TARGET
    );

    public AdultAlienEntityJava(@NotNull EntityType<? extends AlienEntity> type, @NotNull World world) {
        super(type, world);
        stepHeight = 1.5f;
    }

    public boolean isHissing() {
        return dataTracker.get(IS_HISSING);
    }

    public void setIsHissing(boolean isHissing) {
        dataTracker.set(IS_HISSING, isHissing);
    }

    private long hissingCooldown = 0L;

    public float getGrowth() {
        return dataTracker.get(GROWTH);
    }

    public void setGrowth(float growth) {
        dataTracker.set(GROWTH, growth);
    }

    private AdultAlienBrainJava complexBrain;
}
