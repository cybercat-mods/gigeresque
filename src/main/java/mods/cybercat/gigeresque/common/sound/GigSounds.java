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

	public static final SoundEvent ALIEN_AMBIENT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_ambient"));
	public static final SoundEvent ALIEN_HURT = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_hurt"));
	public static final SoundEvent ALIEN_DEATH = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_death"));
	public static final SoundEvent ALIEN_STEP = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "alien_footstep"));

	public static final SoundEvent TB_STEP = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "tb_footstep"));

	public static final SoundEvent EGG_NOTICE = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "egg_notice"));
	public static final SoundEvent EGG_OPEN = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "egg_open"));

	public static final SoundEvent AQUA_LANDMOVE_1 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "aqua_landmove_1"));
	public static final SoundEvent AQUA_LANDMOVE_2 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "aqua_landmove_2"));
	public static final SoundEvent AQUA_LANDMOVE_3 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "aqua_landmove_3"));
	public static final SoundEvent AQUA_LANDMOVE_4 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "aqua_landmove_4"));

	public static final SoundEvent BURSTER_CRAWL_1 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "burster_crawl_1"));
	public static final SoundEvent BURSTER_CRAWL_2 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "burster_crawl_2"));
	public static final SoundEvent BURSTER_CRAWL_3 = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "burster_crawl_3"));

	public static final SoundEvent FACEHUGGER_AMBIENT = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "facehugger_ambient"));
	public static final SoundEvent FACEHUGGER_DEATH = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "facehugger_death"));
	public static final SoundEvent FACEHUGGER_HURT = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "facehugger_hurt"));
	public static final SoundEvent FACEHUGGER_IMPLANT = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "facehugger_implant"));

	public static final SoundEvent CHESTBURSTING = new SoundEvent(
			new Identifier(Gigeresque.MOD_ID, "gigeresque_chestbursting"));

	@Override
	public void initialize() {
		register(ALIEN_AMBIENT);
		register(ALIEN_HURT);
		register(ALIEN_DEATH);

		register(ALIEN_STEP);
		register(TB_STEP);

		register(EGG_NOTICE);
		register(EGG_OPEN);

		register(AQUA_LANDMOVE_1);
		register(AQUA_LANDMOVE_2);
		register(AQUA_LANDMOVE_3);
		register(AQUA_LANDMOVE_4);

		register(BURSTER_CRAWL_1);
		register(BURSTER_CRAWL_2);
		register(BURSTER_CRAWL_3);

		register(FACEHUGGER_AMBIENT);
		register(FACEHUGGER_DEATH);
		register(FACEHUGGER_HURT);
		register(FACEHUGGER_IMPLANT);
	}

	private void register(SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, soundEvent.getId(), soundEvent);
	}
}
