package mods.cybercat.gigeresque.client.entity.animation;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class EntityAnimations {
	public static final ResourceLocation ALIEN = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/alien.animation.json");
	public static final ResourceLocation AQUATIC_ALIEN = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/aquatic_alien.animation.json");
	public static final ResourceLocation AQUATIC_CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/aquatic_chestburster.animation.json");
	public static final ResourceLocation CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/chestburster.animation.json");
	public static final ResourceLocation EGG = new ResourceLocation(Gigeresque.MOD_ID, "animations/egg.animation.json");
	public static final ResourceLocation FACEHUGGER = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/facehugger.animation.json");
	public static final ResourceLocation RUNNER_ALIEN = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/runner_alien.animation.json");
	public static final ResourceLocation RUNNERBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"animations/runnerburster.animation.json");
}
