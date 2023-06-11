package mods.cybercat.gigeresque.common.item.group;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.item.GigItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GigItemGroups {
	
	public static final ResourceKey<CreativeModeTab> GENERAL = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.modResource("items"));
	public static final ResourceKey<CreativeModeTab> BLOCKS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.modResource("blocks"));
	
	public static void init() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, GENERAL, FabricItemGroup.builder().icon(() -> new ItemStack(GigItems.ALIEN_SPAWN_EGG)) // icon
				.title(Component.translatable("itemGroup.gigeresque.items")) // title
				.displayItems((context, entries) -> {
					entries.accept(GigItems.BLACK_FLUID_BUCKET);
					entries.accept(GigItems.SURGERY_KIT);
					entries.accept(GigItems.EGG_SPAWN_EGG);
					entries.accept(GigItems.FACEHUGGER_SPAWN_EGG);
					entries.accept(GigItems.CHESTBURSTER_SPAWN_EGG);
					entries.accept(GigItems.ALIEN_SPAWN_EGG);
					entries.accept(GigItems.AQUATIC_CHESTBURSTER_SPAWN_EGG);
					entries.accept(GigItems.AQUATIC_ALIEN_SPAWN_EGG);
					entries.accept(GigItems.RUNNERBURSTER_SPAWN_EGG);
					entries.accept(GigItems.RUNNER_ALIEN_SPAWN_EGG);
					entries.accept(GigItems.MUTANT_POPPER_SPAWN_EGG);
					entries.accept(GigItems.MUTANT_HAMMERPEDE_SPAWN_EGG);
					entries.accept(GigItems.MUTANT_STALKER_SPAWN_EGG);
					if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
						entries.accept(GigItems.NEOBURSTER_SPAWN_EGG);
						entries.accept(GigItems.NEOMORPH_ADOLESCENT_SPAWN_EGG);
						entries.accept(GigItems.NEOMORPH_SPAWN_EGG);
						entries.accept(GigItems.SPITTER_SPAWN_EGG);
					}
				}).build()); // build() no longer registers by itself
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS, FabricItemGroup.builder().icon(() -> new ItemStack(GIgBlocks.NEST_RESIN_WEB)) // icon
				.title(Component.translatable("itemGroup.gigeresque.blocks")) // title
				.displayItems((context, entries) -> {
					entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_1);
					entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER);
					entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_1_GOO);
					entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_1_SPORE);
					entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_2);
					entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_3);
					// entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_4);
					// entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_5);
					entries.accept(GIgBlocks.NEST_RESIN);
					entries.accept(GIgBlocks.NEST_RESIN_BLOCK);
					entries.accept(GIgBlocks.NEST_RESIN_WEB);
					entries.accept(GIgBlocks.NEST_RESIN_WEB_CROSS);
					if (FabricLoader.getInstance().isDevelopmentEnvironment())
						entries.accept(GIgBlocks.SPORE_BLOCK);
					entries.accept(GIgBlocks.ORGANIC_ALIEN_BLOCK);
					entries.accept(GIgBlocks.ORGANIC_ALIEN_SLAB);
					entries.accept(GIgBlocks.ORGANIC_ALIEN_STAIRS);
					entries.accept(GIgBlocks.ORGANIC_ALIEN_WALL);
					entries.accept(GIgBlocks.RESINOUS_ALIEN_BLOCK);
					entries.accept(GIgBlocks.RESINOUS_ALIEN_PILLAR);
					entries.accept(GIgBlocks.RESINOUS_ALIEN_SLAB);
					entries.accept(GIgBlocks.RESINOUS_ALIEN_STAIRS);
					entries.accept(GIgBlocks.RESINOUS_ALIEN_WALL);
					entries.accept(GIgBlocks.RIBBED_ALIEN_BLOCK);
					entries.accept(GIgBlocks.RIBBED_ALIEN_PILLAR);
					entries.accept(GIgBlocks.RIBBED_ALIEN_SLAB);
					entries.accept(GIgBlocks.RIBBED_ALIEN_STAIRS);
					entries.accept(GIgBlocks.RIBBED_ALIEN_WALL);
					entries.accept(GIgBlocks.ROUGH_ALIEN_BLOCK);
					entries.accept(GIgBlocks.ROUGH_ALIEN_SLAB);
					entries.accept(GIgBlocks.ROUGH_ALIEN_STAIRS);
					entries.accept(GIgBlocks.ROUGH_ALIEN_WALL);
					entries.accept(GIgBlocks.SINOUS_ALIEN_BLOCK);
					entries.accept(GIgBlocks.SMOOTH_ALIEN_PILLAR);
					entries.accept(GIgBlocks.SINOUS_ALIEN_SLAB);
					entries.accept(GIgBlocks.SINOUS_ALIEN_STAIRS);
					entries.accept(GIgBlocks.SMOOTH_ALIEN_STAIRS);
					entries.accept(GIgBlocks.SINOUS_ALIEN_WALL);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_1);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_2);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_3);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_4);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_5);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_6);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_7);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_8);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_9);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_10);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_11);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_12);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_13);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_14);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_15);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_16);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_17);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_18);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_19);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_20);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_21);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_22);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_23);
					entries.accept(GIgBlocks.MURAL_ALIEN_BLOCK_24);
				}).build()); // build() no longer registers by itself
	}

}
