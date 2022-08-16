package mods.cybercat.gigeresque.common.entity.ai.brain.sensor;

import mods.cybercat.gigeresque.mixins.common.entity.ai.SensorTypeInvoker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

public class SensorTypes {
	private SensorTypes() {
	}

	public static final SensorType<? extends Sensor<? super LivingEntity>> NEAREST_ALIEN_WEBBING = SensorTypeInvoker
			.register("nearest_alien_webbing_sensor", NearestAlienWebbingSensor::new);
}
