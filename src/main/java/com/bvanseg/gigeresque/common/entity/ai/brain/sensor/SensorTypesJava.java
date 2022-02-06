package com.bvanseg.gigeresque.common.entity.ai.brain.sensor;

import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.mixins.common.entity.ai.SensorTypeInvoker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

public class SensorTypesJava {
    private SensorTypesJava() {
    }

    public static final SensorType<? extends Sensor<? super LivingEntity>> ALIEN_REPELLENT = SensorTypeInvoker.register("alien_repellent_sensor", AlienRepellentSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> DESTRUCTIBLE_LIGHT = (SensorType<? extends Sensor<? super LivingEntity>>) SensorTypeInvoker.register("destructible_light_sensor", DestructibleLightSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_ALIEN_TARGET = SensorTypeInvoker.register("nearest_alien_target_sensor", NearestAlienTargetSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_ALIEN_WEBBING = (SensorType<? extends Sensor<? super LivingEntity>>) SensorTypeInvoker.register("nearest_alien_webbing_sensor", NearestAlienWebbingSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_EGGS = SensorTypeInvoker.register("nearest_eggs_sensor", NearestEggsSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_FACEHUGGER = SensorTypeInvoker.register("nearest_facehugger_sensor", NearestFacehuggersSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_FOOD_ITEM = SensorTypeInvoker.register("nearest_food_item_sensor", NearestFoodItemSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_HOST = SensorTypeInvoker.register("nearest_host_sensor", NearestHostSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_HOSTS = SensorTypeInvoker.register("nearest_hosts_sensor", NearestHostsSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_LARGER_THREAT = SensorTypeInvoker.register("nearest_larger_threat_sensor", NearestLargerThreatSensorJava::new);
    public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_SMALLER_TARGET = SensorTypeInvoker.register("nearest_smaller_target_sensor", NearestSmallerTargetSensorJava::new);
}
