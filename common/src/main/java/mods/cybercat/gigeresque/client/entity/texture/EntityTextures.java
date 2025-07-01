package mods.cybercat.gigeresque.client.entity.texture;

import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;

public record EntityTextures() {

    public static final ResourceLocation EGGMORPH_OVERLAY = Constants.modResource("textures/misc/eggmorph_overlay.png");

    public static final ResourceLocation BLACK_FLUID_TEXTURE = Constants.modResource("textures/misc/black_fluid_overlay.png");

    public static final ResourceLocation EGGMORPH_OVERLAY_TEXTURE = Constants.modResource("textures/misc/eggmorph_overlay.png");

    private static final String BASE_URL = "textures";

    private static final String BASE_ENTITY_URL = "%s/entity".formatted(BASE_URL);

    private static final String BASE_BLOCK_URL = "%s/block".formatted(BASE_URL);

    public static final ResourceLocation ENGINEER_HOLOGRAM = Constants.modResource(
        "%s/engineer_hologram/engineer_hologram.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation ALIEN = Constants.modResource("%s/alien/alien.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation ROM_ALIEN = Constants.modResource("%s/rom_alien/rom_alien.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation ALIEN_STASIS = Constants.modResource("%s/alien/alien_stasis.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation ROM_ALIEN_STASIS = Constants.modResource(
        "%s/rom_alien/rom_alien_stasis.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation ALIEN_YOUNG = Constants.modResource("%s/alien/alien_young.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation ROM_ALIEN_YOUNG = Constants.modResource(
        "%s/rom_alien/rom_alien_young.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation CHESTBURSTER_BLOOD = Constants.modResource(
        "%s/chestburster/burster_blood.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation EGG = Constants.modResource("%s/egg/egg.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation AQUA_EGG = Constants.modResource("%s/egg/egg_aqua.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation EGG_ACTIVE = Constants.modResource("%s/egg/egg_active.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation EGG_PETRIFIED = Constants.modResource("%s/egg/egg_petrified.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation AQUATIC_ALIEN = Constants.modResource(
        "%s/aquatic_alien/aquatic_alien.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation AQUATIC_CHESTBURSTER_PETRIFIED = Constants.modResource(
        "%s/aquatic_chestburster/aquatic_chestburster_petrified.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation HAMMERPEDE = Constants.modResource(
        "%s/hammerpede/hammerpede.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation POPPER = Constants.modResource(
        "%s/popper/popper.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation STALKER = Constants.modResource(
        "%s/stalker/stalker.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation STALKER_TRANSPARENT = Constants.modResource(
        "%s/stalker/stalker_transparent.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation CHESTBURSTER_PETRIFIED = Constants.modResource(
        "%s/chestburster/chestburster_petrified.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation NEOMORPH_ADOLESCENT = Constants.modResource(
        "%s/neomorph_adolescent/neomorph_adolescent.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation NEOMORPH = Constants.modResource(
        "%s/neomorph/neomorph.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation SPITTER = Constants.modResource(
        "%s/spitter/spitter.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation NEOBURSTER_PETRIFIED = Constants.modResource(
        "%s/neoburster/neoburster_petrified.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation NEOBURSTER = Constants.modResource(
        "%s/neoburster/neoburster.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RUNNERBURSTER_PETRIFIED = Constants.modResource(
        "%s/runnerburster/runner_burster_petrified.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation SPORE_PETRIFIED = Constants.modResource(
        "%s/neomorph_spore_pods/neomorph_spore_pods_petrified.png".formatted(BASE_BLOCK_URL)
    );

    public static final ResourceLocation RUNNER_ALIEN = Constants.modResource(
        "%s/runner_alien/runner_alien.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RUNNER_ALIEN_YOUNG = Constants.modResource(
        "%s/runner_alien/runner_alien_young.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation HELLMORPH_RUNNER = Constants.modResource(
        "%s/hellmorph_runner/hellmorph_runner.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation BAPHOMORPH = Constants.modResource("%s/baphomorph/baphomorph.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation DRACONICTEMPLEBEAST = Constants.modResource(
        "%s/draconictemplebeast/draconictemplebeast.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RAVENOUSTEMPLEBEAST = Constants.modResource(
        "%s/ravenoustemplebeast/ravenoustemplebeast.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RAVENOUSTEMPLEBEAST_STATIS = Constants.modResource(
        "%s/ravenoustemplebeast/ravenoustemplebeast_stasis.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation MOONLIGHTHORRORTEMPLEBEAST = Constants.modResource(
        "%s/moonlighthorrortemplebeast/moonlighthorrortemplebeast.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation MOONLIGHTHORRORTEMPLEBEAST_STATIS = Constants.modResource(
        "%s/moonlighthorrortemplebeast/moonlighthorrortemplebeast_stasis.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RUNNERBURSTER = Constants.modResource(
        "%s/runnerburster/runnerburster.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation AQUATICBURSTER = Constants.modResource(
        "%s/aquatic_chestburster/aquatic_chestburster.png".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation HELLBURSTER = Constants.modResource("%s/hell_burster/hell_burster.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation FACEHUGGER = Constants.modResource("%s/facehugger/facehugger.png".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation CHESTBURSTER = Constants.modResource(
        "%s/chestburster/chestburster.png".formatted(BASE_ENTITY_URL)
    );
}
