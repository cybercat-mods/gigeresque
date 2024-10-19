package mods.cybercat.gigeresque.common.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;

public record GigSounds() implements GigeresqueInitializer {

    public static final SoundEvent ALIEN_HISS = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_hiss"));

    public static final SoundEvent ALIEN_AMBIENT = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_ambient"));

    public static final SoundEvent ALIEN_HURT = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_hurt"));

    public static final SoundEvent ALIEN_DEATH = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_death"));

    public static final SoundEvent ALIEN_DEATH_THUD = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_death_thud"));

    public static final SoundEvent ALIEN_CLAW = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_claw"));

    public static final SoundEvent ALIEN_TAIL = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_tail"));

    public static final SoundEvent ALIEN_HEADBITE = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_headbite"));

    public static final SoundEvent ALIEN_CRUNCH = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_crunch"));

    public static final SoundEvent ALIEN_FOOTSTEP = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_footstep"));

    public static final SoundEvent ALIEN_HANDSTEP = SoundEvent.createVariableRangeEvent(Constants.modResource("alien_handstep"));

    public static final SoundEvent EGG_NOTICE = SoundEvent.createVariableRangeEvent(Constants.modResource("egg_notice"));

    public static final SoundEvent EGG_OPEN = SoundEvent.createVariableRangeEvent(Constants.modResource("egg_open"));

    public static final SoundEvent AQUA_LANDMOVE = SoundEvent.createVariableRangeEvent(Constants.modResource("aqua_landmove"));

    public static final SoundEvent AQUA_LANDCLAW = SoundEvent.createVariableRangeEvent(Constants.modResource("aqua_landclaw"));

    public static final SoundEvent BURSTER_CRAWL = SoundEvent.createVariableRangeEvent(Constants.modResource("burster_crawl"));

    public static final SoundEvent HUGGER_AMBIENT = SoundEvent.createVariableRangeEvent(Constants.modResource("hugger_ambient"));

    public static final SoundEvent HUGGER_DEATH = SoundEvent.createVariableRangeEvent(Constants.modResource("hugger_death"));

    public static final SoundEvent HUGGER_HURT = SoundEvent.createVariableRangeEvent(Constants.modResource("hugger_hurt"));

    public static final SoundEvent HUGGER_IMPLANT = SoundEvent.createVariableRangeEvent(Constants.modResource("hugger_implant"));

    public static final SoundEvent CHESTBURSTING = SoundEvent.createVariableRangeEvent(Constants.modResource("chestbursting"));

    private static GigSounds instance;

    public static synchronized GigSounds getInstance() {
        if (instance == null)
            instance = new GigSounds();
        return instance;
    }

    @Override
    public void initialize() {
        register(ALIEN_HISS);
        register(ALIEN_AMBIENT);
        register(ALIEN_HURT);
        register(ALIEN_DEATH);
        register(ALIEN_DEATH_THUD);
        register(ALIEN_CLAW);
        register(ALIEN_TAIL);
        register(ALIEN_HEADBITE);
        register(ALIEN_CRUNCH);
        register(ALIEN_FOOTSTEP);
        register(ALIEN_HANDSTEP);

        register(EGG_NOTICE);
        register(EGG_OPEN);

        register(AQUA_LANDMOVE);
        register(AQUA_LANDCLAW);

        register(BURSTER_CRAWL);

        register(HUGGER_AMBIENT);
        register(HUGGER_DEATH);
        register(HUGGER_HURT);
        register(HUGGER_IMPLANT);
    }

    private void register(SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, soundEvent.getLocation(), soundEvent);
    }
}
