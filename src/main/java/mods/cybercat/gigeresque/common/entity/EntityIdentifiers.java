package mods.cybercat.gigeresque.common.entity;

import java.util.Map;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.StalkerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EntityIdentifiers {
	private EntityIdentifiers() {
	}

	public static final ResourceLocation ALIEN = new ResourceLocation(Gigeresque.MOD_ID, "alien");
	public static final ResourceLocation AQUATIC_ALIEN = new ResourceLocation(Gigeresque.MOD_ID, "aquatic_alien");
	public static final ResourceLocation AQUATIC_CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID,
			"aquatic_chestburster");
	public static final ResourceLocation CHESTBURSTER = new ResourceLocation(Gigeresque.MOD_ID, "chestburster");
	public static final ResourceLocation EGG = new ResourceLocation(Gigeresque.MOD_ID, "egg");
	public static final ResourceLocation FACEHUGGER = new ResourceLocation(Gigeresque.MOD_ID, "facehugger");
	public static final ResourceLocation RUNNER_ALIEN = new ResourceLocation(Gigeresque.MOD_ID, "runner_alien");
	public static final ResourceLocation RUNNERBURSTER = new ResourceLocation(Gigeresque.MOD_ID, "runnerburster");
	public static final ResourceLocation MUTANT_POPPER = new ResourceLocation(Gigeresque.MOD_ID, "popper");
	public static final ResourceLocation MUTANT_HAMMERPEDE = new ResourceLocation(Gigeresque.MOD_ID, "hammerpede");
	public static final ResourceLocation MUTANT_STALKER = new ResourceLocation(Gigeresque.MOD_ID, "stalker");

	public static final Map<Class<? extends Entity>, ResourceLocation> typeMappings = Map.ofEntries(
			Map.entry(ClassicAlienEntity.class, ALIEN), Map.entry(AquaticAlienEntity.class, AQUATIC_ALIEN),
			Map.entry(AquaticChestbursterEntity.class, AQUATIC_CHESTBURSTER),
			Map.entry(ChestbursterEntity.class, CHESTBURSTER), Map.entry(AlienEggEntity.class, EGG),
			Map.entry(FacehuggerEntity.class, FACEHUGGER), Map.entry(RunnerAlienEntity.class, RUNNER_ALIEN),
			Map.entry(RunnerbursterEntity.class, RUNNERBURSTER), Map.entry(PopperEntity.class, MUTANT_POPPER),
			Map.entry(HammerpedeEntity.class, MUTANT_HAMMERPEDE), Map.entry(StalkerEntity.class, MUTANT_STALKER));

}
