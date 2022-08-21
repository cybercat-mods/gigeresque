package mods.cybercat.gigeresque.common.sound;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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

	public static final SoundEvent ALIEN_HISS = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_hiss"));
	public static final SoundEvent ALIEN_AMBIENT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_ambient"));
	public static final SoundEvent ALIEN_HURT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_hurt"));
	public static final SoundEvent ALIEN_DEATH = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_death"));
	public static final SoundEvent ALIEN_ATTACK = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_attack"));
	public static final SoundEvent ALIEN_STEP = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_footstep"));

	public static final SoundEvent EGG_NOTICE = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "egg_notice"));
	public static final SoundEvent EGG_OPEN = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "egg_open"));

	public static final SoundEvent AQUA_LANDMOVE = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "aqua_landmove"));

	public static final SoundEvent BURSTER_CRAWL = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "burster_crawl"));

	public static final SoundEvent HUGGER_AMBIENT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "hugger_ambient"));
	public static final SoundEvent HUGGER_DEATH = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "hugger_death"));
	public static final SoundEvent HUGGER_HURT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "hugger_hurt"));
	public static final SoundEvent HUGGER_IMPLANT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "hugger_implant"));

	public static final SoundEvent CHESTBURSTING = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "gigeresque_chestbursting"));

	@Override
	public void initialize() {
		register(ALIEN_HISS);
		register(ALIEN_AMBIENT);
		register(ALIEN_HURT);
		register(ALIEN_DEATH);
		register(ALIEN_ATTACK);
		register(ALIEN_STEP);

		register(EGG_NOTICE);
		register(EGG_OPEN);

		register(AQUA_LANDMOVE);

		register(BURSTER_CRAWL);

		register(HUGGER_AMBIENT);
		register(HUGGER_DEATH);
		register(HUGGER_HURT);
		register(HUGGER_IMPLANT);
	}

	private void register(SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, soundEvent.getId(), soundEvent);
	}
}
