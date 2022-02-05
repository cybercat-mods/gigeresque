package com.bvanseg.gigeresque.common.entity.ai.brain.sensor

import com.bvanseg.gigeresque.mixins.common.entity.ai.SensorTypeInvoker

/**
 * @author Boston Vanseghi
 */
object SensorTypes {
    val ALIEN_REPELLENT = SensorTypeInvoker.register("alien_repellent_sensor", ::AlienRepellentSensor)
    val DESTRUCTIBLE_LIGHT = SensorTypeInvoker.register("destructible_light_sensor", ::DestructibleLightSensor)
    val NEAREST_ALIEN_TARGET = SensorTypeInvoker.register("nearest_alien_target_sensor", ::NearestAlienTargetSensor)
    val NEAREST_ALIEN_WEBBING = SensorTypeInvoker.register("nearest_alien_webbing_sensor", ::NearestAlienWebbingSensor)
    val NEAREST_EGGS = SensorTypeInvoker.register("nearest_eggs_sensor", ::NearestEggsSensor)
    val NEAREST_FACEHUGGER = SensorTypeInvoker.register("nearest_facehugger_sensor", ::NearestFacehuggersSensor)
    val NEAREST_FOOD_ITEM = SensorTypeInvoker.register("nearest_food_item_sensor", ::NearestFoodItemSensor)
    val NEAREST_HOST = SensorTypeInvoker.register("nearest_host_sensor", ::NearestHostSensor)
    val NEAREST_HOSTS = SensorTypeInvoker.register("nearest_hosts_sensor", ::NearestHostsSensor)
    val NEAREST_LARGER_THREAT = SensorTypeInvoker.register("nearest_larger_threat_sensor", ::NearestLargerThreatSensor)
    val NEAREST_SMALLER_TARGET =
        SensorTypeInvoker.register("nearest_smaller_target_sensor", ::NearestSmallerTargetSensor)
}