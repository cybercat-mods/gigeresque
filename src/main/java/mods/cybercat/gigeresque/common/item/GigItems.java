package mods.cybercat.gigeresque.common.item;

import mod.azure.azurelib.items.AzureSpawnEgg;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record GigItems() implements GigeresqueInitializer {

	private static GigItems instance;

	synchronized public static GigItems getInstance() {
		if (instance == null) {
			instance = new GigItems();
		}
		return instance;
	}

	@Override
	public void initialize() {
	}

	public static final BucketItem BLACK_FLUID_BUCKET = registerItem("black_fluid_bucket", new BucketItem(GigFluids.BLACK_FLUID_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

	public static final SurgeryKitItem SURGERY_KIT = registerItem("surgery_kit", new SurgeryKitItem());

	public static final AzureSpawnEgg ALIEN_SPAWN_EGG = registerItem("alien_spawn_egg", new AzureSpawnEgg(Entities.ALIEN, 0x404345, 0x949597));
	public static final AzureSpawnEgg AQUATIC_ALIEN_SPAWN_EGG = registerItem("aquatic_alien_spawn_egg", new AzureSpawnEgg(Entities.AQUATIC_ALIEN, 0x404345, 0x949597));
	public static final AzureSpawnEgg AQUATIC_CHESTBURSTER_SPAWN_EGG = registerItem("aquatic_chestburster_spawn_egg", new AzureSpawnEgg(Entities.AQUATIC_CHESTBURSTER, 0xDED29D, 0x2C2B27));
	public static final AzureSpawnEgg CHESTBURSTER_SPAWN_EGG = registerItem("chestburster_spawn_egg", new AzureSpawnEgg(Entities.CHESTBURSTER, 0xDED29D, 0x2C2B27));
	public static final AzureSpawnEgg EGG_SPAWN_EGG = registerItem("egg_spawn_egg", new AzureSpawnEgg(Entities.EGG, 0x554E45, 0x4D4932));
	public static final AzureSpawnEgg FACEHUGGER_SPAWN_EGG = registerItem("facehugger_spawn_egg", new AzureSpawnEgg(Entities.FACEHUGGER, 0xC7B986, 0x516B21));
	public static final AzureSpawnEgg RUNNER_ALIEN_SPAWN_EGG = registerItem("runner_alien_spawn_egg", new AzureSpawnEgg(Entities.RUNNER_ALIEN, 0x3E230B, 0x623C25));
	public static final AzureSpawnEgg RUNNERBURSTER_SPAWN_EGG = registerItem("runnerburster_spawn_egg", new AzureSpawnEgg(Entities.RUNNERBURSTER, 0xDED29D, 0x2C2B27));
	public static final AzureSpawnEgg MUTANT_POPPER_SPAWN_EGG = registerItem("popper_spawn_egg", new AzureSpawnEgg(Entities.MUTANT_POPPER, 0xdeeae9, 0x816d66));
	public static final AzureSpawnEgg MUTANT_HAMMERPEDE_SPAWN_EGG = registerItem("hammerpede_spawn_egg", new AzureSpawnEgg(Entities.MUTANT_HAMMERPEDE, 0xe3e1d5, 0x826e66));
	public static final AzureSpawnEgg MUTANT_STALKER_SPAWN_EGG = registerItem("stalker_spawn_egg", new AzureSpawnEgg(Entities.MUTANT_STALKER, 0xcdd7d8, 0x816d66));
	public static final AzureSpawnEgg NEOBURSTER_SPAWN_EGG = registerItem("neoburster_spawn_egg", new AzureSpawnEgg(Entities.NEOBURSTER, 0xdccfca, 0xcbbcb6));
	public static final AzureSpawnEgg NEOMORPH_ADOLESCENT_SPAWN_EGG = registerItem("neomorph_adolescent_spawn_egg", new AzureSpawnEgg(Entities.NEOMORPH_ADOLESCENT, 0xe6ddd9, 0xada1a2));
	public static final AzureSpawnEgg NEOMORPH_SPAWN_EGG = registerItem("neomorph_spawn_egg", new AzureSpawnEgg(Entities.NEOMORPH, 0xfaf8f5, 0xa587a4));
	public static final AzureSpawnEgg SPITTER_SPAWN_EGG = registerItem("spitter_spawn_egg", new AzureSpawnEgg(Entities.SPITTER, 0xccc737, 0x383a33));

//	public static final AzureSpawnEgg CRUSHER_SPAWN_EGG = registerItem("crusher_spawn_egg", new AzureSpawnEgg(Entities.CRUSHER, 0x9d917b, 0x2d2f2c));
//	public static final AzureSpawnEgg PRAETORIAN_SPAWN_EGG = registerItem("praetorian_spawn_egg", new AzureSpawnEgg(Entities.PRAETORIAN, 0x404345, 0x949597));
//	public static final AzureSpawnEgg ULTRAMORTH_SPAWN_EGG = registerItem("ultramorth_spawn_egg", new AzureSpawnEgg(Entities.ULTRAMORTH, 0xa4adbc, 0x5a666b));

	public static <I extends Item> I registerItem(String name, I item) {
		return Registry.register(BuiltInRegistries.ITEM, Constants.modResource(name), item);
	}
}
