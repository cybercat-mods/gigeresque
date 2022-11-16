package mods.cybercat.gigeresque.client.entity.texture;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class EntityTextures {
	private static final String BASE_URL = "textures";
	private static final String BASE_ENTITY_URL = "%s/entity".formatted(BASE_URL);

	public static final ResourceLocation ALIEN = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/alien/alien.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation ALIEN_STATIS = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/alien/alien_stasis.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation ALIEN_YOUNG = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/alien/alien_young.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation AQUATIC_ALIEN = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation AQUATIC_ALIEN_CONSTANT = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien_constant.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation AQUATIC_ALIEN_TINTABLE = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien_tintable.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation AQUATIC_ALIEN_YOUNG = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/aquatic_alien/aquatic_alien_young.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation AQUATIC_CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/aquatic_chestburster/aquatic_chestburster.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/chestburster/chestburster.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation CHESTBURSTER_BLOOD = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/chestburster/burster_blood.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation EGG = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/egg/egg.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation EGG_ACTIVE = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/egg/egg_active.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation FACEHUGGER = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/facehugger/facehugger.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation RUNNER_ALIEN = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/runner_alien/runner_alien.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation RUNNER_ALIEN_YOUNG = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/runner_alien/runner_alien_young.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation RUNNERBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"%s/runnerburster/runnerburster.png".formatted(BASE_ENTITY_URL));
	public static final ResourceLocation JAR = new ResourceLocation(Gigeresque.MOD_ID,
			"textures/block/jar.png");
	public static final ResourceLocation SARCOPHAGUS = new ResourceLocation(Gigeresque.MOD_ID,
			"textures/block/sarcophagus.png");
	public static final ResourceLocation SITTINGIDOL = new ResourceLocation(Gigeresque.MOD_ID,
			"textures/block/sittingidol.png");
	
	public static final ResourceLocation EGGMORPH_OVERLAY = new ResourceLocation(Gigeresque.MOD_ID,
			"textures/misc/eggmorph_overlay.png");
	public static final ResourceLocation BLACK_FLUID_TEXTURE = new ResourceLocation(Gigeresque.MOD_ID,
			"textures/misc/black_fluid_overlay.png");
	public static final ResourceLocation EGGMORPH_OVERLAY_TEXTURE = new ResourceLocation(Gigeresque.MOD_ID,
			"textures/misc/eggmorph_overlay.png");
}
