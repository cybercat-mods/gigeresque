package mods.cybercat.gigeresque.client.entity.texture;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.resources.ResourceLocation;

public record EntityTextures() {
    public static final ResourceLocation EGGMORPH_OVERLAY = Constants.modResource("textures/misc/eggmorph_overlay.png");
    public static final ResourceLocation BLACK_FLUID_TEXTURE = Constants.modResource("textures/misc/black_fluid_overlay.png");
    public static final ResourceLocation EGGMORPH_OVERLAY_TEXTURE = Constants.modResource("textures/misc/eggmorph_overlay.png");
    private static final String BASE_URL = "textures";
    private static final String BASE_ENTITY_URL = "%s/entity".formatted(BASE_URL);
    public static final ResourceLocation ALIEN = Constants.modResource("%s/alien/alien.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation ALIEN_STATIS = Constants.modResource("%s/alien/alien_stasis.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation ALIEN_YOUNG = Constants.modResource("%s/alien/alien_young.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation CHESTBURSTER_BLOOD = Constants.modResource("%s/chestburster/burster_blood.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation EGG = Constants.modResource("%s/egg/egg.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation EGG_ACTIVE = Constants.modResource("%s/egg/egg_active.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation EGG_PETRIFIED = Constants.modResource("%s/egg/egg_petrified.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation RUNNER_ALIEN_YOUNG = Constants.modResource("%s/runner_alien/runner_alien_young.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation HELLMORPH_RUNNER = Constants.modResource("%s/hellmorph_runner/hellmorph_runner.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation BAPHOMORPH = Constants.modResource("%s/baphomorph/baphomorph.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation RAVENOUSTEMPLEBEAST = Constants.modResource("%s/ravenoustemplebeast/ravenoustemplebeast.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation RAVENOUSTEMPLEBEAST_STATIS = Constants.modResource("%s/ravenoustemplebeast/ravenoustemplebeast_stasis.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation MOONLIGHTHORRORTEMPLEBEAST = Constants.modResource("%s/moonlighthorrortemplebeast/moonlighthorrortemplebeast.png".formatted(BASE_ENTITY_URL));
    public static final ResourceLocation MOONLIGHTHORRORTEMPLEBEAST_STATIS = Constants.modResource("%s/moonlighthorrortemplebeast/moonlighthorrortemplebeast_stasis.png".formatted(BASE_ENTITY_URL));
}
