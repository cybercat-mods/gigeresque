package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.ai.brain.sensor.SensorTypesJava;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class AlienEggEntityJava extends AlienEntityJava implements IAnimatable {
    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ARMOR, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0);
    }

    private static final TrackedData<Boolean> IS_HATCHING = DataTracker.registerData(AlienEggEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_HATCHED = DataTracker.registerData(AlienEggEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_FACEHUGGER = DataTracker.registerData(AlienEggEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final long MAX_HATCH_PROGRESS = 50L;

    private static final List<SensorType<? extends Sensor<? extends LivingEntity>>> SENSOR_TYPES = List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorTypesJava.NEAREST_EGGS,
            SensorTypesJava.NEAREST_FACEHUGGER,
            SensorTypesJava.NEAREST_HOSTS
    );

}
