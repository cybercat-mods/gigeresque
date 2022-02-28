package mods.cybercat.gigeresque.common.entity;

import java.util.Map;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EntityIdentifiers {
	private EntityIdentifiers() {
	}

	public static final Identifier ALIEN = new Identifier(Gigeresque.MOD_ID, "alien");
	public static final Identifier AQUATIC_ALIEN = new Identifier(Gigeresque.MOD_ID, "aquatic_alien");
	public static final Identifier AQUATIC_CHESTBURSTER = new Identifier(Gigeresque.MOD_ID, "aquatic_chestburster");
	public static final Identifier CHESTBURSTER = new Identifier(Gigeresque.MOD_ID, "chestburster");
	public static final Identifier EGG = new Identifier(Gigeresque.MOD_ID, "egg");
	public static final Identifier FACEHUGGER = new Identifier(Gigeresque.MOD_ID, "facehugger");
	public static final Identifier RUNNER_ALIEN = new Identifier(Gigeresque.MOD_ID, "runner_alien");
	public static final Identifier RUNNERBURSTER = new Identifier(Gigeresque.MOD_ID, "runnerburster");

	public static final Map<Class<? extends Entity>, Identifier> typeMappings = Map.of(ClassicAlienEntity.class, ALIEN,
			AquaticAlienEntity.class, AQUATIC_ALIEN, AquaticChestbursterEntity.class, AQUATIC_CHESTBURSTER,
			ChestbursterEntity.class, CHESTBURSTER, AlienEggEntity.class, EGG, FacehuggerEntity.class, FACEHUGGER,
			RunnerAlienEntity.class, RUNNER_ALIEN, RunnerbursterEntity.class, RUNNERBURSTER);
}
