package mods.cybercat.gigeresque.client.entity.model;

import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;

public record EntityModels() {

    private static final String BASE_URL = "geo";

    private static final String BASE_ENTITY_URL = "%s/entity".formatted(BASE_URL);

    private static final String BASE_BLOCK_URL = "%s/block".formatted(BASE_URL);

    public static final ResourceLocation ENGINEER_HOLOGRAM = Constants.modResource(
        "%s/engineer_hologram/engineer_hologram.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation SPITTER = Constants.modResource("%s/spitter/spitter.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation AQUA_EGG = Constants.modResource("%s/egg/egg.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation ROM_ALIEN = Constants.modResource("%s/rom_alien/rom_alien.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation HAMMERPEDE = Constants.modResource("%s/hammerpede/hammerpede.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation POPPER = Constants.modResource("%s/popper/popper.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation STALKER = Constants.modResource("%s/stalker/stalker.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation NEOBURSTER = Constants.modResource("%s/neoburster/neoburster.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation NEOMORPH_ADOLESCENT = Constants.modResource(
        "%s/neomorph_adolescent/neomorph_adolescent.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation NEOMORPH = Constants.modResource("%s/neomorph/neomorph.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation RUNNER_ALIEN = Constants.modResource(
        "%s/runner_alien/runner_alien.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RUNNERBURSTER = Constants.modResource(
        "%s/runnerburster/runnerburster.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation DRACONICTEMPLEBEAST = Constants.modResource(
        "%s/draconictemplebeast/draconictemplebeast.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation MOONLIGHTHORRORTEMPLEBEAST = Constants.modResource(
        "%s/moonlighthorrortemplebeast/moonlighthorrortemplebeast.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation RAVENOUSTEMPLEBEAST = Constants.modResource(
        "%s/ravenoustemplebeast/ravenoustemplebeast.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation AQUATIC_ALIEN = Constants.modResource(
        "%s/aquatic_alien/aquatic_alien.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation AQUATICBURSTER = Constants.modResource(
        "%s/aquatic_chestburster/aquatic_chestburster.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation ALIEN = Constants.modResource("%s/alien/alien.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation CHESTBURSTER = Constants.modResource(
        "%s/chestburster/chestburster.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation EGG = Constants.modResource("%s/egg/egg.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation FACEHUGGER = Constants.modResource("%s/facehugger/facehugger.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation BAPHOMORPH = Constants.modResource("%s/baphomorph/baphomorph.geo.json".formatted(BASE_ENTITY_URL));

    public static final ResourceLocation HELLBURSTER = Constants.modResource(
        "%s/hell_burster/hell_burster.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation HELLMORPH_RUNNER = Constants.modResource(
        "%s/hellmorph_runner/hellmorph_runner.geo.json".formatted(BASE_ENTITY_URL)
    );

    public static final ResourceLocation PETRIFIED_1 = Constants.modResource(
            "%s/petrified/petrified_1.geo.json".formatted(BASE_BLOCK_URL)
    );

    public static final ResourceLocation PETRIFIED_2 = Constants.modResource(
            "%s/petrified/petrified_2.geo.json".formatted(BASE_BLOCK_URL)
    );

    public static final ResourceLocation PETRIFIED_3 = Constants.modResource(
            "%s/petrified/petrified_3.geo.json".formatted(BASE_BLOCK_URL)
    );

    public static final ResourceLocation PETRIFIED_4 = Constants.modResource(
            "%s/petrified/petrified_4.geo.json".formatted(BASE_BLOCK_URL)
    );

    public static final ResourceLocation PETRIFIED_5 = Constants.modResource(
            "%s/neomorph_spore_pods/neomorph_spore_pods.geo.json".formatted(BASE_BLOCK_URL)
    );

    public static final ResourceLocation PETRIFIED_STATUE = Constants.modResource(
            "%s/petrified/petrified_statue.geo.json".formatted(BASE_BLOCK_URL)
    );
}
