package mods.cybercat.gigeresque.common.item.group;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public record GigItemGroups() implements GigeresqueInitializer {

    public static final ResourceKey<CreativeModeTab> GENERAL = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.modResource("items"));
    public static final ResourceKey<CreativeModeTab> BLOCKS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.modResource("blocks"));
    private static GigItemGroups instance;

    public static synchronized GigItemGroups getInstance() {
        if (instance == null) {
            instance = new GigItemGroups();
        }
        return instance;
    }

    @Override
    public void initialize() {
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
                    }
                    entries.accept(GigItems.SPITTER_SPAWN_EGG);
                }).build()); // build() no longer registers by itself
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS, FabricItemGroup.builder().icon(() -> new ItemStack(GigBlocks.NEST_RESIN_WEB)) // icon
                .title(Component.translatable("itemGroup.gigeresque.blocks")) // title
                .displayItems((context, entries) -> {
                    entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1);
                    entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER);
                    entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO);
                    entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE);
                    entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_2);
                    entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_3);
                    // entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_4);
                    // entries.accept(GIgBlocks.ALIEN_STORAGE_BLOCK_5);
                    entries.accept(GigBlocks.NEST_RESIN);
                    entries.accept(GigBlocks.NEST_RESIN_BLOCK);
                    entries.accept(GigBlocks.NEST_RESIN_WEB);
                    entries.accept(GigBlocks.NEST_RESIN_WEB_CROSS);
                    if (FabricLoader.getInstance().isDevelopmentEnvironment())
                        entries.accept(GigBlocks.SPORE_BLOCK);
                    entries.accept(GigBlocks.ORGANIC_ALIEN_BLOCK);
                    entries.accept(GigBlocks.ORGANIC_ALIEN_SLAB);
                    entries.accept(GigBlocks.ORGANIC_ALIEN_STAIRS);
                    entries.accept(GigBlocks.ORGANIC_ALIEN_WALL);
                    entries.accept(GigBlocks.RESINOUS_ALIEN_BLOCK);
                    entries.accept(GigBlocks.RESINOUS_ALIEN_PILLAR);
                    entries.accept(GigBlocks.RESINOUS_ALIEN_SLAB);
                    entries.accept(GigBlocks.RESINOUS_ALIEN_STAIRS);
                    entries.accept(GigBlocks.RESINOUS_ALIEN_WALL);
                    entries.accept(GigBlocks.RIBBED_ALIEN_BLOCK);
                    entries.accept(GigBlocks.RIBBED_ALIEN_PILLAR);
                    entries.accept(GigBlocks.RIBBED_ALIEN_SLAB);
                    entries.accept(GigBlocks.RIBBED_ALIEN_STAIRS);
                    entries.accept(GigBlocks.RIBBED_ALIEN_WALL);
                    entries.accept(GigBlocks.ROUGH_ALIEN_BLOCK);
                    entries.accept(GigBlocks.ROUGH_ALIEN_SLAB);
                    entries.accept(GigBlocks.ROUGH_ALIEN_STAIRS);
                    entries.accept(GigBlocks.ROUGH_ALIEN_WALL);
                    entries.accept(GigBlocks.SINOUS_ALIEN_BLOCK);
                    entries.accept(GigBlocks.SMOOTH_ALIEN_PILLAR);
                    entries.accept(GigBlocks.SINOUS_ALIEN_SLAB);
                    entries.accept(GigBlocks.SINOUS_ALIEN_STAIRS);
                    entries.accept(GigBlocks.SMOOTH_ALIEN_STAIRS);
                    entries.accept(GigBlocks.SINOUS_ALIEN_WALL);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_1);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_2);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_3);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_4);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_5);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_6);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_7);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_8);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_9);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_10);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_11);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_12);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_13);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_14);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_15);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_16);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_17);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_18);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_19);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_20);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_21);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_22);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_23);
                    entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_24);
                }).build()); // build() no longer registers by itself
    }

}
