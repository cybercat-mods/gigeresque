package mods.cybercat.gigeresque.common.entity.ai;

import java.util.function.Supplier;

import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyNestBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.SBLConstants;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

public final class GigSensors {

	public static void init() {
	}

	public static final Supplier<SensorType<NearbyLightsBlocksSensor<?>>> NEARBY_LIGHT_BLOCKS = register(
			"nearby_light_blocks", NearbyLightsBlocksSensor::new);

	public static final Supplier<SensorType<NearbyNestBlocksSensor<?>>> NEARBY_NEST_BLOCKS = register(
			"nearby_nest_blocks", NearbyNestBlocksSensor::new);

	public static final Supplier<SensorType<NearbyRepellentsSensor<?>>> NEARBY_REPELLENT_BLOCKS = register(
			"nearby_repellent_blocks", NearbyRepellentsSensor::new);

	public static final Supplier<SensorType<ItemEntitySensor<?>>> FOOD_ITEMS = register("food_items",
			ItemEntitySensor::new);

	private static <T extends ExtendedSensor<?>> Supplier<SensorType<T>> register(String id, Supplier<T> sensor) {
		return SBLConstants.SBL_LOADER.registerSensorType(id, sensor);
	}
}
