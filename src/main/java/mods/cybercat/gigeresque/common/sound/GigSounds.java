package mods.cybercat.gigeresque.common.sound;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

public class GigSounds implements GigeresqueInitializer {
	private GigSounds() {
	}

	private static GigSounds instance;

	synchronized public static GigSounds getInstance() {
		if (instance == null) {
			instance = new GigSounds();
		}
		return instance;
	}

	public static final SoundEvent ALIEN_HISS = new SoundEvent(Constants.modResource("alien_hiss"));
	public static final SoundEvent ALIEN_AMBIENT = new SoundEvent(Constants.modResource("alien_ambient"));
	public static final SoundEvent ALIEN_HURT = new SoundEvent(Constants.modResource("alien_hurt"));
	public static final SoundEvent ALIEN_DEATH = new SoundEvent(Constants.modResource("alien_death"));
	public static final SoundEvent ALIEN_DEATH_THUD = new SoundEvent(Constants.modResource("alien_death_thud"));
	public static final SoundEvent ALIEN_CLAW = new SoundEvent(Constants.modResource("alien_claw"));
	public static final SoundEvent ALIEN_TAIL = new SoundEvent(Constants.modResource("alien_tail"));
	public static final SoundEvent ALIEN_HEADBITE = new SoundEvent(Constants.modResource("alien_headbite"));
	public static final SoundEvent ALIEN_CRUNCH = new SoundEvent(Constants.modResource("alien_crunch"));
	public static final SoundEvent ALIEN_FOOTSTEP = new SoundEvent(Constants.modResource("alien_footstep"));
	public static final SoundEvent ALIEN_HANDSTEP = new SoundEvent(Constants.modResource("alien_handstep"));

	public static final SoundEvent EGG_NOTICE = new SoundEvent(Constants.modResource("egg_notice"));
	public static final SoundEvent EGG_OPEN = new SoundEvent(Constants.modResource("egg_open"));

	public static final SoundEvent AQUA_LANDMOVE = new SoundEvent(Constants.modResource("aqua_landmove"));
	public static final SoundEvent AQUA_LANDCLAW = new SoundEvent(Constants.modResource("aqua_landclaw"));

	public static final SoundEvent BURSTER_CRAWL = new SoundEvent(Constants.modResource("burster_crawl"));

	public static final SoundEvent HUGGER_AMBIENT = new SoundEvent(Constants.modResource("hugger_ambient"));
	public static final SoundEvent HUGGER_DEATH = new SoundEvent(Constants.modResource("hugger_death"));
	public static final SoundEvent HUGGER_HURT = new SoundEvent(Constants.modResource("hugger_hurt"));
	public static final SoundEvent HUGGER_IMPLANT = new SoundEvent(Constants.modResource("hugger_implant"));

	public static final SoundEvent CHESTBURSTING = new SoundEvent(Constants.modResource("chestbursting"));

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
		Registry.register(Registry.SOUND_EVENT, soundEvent.getLocation(), soundEvent);
	}
}
