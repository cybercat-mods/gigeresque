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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EntityIdentifiers {
	private EntityIdentifiers() {
	}

	public static final ResourceLocation ALIEN = new ResourceLocation(Gigeresque.MOD_ID, "alien");
	public static final ResourceLocation AQUATIC_ALIEN = new ResourceLocation(Gigeresque.MOD_ID, "aquatic_alien");
	public static final ResourceLocation AQUATIC_CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID, "aquatic_chestburster");
	public static final ResourceLocation CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID, "chestburster");
	public static final ResourceLocation EGG = new ResourceLocation(Gigeresque.MOD_ID, "egg");
	public static final ResourceLocation FACEHUGGER = new ResourceLocation(Gigeresque.MOD_ID, "facehugger");
	public static final ResourceLocation RUNNER_ALIEN = new ResourceLocation(Gigeresque.MOD_ID, "runner_alien");
	public static final ResourceLocation RUNNERBURSTER = new ResourceLocation(Gigeresque.MOD_ID, "runnerburster");

	public static final Map<Class<? extends Entity>, ResourceLocation> typeMappings = Map.of(ClassicAlienEntity.class, ALIEN,
			AquaticAlienEntity.class, AQUATIC_ALIEN, AquaticChestbursterEntity.class, AQUATIC_CHESTBURSTER,
			ChestbursterEntity.class, CHESTBURSTER, AlienEggEntity.class, EGG, FacehuggerEntity.class, FACEHUGGER,
			RunnerAlienEntity.class, RUNNER_ALIEN, RunnerbursterEntity.class, RUNNERBURSTER);
}
