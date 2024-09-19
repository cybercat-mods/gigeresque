package mods.cybercat.gigeresque.common.item.group;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record GigItemGroups() implements CommonCreativeTabRegistryInterface {

    public static final Supplier<CreativeModeTab> GENERAL = GigServices.COMMON_REGISTRY.registerCreativeModeTab(
            CommonMod.MOD_ID,
            "items",
            () -> GigServices.COMMON_REGISTRY.newCreativeTabBuilder()
                    .title(Component.translatable("itemGroup." + CommonMod.MOD_ID + ".items"))
                    .icon(() -> new ItemStack(GigItems.ALIEN_SPAWN_EGG.get()))
                    .displayItems((enabledFeatures, entries) -> {
                        entries.accept(GigItems.BLACK_FLUID_BUCKET.get());
                        entries.accept(GigItems.SURGERY_KIT.get());
                        entries.accept(GigItems.EGG_SPAWN_EGG.get());
                        entries.accept(GigItems.FACEHUGGER_SPAWN_EGG.get());
                        entries.accept(GigItems.CHESTBURSTER_SPAWN_EGG.get());
                        entries.accept(GigItems.ALIEN_SPAWN_EGG.get());
                        entries.accept(GigItems.AQUATIC_CHESTBURSTER_SPAWN_EGG.get());
                        entries.accept(GigItems.AQUATIC_ALIEN_SPAWN_EGG.get());
                        entries.accept(GigItems.RUNNERBURSTER_SPAWN_EGG.get());
                        entries.accept(GigItems.RUNNER_ALIEN_SPAWN_EGG.get());
                        entries.accept(GigItems.MUTANT_POPPER_SPAWN_EGG.get());
                        entries.accept(GigItems.MUTANT_HAMMERPEDE_SPAWN_EGG.get());
                        entries.accept(GigItems.MUTANT_STALKER_SPAWN_EGG.get());
                        if (CommonMod.config.enableDevEntites) {
                            entries.accept(GigItems.NEOBURSTER_SPAWN_EGG.get());
                            entries.accept(GigItems.NEOMORPH_ADOLESCENT_SPAWN_EGG.get());
                            entries.accept(GigItems.NEOMORPH_SPAWN_EGG.get());
                            entries.accept(GigItems.DRACONICTEMPLEBEAST_SPAWN_EGG.get());
                            entries.accept(GigItems.RAVENOUSTEMPLEBEAST_SPAWN_EGG.get());
                            entries.accept(GigItems.MOONLIGHTHORRORTEMPLEBEAST_SPAWN_EGG.get());
                            entries.accept(GigItems.HELLMORPH_RUNNER_SPAWN_EGG.get());
                            entries.accept(GigItems.BAPHOMORPH_SPAWN_EGG.get());
                            entries.accept(GigItems.SPITTER_SPAWN_EGG.get());
                        }
                    })
                    .build());

    public static final Supplier<CreativeModeTab> BLOCKS = GigServices.COMMON_REGISTRY.registerCreativeModeTab(
            CommonMod.MOD_ID,
            "blocks",
            () -> GigServices.COMMON_REGISTRY.newCreativeTabBuilder()
                    .title(Component.translatable("itemGroup." + CommonMod.MOD_ID + ".blocks"))
                    .icon(() -> new ItemStack(GigBlocks.NEST_RESIN_WEB.get()))
                    .displayItems((enabledFeatures, entries) -> {
                        entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1.get());
                        entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER.get());
                        entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO.get());
                        entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE.get());
                        entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_2.get());
                        entries.accept(GigBlocks.ALIEN_STORAGE_BLOCK_3.get());
                        entries.accept(GigBlocks.NEST_RESIN.get());
                        entries.accept(GigBlocks.NEST_RESIN_BLOCK.get());
                        entries.accept(GigBlocks.NEST_RESIN_WEB.get());
                        entries.accept(GigBlocks.NEST_RESIN_WEB_CROSS.get());
                        if (CommonMod.config.enableDevEntites)
                            entries.accept(GigBlocks.SPORE_BLOCK.get());
                        entries.accept(GigBlocks.ORGANIC_ALIEN_BLOCK.get());
                        entries.accept(GigBlocks.ORGANIC_ALIEN_SLAB.get());
                        entries.accept(GigBlocks.ORGANIC_ALIEN_STAIRS.get());
                        entries.accept(GigBlocks.ORGANIC_ALIEN_WALL.get());
                        entries.accept(GigBlocks.RESINOUS_ALIEN_BLOCK.get());
                        entries.accept(GigBlocks.RESINOUS_ALIEN_PILLAR.get());
                        entries.accept(GigBlocks.RESINOUS_ALIEN_SLAB.get());
                        entries.accept(GigBlocks.RESINOUS_ALIEN_STAIRS.get());
                        entries.accept(GigBlocks.RESINOUS_ALIEN_WALL.get());
                        entries.accept(GigBlocks.RIBBED_ALIEN_BLOCK.get());
                        entries.accept(GigBlocks.RIBBED_ALIEN_PILLAR.get());
                        entries.accept(GigBlocks.RIBBED_ALIEN_SLAB.get());
                        entries.accept(GigBlocks.RIBBED_ALIEN_STAIRS.get());
                        entries.accept(GigBlocks.RIBBED_ALIEN_WALL.get());
                        entries.accept(GigBlocks.ROUGH_ALIEN_BLOCK.get());
                        entries.accept(GigBlocks.ROUGH_ALIEN_SLAB.get());
                        entries.accept(GigBlocks.ROUGH_ALIEN_STAIRS.get());
                        entries.accept(GigBlocks.ROUGH_ALIEN_WALL.get());
                        entries.accept(GigBlocks.SINOUS_ALIEN_BLOCK.get());
                        entries.accept(GigBlocks.SMOOTH_ALIEN_PILLAR.get());
                        entries.accept(GigBlocks.SINOUS_ALIEN_SLAB.get());
                        entries.accept(GigBlocks.SINOUS_ALIEN_STAIRS.get());
                        entries.accept(GigBlocks.SMOOTH_ALIEN_STAIRS.get());
                        entries.accept(GigBlocks.SINOUS_ALIEN_WALL.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_1.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_2.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_3.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_4.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_5.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_6.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_7.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_8.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_9.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_10.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_11.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_12.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_13.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_14.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_15.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_16.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_17.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_18.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_19.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_20.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_21.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_22.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_23.get());
                        entries.accept(GigBlocks.MURAL_ALIEN_BLOCK_24.get());
                        entries.accept(GigBlocks.PETRIFIED_OBJECT_BLOCK.get());
                    })
                    .build());

    public static void initialize() {
    }

}
