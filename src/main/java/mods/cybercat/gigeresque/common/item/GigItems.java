package mods.cybercat.gigeresque.common.item;

import mod.azure.azurelib.items.AzureSpawnEgg;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class GigItems {

	public static void init() {
	}

	public static final GigBucket BLACK_FLUID_BUCKET = registerItem("black_fluid_bucket", new GigBucket());

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

	public static <I extends Item> I registerItem(String name, I item) {
		return Registry.register(BuiltInRegistries.ITEM, Constants.modResource(name), item);
	}
}
