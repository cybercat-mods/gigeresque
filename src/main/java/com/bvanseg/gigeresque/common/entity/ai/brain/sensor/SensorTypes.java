package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.mixins.common.entity.ai.SensorTypeInvoker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

public class SensorTypes {
    private SensorTypes() {
    }

    public static final SensorType<? extends Sensor<? super LivingEntity>> ALIEN_REPELLENT = SensorTypeInvoker.register("alien_repellent_sensor", AlienRepellentSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> DESTRUCTIBLE_LIGHT = SensorTypeInvoker.register("destructible_light_sensor", DestructibleLightSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_ALIEN_TARGET = SensorTypeInvoker.register("nearest_alien_target_sensor", NearestAlienTargetSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_ALIEN_WEBBING = SensorTypeInvoker.register("nearest_alien_webbing_sensor", NearestAlienWebbingSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_EGGS = SensorTypeInvoker.register("nearest_eggs_sensor", NearestEggsSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_FACEHUGGER = SensorTypeInvoker.register("nearest_facehugger_sensor", NearestFacehuggersSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_FOOD_ITEM = SensorTypeInvoker.register("nearest_food_item_sensor", NearestFoodItemSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_HOST = SensorTypeInvoker.register("nearest_host_sensor", NearestHostSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_HOSTS = SensorTypeInvoker.register("nearest_hosts_sensor", NearestHostsSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_LARGER_THREAT = SensorTypeInvoker.register("nearest_larger_threat_sensor", NearestLargerThreatSensor::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_SMALLER_TARGET = SensorTypeInvoker.register("nearest_smaller_target_sensor", NearestSmallerTargetSensor::new);
}
