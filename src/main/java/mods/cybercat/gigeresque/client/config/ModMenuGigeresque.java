package mods.cybercat.gigeresque.client.config;

import java.util.HashMap;
import java.util.Map;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.config.CustomMidnightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuGigeresque implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> CustomMidnightConfig.getScreen(parent, Gigeresque.MOD_ID);
	}

	@Override
	public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
		HashMap<String, ConfigScreenFactory<?>> map = new HashMap<>();
		CustomMidnightConfig.configClass
				.forEach((modid, cClass) -> map.put(modid, parent -> CustomMidnightConfig.getScreen(parent, modid)));
		return map;
	}
}
