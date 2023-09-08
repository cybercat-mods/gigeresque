package mods.cybercat.gigeresque.common.entity;

import java.util.Map;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import net.minecraft.resources.ResourceLocation;

public record EntityIdentifiers() {

	public static final ResourceLocation ALIEN = Constants.modResource("alien");
	public static final ResourceLocation AQUATIC_ALIEN = Constants.modResource("aquatic_alien");
	public static final ResourceLocation AQUATIC_CHESTBURSTER = Constants.modResource("aquatic_chestburster");
	public static final ResourceLocation CHESTBURSTER = Constants.modResource("chestburster");
	public static final ResourceLocation EGG = Constants.modResource("egg");
	public static final ResourceLocation FACEHUGGER = Constants.modResource("facehugger");
	public static final ResourceLocation RUNNER_ALIEN = Constants.modResource("runner_alien");
	public static final ResourceLocation RUNNERBURSTER = Constants.modResource("runnerburster");
	public static final ResourceLocation MUTANT_POPPER = Constants.modResource("popper");
	public static final ResourceLocation MUTANT_HAMMERPEDE = Constants.modResource("hammerpede");
	public static final ResourceLocation MUTANT_STALKER = Constants.modResource("stalker");

	public static final ResourceLocation PRAETORIAN = Constants.modResource("praetorian");
	public static final ResourceLocation CRUSHER = Constants.modResource("crusher");
	public static final ResourceLocation ULTRAMORTH = Constants.modResource("ultramorth");
	public static final ResourceLocation SPITTER = Constants.modResource("spitter");

	public static final ResourceLocation NEOBURSTER = Constants.modResource("neoburster");
	public static final ResourceLocation NEOMORPH_ADOLESCENT = Constants.modResource("neomorph_adolescent");
	public static final ResourceLocation NEOMORPH = Constants.modResource("neomorph");

	public static final Map<Class<? extends Object>, ResourceLocation> typeMappings = Map.ofEntries(Map.entry(ClassicAlienEntity.class, ALIEN), Map.entry(AquaticAlienEntity.class, AQUATIC_ALIEN), Map.entry(AquaticChestbursterEntity.class, AQUATIC_CHESTBURSTER), Map.entry(ChestbursterEntity.class, CHESTBURSTER), Map.entry(AlienEggEntity.class, EGG), Map.entry(FacehuggerEntity.class, FACEHUGGER), Map.entry(RunnerAlienEntity.class, RUNNER_ALIEN), Map.entry(RunnerbursterEntity.class, RUNNERBURSTER),
			Map.entry(PopperEntity.class, MUTANT_POPPER), Map.entry(HammerpedeEntity.class, MUTANT_HAMMERPEDE), Map.entry(StalkerEntity.class, MUTANT_STALKER), Map.entry(NeobursterEntity.class, NEOBURSTER), Map.entry(NeomorphAdolescentEntity.class, NEOMORPH_ADOLESCENT), Map.entry(NeomorphEntity.class, NEOMORPH),
			Map.entry(SpitterEntity.class, SPITTER));
//			,Map.entry(CrusherEntity.class, CRUSHER),
//			Map.entry(PraetorianEntity.class, PRAETORIAN), 
//			Map.entry(UltramorpthEntity.class, ULTRAMORTH));

}
