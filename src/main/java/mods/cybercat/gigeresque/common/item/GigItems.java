package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.group.GigItemGroups;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GigItems {

	public static void init() {
	}

	public static final BucketItem BLACK_FLUID_BUCKET = registerItem("black_fluid_bucket", new BucketItem(GigFluids.BLACK_FLUID_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(GigItemGroups.GENERAL)));

	public static final SurgeryKitItem SURGERY_KIT = registerItem("surgery_kit", new SurgeryKitItem());

	public static final GigSpawnEgg ALIEN_SPAWN_EGG = registerItem("alien_spawn_egg",
			new GigSpawnEgg(Entities.ALIEN, 0x404345, 0x949597));
	public static final GigSpawnEgg AQUATIC_ALIEN_SPAWN_EGG = registerItem("aquatic_alien_spawn_egg",
			new GigSpawnEgg(Entities.AQUATIC_ALIEN, 0x404345, 0x949597));
	public static final GigSpawnEgg AQUATIC_CHESTBURSTER_SPAWN_EGG = registerItem("aquatic_chestburster_spawn_egg",
			new GigSpawnEgg(Entities.AQUATIC_CHESTBURSTER, 0xDED29D, 0x2C2B27));
	public static final GigSpawnEgg CHESTBURSTER_SPAWN_EGG = registerItem("chestburster_spawn_egg",
			new GigSpawnEgg(Entities.CHESTBURSTER, 0xDED29D, 0x2C2B27));
	public static final GigSpawnEgg EGG_SPAWN_EGG = registerItem("egg_spawn_egg",
			new GigSpawnEgg(Entities.EGG, 0x554E45, 0x4D4932));
	public static final GigSpawnEgg FACEHUGGER_SPAWN_EGG = registerItem("facehugger_spawn_egg",
			new GigSpawnEgg(Entities.FACEHUGGER, 0xC7B986, 0x516B21));
	public static final GigSpawnEgg RUNNER_ALIEN_SPAWN_EGG = registerItem("runner_alien_spawn_egg",
			new GigSpawnEgg(Entities.RUNNER_ALIEN, 0x3E230B, 0x623C25));
	public static final GigSpawnEgg RUNNERBURSTER_SPAWN_EGG = registerItem("runnerburster_spawn_egg",
			new GigSpawnEgg(Entities.RUNNERBURSTER, 0xDED29D, 0x2C2B27));
	public static final GigSpawnEgg MUTANT_POPPER_SPAWN_EGG = registerItem("popper_spawn_egg",
			new GigSpawnEgg(Entities.MUTANT_POPPER, 0xdeeae9, 0x816d66));
	public static final GigSpawnEgg MUTANT_HAMMERPEDE_SPAWN_EGG = registerItem("hammerpede_spawn_egg",
			new GigSpawnEgg(Entities.MUTANT_HAMMERPEDE, 0xe3e1d5, 0x826e66));
	public static final GigSpawnEgg MUTANT_STALKER_SPAWN_EGG = registerItem("stalker_spawn_egg",
			new GigSpawnEgg(Entities.MUTANT_STALKER, 0xcdd7d8, 0x816d66));

	public static <I extends Item> I registerItem(String name, I item) {
		return Registry.register(Registry.ITEM, Constants.modResource(name), item);
	}
}
