package mods.cybercat.gigeresque.common.block;

import java.util.function.Supplier;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.mixins.common.PointOfInterestTypesInvoker;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public class GigPOIs {

	public static final Supplier<PoiType> GUNSMITH_POI = registerPoiType("nests", () -> new PoiType(PointOfInterestTypesInvoker.invokeGetBlockStates(GigBlocks.NEST_RESIN_WEB_CROSS), 1, 30));
	
	public static Supplier<PoiType> registerPoiType(String name, Supplier<PoiType> poiType) {
		var resourceKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Constants.modResource(name));
		var registry = Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, resourceKey, poiType.get());
		PointOfInterestTypesInvoker.invokeRegisterBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(resourceKey), PoiTypes.getBlockStates(GigBlocks.NEST_RESIN_WEB_CROSS));
		return () -> registry;
	}
}
