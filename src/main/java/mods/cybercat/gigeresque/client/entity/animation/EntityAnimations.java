package mods.cybercat.gigeresque.client.entity.animation;

import mods.cybercat.gigeresque.common.Gigeresque;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityAnimations {
	public static final Identifier ALIEN = new Identifier(Gigeresque.MOD_ID, "animations/alien.animation.json");
	public static final Identifier AQUATIC_ALIEN = new Identifier(Gigeresque.MOD_ID,
			"animations/aquatic_alien.animation.json");
	public static final Identifier AQUATIC_CHESTBURSTER = new Identifier(Gigeresque.MOD_ID,
			"animations/aquatic_chestburster.animation.json");
	public static final Identifier CHESTBURSTER = new Identifier(Gigeresque.MOD_ID,
			"animations/chestburster.animation.json");
	public static final Identifier EGG = new Identifier(Gigeresque.MOD_ID, "animations/egg.animation.json");
	public static final Identifier FACEHUGGER = new Identifier(Gigeresque.MOD_ID,
			"animations/facehugger.animation.json");
	public static final Identifier RUNNER_ALIEN = new Identifier(Gigeresque.MOD_ID,
			"animations/runner_alien.animation.json");
	public static final Identifier RUNNERBURSTER = new Identifier(Gigeresque.MOD_ID,
			"animations/runnerburster.animation.json");
}
