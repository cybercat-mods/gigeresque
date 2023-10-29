package mods.cybercat.gigeresque.common.entity.ai;

import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyNestBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.SBLConstants;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.function.Supplier;

public record GigSensors() implements GigeresqueInitializer {

    public static final Supplier<SensorType<NearbyLightsBlocksSensor<?>>> NEARBY_LIGHT_BLOCKS = register("nearby_light_blocks", NearbyLightsBlocksSensor::new);
    public static final Supplier<SensorType<NearbyNestBlocksSensor<?>>> NEARBY_NEST_BLOCKS = register("nearby_nest_blocks", NearbyNestBlocksSensor::new);
    public static final Supplier<SensorType<NearbyRepellentsSensor<?>>> NEARBY_REPELLENT_BLOCKS = register("nearby_repellent_blocks", NearbyRepellentsSensor::new);
    public static final Supplier<SensorType<ItemEntitySensor<?>>> FOOD_ITEMS = register("food_items", ItemEntitySensor::new);
    private static GigSensors instance;

    public static synchronized GigSensors getInstance() {
        if (instance == null) {
            instance = new GigSensors();
        }
        return instance;
    }

    private static <T extends ExtendedSensor<?>> Supplier<SensorType<T>> register(String id, Supplier<T> sensor) {
        return SBLConstants.SBL_LOADER.registerSensorType(id, sensor);
    }

    @Override
    public void initialize() {
        /*
        Fine to leave empty
         */
    }
}
