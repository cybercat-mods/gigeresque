package mods.cybercat.gigeresque.common.entity.ai;

import java.util.function.Supplier;

import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.SBLConstants;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

public final class GigSensors {

	public static void init() {
	}

	public static final Supplier<SensorType<NearbyLightsBlocksSensor<?>>> NEARBY_LIGHT_BLOCKS = register(
			"nearby_light_blocks", NearbyLightsBlocksSensor::new);

	private static <T extends ExtendedSensor<?>> Supplier<SensorType<T>> register(String id, Supplier<T> sensor) {
		return SBLConstants.SBL_LOADER.registerSensorType(id, sensor);
	}
}
