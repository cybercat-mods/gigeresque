package mods.cybercat.gigeresque.client.entity.texture;

import mods.cybercat.gigeresque.common.Gigeresque;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityTextures {
	private static final String BASE_URL = "textures";
	private static final String BASE_ENTITY_URL = "%s/entity".formatted(BASE_URL);

	public static final Identifier ALIEN = new Identifier(Gigeresque.MOD_ID,
			"%s/alien/alien.png".formatted(BASE_ENTITY_URL));
	public static final Identifier ALIEN_YOUNG = new Identifier(Gigeresque.MOD_ID,
			"%s/alien/alien_young.png".formatted(BASE_ENTITY_URL));
	public static final Identifier AQUATIC_ALIEN = new Identifier(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien.png".formatted(BASE_ENTITY_URL));
	public static final Identifier AQUATIC_ALIEN_CONSTANT = new Identifier(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien_constant.png".formatted(BASE_ENTITY_URL));
	public static final Identifier AQUATIC_ALIEN_TINTABLE = new Identifier(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien_tintable.png".formatted(BASE_ENTITY_URL));
	public static final Identifier AQUATIC_ALIEN_YOUNG = new Identifier(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien_young.png".formatted(BASE_ENTITY_URL));
	public static final Identifier AQUATIC_CHESTBURSTER = new Identifier(Gigeresque.MOD_ID,
			"%s/aquatic_chestburster/aquatic_chestburster.png".formatted(BASE_ENTITY_URL));
	public static final Identifier CHESTBURSTER = new Identifier(Gigeresque.MOD_ID,
			"%s/chestburster/chestburster.png".formatted(BASE_ENTITY_URL));
	public static final Identifier EGG = new Identifier(Gigeresque.MOD_ID, "%s/egg/egg.png".formatted(BASE_ENTITY_URL));
	public static final Identifier FACEHUGGER = new Identifier(Gigeresque.MOD_ID,
			"%s/facehugger/facehugger.png".formatted(BASE_ENTITY_URL));
	public static final Identifier RUNNER_ALIEN = new Identifier(Gigeresque.MOD_ID,
			"%s/runner_alien/runner_alien.png".formatted(BASE_ENTITY_URL));
	public static final Identifier RUNNER_ALIEN_YOUNG = new Identifier(Gigeresque.MOD_ID,
			"%s/runner_alien/runner_alien_young.png".formatted(BASE_ENTITY_URL));
	public static final Identifier RUNNERBURSTER = new Identifier(Gigeresque.MOD_ID,
			"%s/runnerburster/runnerburster.png".formatted(BASE_ENTITY_URL));
}
