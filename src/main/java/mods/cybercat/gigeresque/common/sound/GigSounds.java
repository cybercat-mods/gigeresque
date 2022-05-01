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

	public static final SoundEvent EGG_NOTICE = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "egg_notice"));
	public static final SoundEvent EGG_OPEN = new SoundEvent(new Identifier(Gigeresque.MOD_ID, "egg_open"));

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

		register(EGG_NOTICE);
		register(EGG_OPEN);

		register(FACEHUGGER_AMBIENT);
		register(FACEHUGGER_DEATH);
		register(FACEHUGGER_HURT);
		register(FACEHUGGER_IMPLANT);
	}

	private void register(SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, soundEvent.getId(), soundEvent);
	}
}
