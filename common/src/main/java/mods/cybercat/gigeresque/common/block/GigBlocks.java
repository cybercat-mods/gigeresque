package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.storage.*;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.item.CommonItemRegistryInterface;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public record GigBlocks() implements CommonBlockRegistryInterface, CommonItemRegistryInterface {
    public static final Supplier<SporeBlock> SPORE_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "neomorph_spore_pods",
            () -> new SporeBlock(
            Properties.of().sound(SoundType.NYLIUM).noOcclusion().strength(15, 15)));
    public static final Supplier<BlockItem> SPORE_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "neomorph_spore_pods", () -> new GigBlockItem(SPORE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<PetrifiedObjectBlock> PETRIFIED_OBJECT_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "petrified_object",
            () -> new PetrifiedObjectBlock(
                    Properties.of().sound(SoundType.STONE).randomTicks().strength(15, 15).noLootTable()));
    public static final Supplier<BlockItem> PETRIFIED_OBJECT_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "petrified_object", () -> new BlockItem(PETRIFIED_OBJECT_BLOCK.get(), new Item.Properties()));

    /*
     * NORMAL BLOCKS
     */
    public static final Supplier<AlienSarcophagusInvisBlock> ALIEN_STORAGE_BLOCK_INVIS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_invis",
            AlienSarcophagusInvisBlock::new);
    public static final Supplier<SittingIdolInvisBlock> ALIEN_STORAGE_BLOCK_INVIS2 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_invis2",
            SittingIdolInvisBlock::new);
    public static final Supplier<AlienSarcophagusBlock> ALIEN_STORAGE_BLOCK_1 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_block1",
            () -> new AlienSarcophagusBlock(
                    Properties.of().sound(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).noOcclusion()));
    public static final Supplier<BlockItem> ALIEN_STORAGE_BLOCK_1_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "alien_storage_block1", () -> new BlockItem(ALIEN_STORAGE_BLOCK_1.get(), new Item.Properties()));
    public static final Supplier<AlienSarcophagusHuggerBlock> ALIEN_STORAGE_BLOCK_1_HUGGER = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_block1_hugger",
            AlienSarcophagusHuggerBlock::new);
    public static final Supplier<BlockItem> ALIEN_STORAGE_BLOCK_1_HUGGER_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "alien_storage_block1_hugger", () -> new BlockItem(ALIEN_STORAGE_BLOCK_1_HUGGER.get(), new Item.Properties()));
    public static final Supplier<AlienSarcophagusGooBlock> ALIEN_STORAGE_BLOCK_1_GOO = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_block1_goo",
            AlienSarcophagusGooBlock::new);
    public static final Supplier<BlockItem> ALIEN_STORAGE_BLOCK_1_GOO_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "alien_storage_block1_goo", () -> new BlockItem(ALIEN_STORAGE_BLOCK_1_GOO.get(), new Item.Properties()));
    public static final Supplier<AlienSarcophagusSporeBlock> ALIEN_STORAGE_BLOCK_1_SPORE = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_block1_spore",
            AlienSarcophagusSporeBlock::new);
    public static final Supplier<BlockItem> ALIEN_STORAGE_BLOCK_1_SPORE_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "alien_storage_block1_spore", () -> new BlockItem(ALIEN_STORAGE_BLOCK_1_SPORE.get(), new Item.Properties()));
    public static final Supplier<Block> ALIEN_STORAGE_BLOCK_2 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_block2",
            () -> new AlienJarBlock(
                    Properties.of().sound(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).noOcclusion()));
    public static final Supplier<BlockItem> ALIEN_STORAGE_BLOCK_2_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "alien_storage_block2", () -> new BlockItem(ALIEN_STORAGE_BLOCK_2.get(), new Item.Properties()));
    public static final Supplier<Block> ALIEN_STORAGE_BLOCK_3 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "alien_storage_block3",
            () -> new SittingIdolBlock(
                    Properties.of().sound(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).noOcclusion()));
    public static final Supplier<BlockItem> ALIEN_STORAGE_BLOCK_3_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "alien_storage_block3", () -> new BlockItem(ALIEN_STORAGE_BLOCK_3.get(), new Item.Properties()));

    public static final Supplier<Block> NEST_RESIN = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "nest_resin",
            () -> new NestResinBlock(
                    Properties.of().sound(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f)));
    public static final Supplier<BlockItem> NEST_RESIN_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "nest_resin", () -> new BlockItem(NEST_RESIN.get(), new Item.Properties()));
    public static final Supplier<Block> NEST_RESIN_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "nest_resin_block",
            () -> new AbstractNestBlock(
                    Properties.of().sound(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f)));
    public static final Supplier<BlockItem> NEST_RESIN_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "nest_resin_block", () -> new BlockItem(NEST_RESIN_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Block> NEST_RESIN_WEB = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "nest_resin_web",
            () -> new NestResinWebBlock(
                    Properties.of().sound(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f).noCollission()));
    public static final Supplier<BlockItem> NEST_RESIN_WEB_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "nest_resin_web", () -> new BlockItem(NEST_RESIN_WEB.get(), new Item.Properties()));
    public static final Supplier<Block> NEST_RESIN_WEB_CROSS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "nest_resin_web_cross",
            () -> new NestResinWebFullBlock(Properties.of().sound(
                            SoundType.HONEY_BLOCK).noOcclusion().requiresCorrectToolForDrops().strength(5.0f, 8.0f).noCollission()));
    public static final Supplier<BlockItem> NEST_RESIN_WEB_CROSS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "nest_resin_web_cross", () -> new BlockItem(NEST_RESIN_WEB_CROSS.get(), new Item.Properties()));

    public static final Supplier<Block> ORGANIC_ALIEN_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "organic_alien_block",
            () -> new GigBlock(
                    Properties.of().requiresCorrectToolForDrops().strength(CommonMod.config.alienblockHardness,
                            CommonMod.config.alienblockResistance).sound(SoundType.NETHERRACK).explosionResistance(10)));
    public static final Supplier<BlockItem> ORGANIC_ALIEN_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "organic_alien_block", () -> new BlockItem(ORGANIC_ALIEN_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Block> RESINOUS_ALIEN_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "resinous_alien_block",
            () -> new GigBlock(
                    Properties.of().requiresCorrectToolForDrops().strength(CommonMod.config.alienblockHardness,
                            CommonMod.config.alienblockResistance).sound(SoundType.DEEPSLATE).explosionResistance(10)));
    public static final Supplier<BlockItem> RESINOUS_ALIEN_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "resinous_alien_block", () -> new BlockItem(RESINOUS_ALIEN_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Block> RIBBED_ALIEN_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "ribbed_alien_block",
            () -> new GigBlock(
                    Properties.of().requiresCorrectToolForDrops().strength(CommonMod.config.alienblockHardness,
                            CommonMod.config.alienblockResistance).sound(SoundType.DEEPSLATE).explosionResistance(10)));
    public static final Supplier<BlockItem> RIBBED_ALIEN_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "ribbed_alien_block", () -> new BlockItem(RIBBED_ALIEN_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Block> ROUGH_ALIEN_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "rough_alien_block",
            () -> new GigBlock(
                    Properties.of().requiresCorrectToolForDrops().strength(CommonMod.config.alienblockHardness,
                            CommonMod.config.alienblockResistance).sound(SoundType.DEEPSLATE).explosionResistance(10)));
    public static final Supplier<BlockItem> ROUGH_ALIEN_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "rough_alien_block", () -> new BlockItem(ROUGH_ALIEN_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Block> SINOUS_ALIEN_BLOCK = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "sinous_alien_block",
            () -> new GigBlock(
                    Properties.of().requiresCorrectToolForDrops().strength(CommonMod.config.alienblockHardness,
                            CommonMod.config.alienblockResistance).sound(SoundType.DEEPSLATE).explosionResistance(10)));
    public static final Supplier<BlockItem> SINOUS_ALIEN_BLOCK_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "sinous_alien_block", () -> new BlockItem(SINOUS_ALIEN_BLOCK.get(), new Item.Properties()));

    public static final Supplier<Block> MURAL_ALIEN_BLOCK_1 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_1",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_1_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_1", () -> new BlockItem(MURAL_ALIEN_BLOCK_1.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_2 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_2",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_2_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_2", () -> new BlockItem(MURAL_ALIEN_BLOCK_2.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_3 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_3",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_3_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_3", () -> new BlockItem(MURAL_ALIEN_BLOCK_3.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_4 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_4",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_4_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_4", () -> new BlockItem(MURAL_ALIEN_BLOCK_4.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_5 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_5",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_5_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_5", () -> new BlockItem(MURAL_ALIEN_BLOCK_5.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_6 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_6",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_6_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_6", () -> new BlockItem(MURAL_ALIEN_BLOCK_6.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_7 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_7",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_7_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_7", () -> new BlockItem(MURAL_ALIEN_BLOCK_7.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_8 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_8",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_8_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_8", () -> new BlockItem(MURAL_ALIEN_BLOCK_8.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_9 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_9",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_9_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_9", () -> new BlockItem(MURAL_ALIEN_BLOCK_9.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_10 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_10",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_10_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_10", () -> new BlockItem(MURAL_ALIEN_BLOCK_10.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_11 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_11",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_11_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_11", () -> new BlockItem(MURAL_ALIEN_BLOCK_11.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_12 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_12",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_12_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_12", () -> new BlockItem(MURAL_ALIEN_BLOCK_12.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_13 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_13",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_13_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_13", () -> new BlockItem(MURAL_ALIEN_BLOCK_13.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_14 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_14",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_14_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_14", () -> new BlockItem(MURAL_ALIEN_BLOCK_14.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_15 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_15",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_15_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_15", () -> new BlockItem(MURAL_ALIEN_BLOCK_15.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_16 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_16",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_16_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_16", () -> new BlockItem(MURAL_ALIEN_BLOCK_16.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_17 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_17",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_17_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_17", () -> new BlockItem(MURAL_ALIEN_BLOCK_17.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_18 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_18",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_18_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_18", () -> new BlockItem(MURAL_ALIEN_BLOCK_18.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_19 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_19",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_19_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_19", () -> new BlockItem(MURAL_ALIEN_BLOCK_19.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_20 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_20",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_20_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_20", () -> new BlockItem(MURAL_ALIEN_BLOCK_20.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_21 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_21",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_21_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_21", () -> new BlockItem(MURAL_ALIEN_BLOCK_21.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_22 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_22",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_22_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_22", () -> new BlockItem(MURAL_ALIEN_BLOCK_22.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_23 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_23",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_23_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_23", () -> new BlockItem(MURAL_ALIEN_BLOCK_23.get(), new Item.Properties()));
    public static final Supplier<Block> MURAL_ALIEN_BLOCK_24 = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "mural_block_24",
            MuralBlock::new);
    public static final Supplier<BlockItem> MURAL_ALIEN_BLOCK_24_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "mural_block_24", () -> new BlockItem(MURAL_ALIEN_BLOCK_24.get(), new Item.Properties()));

    /*
     * PILLARS
     */
    public static final Supplier<Block> RESINOUS_ALIEN_PILLAR = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "resinous_alien_pillar",
            () -> new RotatedPillarBlock(Properties.ofFullCopy(ROUGH_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> RESINOUS_ALIEN_PILLAR_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "resinous_alien_pillar", () -> new BlockItem(RESINOUS_ALIEN_PILLAR.get(), new Item.Properties()));
    public static final Supplier<Block> RIBBED_ALIEN_PILLAR = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "ribbed_alien_pillar",
            () -> new RotatedPillarBlock(Properties.ofFullCopy(ROUGH_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> RIBBED_ALIEN_PILLAR_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "ribbed_alien_pillar", () -> new BlockItem(RIBBED_ALIEN_PILLAR.get(), new Item.Properties()));
    public static final Supplier<Block> RIBBED_ALIEN_STAIRS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "ribbed_alien_stairs",
            () -> new CustomStairsBlock(RIBBED_ALIEN_PILLAR.get()));
    public static final Supplier<BlockItem> RIBBED_ALIEN_STAIRS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "ribbed_alien_stairs", () -> new BlockItem(RIBBED_ALIEN_STAIRS.get(), new Item.Properties()));
    public static final Supplier<Block> SMOOTH_ALIEN_PILLAR = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "smooth_alien_pillar",
            () -> new RotatedPillarBlock(Properties.ofFullCopy(SINOUS_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> SMOOTH_ALIEN_PILLAR_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "smooth_alien_pillar", () -> new BlockItem(SMOOTH_ALIEN_PILLAR.get(), new Item.Properties()));
    public static final Supplier<Block> SMOOTH_ALIEN_STAIRS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "smooth_alien_stairs",
            () -> new CustomStairsBlock(SMOOTH_ALIEN_PILLAR.get()));
    public static final Supplier<BlockItem> SMOOTH_ALIEN_STAIRS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "smooth_alien_stairs", () -> new BlockItem(SMOOTH_ALIEN_STAIRS.get(), new Item.Properties()));
    /*
     * SLABS
     */
    public static final Supplier<Block> ORGANIC_ALIEN_SLAB = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "organic_alien_slab",
            () -> new SlabBlock(Properties.ofFullCopy(ORGANIC_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> ORGANIC_ALIEN_SLAB_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "organic_alien_slab", () -> new BlockItem(ORGANIC_ALIEN_SLAB.get(), new Item.Properties()));
    public static final Supplier<Block> RESINOUS_ALIEN_SLAB = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "resinous_alien_slab",
            () -> new SlabBlock(Properties.ofFullCopy(ORGANIC_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> RESINOUS_ALIEN_SLAB_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "resinous_alien_slab", () -> new BlockItem(RESINOUS_ALIEN_SLAB.get(), new Item.Properties()));
    public static final Supplier<Block> RIBBED_ALIEN_SLAB = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "ribbed_alien_slab",
            () -> new SlabBlock(Properties.ofFullCopy(ORGANIC_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> RIBBED_ALIEN_SLAB_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "ribbed_alien_slab", () -> new BlockItem(RIBBED_ALIEN_SLAB.get(), new Item.Properties()));
    public static final Supplier<Block> ROUGH_ALIEN_SLAB = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "rough_alien_slab",
            () -> new SlabBlock(Properties.ofFullCopy(ROUGH_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> ROUGH_ALIEN_SLAB_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "rough_alien_slab", () -> new BlockItem(ROUGH_ALIEN_SLAB.get(), new Item.Properties()));
    public static final Supplier<Block> SINOUS_ALIEN_SLAB = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "sinous_alien_slab",
            () -> new SlabBlock(Properties.ofFullCopy(SINOUS_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> SINOUS_ALIEN_SLAB_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "sinous_alien_slab", () -> new BlockItem(SINOUS_ALIEN_SLAB.get(), new Item.Properties()));
    /*
     * STAIRS
     */
    public static final Supplier<Block> ORGANIC_ALIEN_STAIRS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "organic_alien_stairs",
            () -> new CustomStairsBlock(ORGANIC_ALIEN_BLOCK.get()));
    public static final Supplier<BlockItem> ORGANIC_ALIEN_STAIRS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "organic_alien_stairs", () -> new BlockItem(ORGANIC_ALIEN_STAIRS.get(), new Item.Properties()));
    public static final Supplier<Block> RESINOUS_ALIEN_STAIRS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "resinous_alien_stairs",
            () -> new CustomStairsBlock(RESINOUS_ALIEN_BLOCK.get()));
    public static final Supplier<BlockItem> RESINOUS_ALIEN_STAIRS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "resinous_alien_stairs", () -> new BlockItem(RESINOUS_ALIEN_STAIRS.get(), new Item.Properties()));
    public static final Supplier<Block> ROUGH_ALIEN_STAIRS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "rough_alien_stairs",
            () -> new CustomStairsBlock(ROUGH_ALIEN_BLOCK.get()));
    public static final Supplier<BlockItem> ROUGH_ALIEN_STAIRS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "rough_alien_stairs", () -> new BlockItem(ROUGH_ALIEN_STAIRS.get(), new Item.Properties()));
    public static final Supplier<Block> SINOUS_ALIEN_STAIRS = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "sinous_alien_stairs",
            () -> new CustomStairsBlock(SINOUS_ALIEN_BLOCK.get()));
    public static final Supplier<BlockItem> SINOUS_ALIEN_STAIRS_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "sinous_alien_stairs", () -> new BlockItem(SINOUS_ALIEN_STAIRS.get(), new Item.Properties()));
    /*
     * WALLS
     */
    public static final Supplier<WallBlock> ORGANIC_ALIEN_WALL = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "organic_alien_wall",
            () -> new WallBlock(
                    Properties.ofFullCopy(ORGANIC_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> ORGANIC_ALIEN_WALL_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "organic_alien_wall", () -> new BlockItem(ORGANIC_ALIEN_WALL.get(), new Item.Properties()));
    public static final Supplier<WallBlock> RESINOUS_ALIEN_WALL = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "resinous_alien_wall",
            () -> new WallBlock(
                    Properties.ofFullCopy(RESINOUS_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> RESINOUS_ALIEN_WALL_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "resinous_alien_wall", () -> new BlockItem(RESINOUS_ALIEN_WALL.get(), new Item.Properties()));
    public static final Supplier<WallBlock> RIBBED_ALIEN_WALL = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "ribbed_alien_wall",
            () -> new WallBlock(
                    Properties.ofFullCopy(RIBBED_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> RIBBED_ALIEN_WALL_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "ribbed_alien_wall", () -> new BlockItem(RIBBED_ALIEN_WALL.get(), new Item.Properties()));
    public static final Supplier<WallBlock> ROUGH_ALIEN_WALL = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "rough_alien_wall",
            () -> new WallBlock(
                    Properties.ofFullCopy(ROUGH_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> ROUGH_ALIEN_WALL_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "rough_alien_wall", () -> new BlockItem(ROUGH_ALIEN_WALL.get(), new Item.Properties()));
    public static final Supplier<WallBlock> SINOUS_ALIEN_WALL = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "sinous_alien_wall",
            () -> new WallBlock(
                    Properties.ofFullCopy(SINOUS_ALIEN_BLOCK.get())));
    public static final Supplier<BlockItem> SINOUS_ALIEN_WALL_ITEM = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "sinous_alien_wall", () -> new BlockItem(SINOUS_ALIEN_WALL.get(), new Item.Properties()));
    /*
     * FLUID BLOCKS
     */
    private static final Properties replaceCheck = CommonMod.config.blackfuildNonrepacle ? Properties.of().mapColor(
            MapColor.WATER).noCollission().strength(100.0F).noLootTable().liquid().pushReaction(
            PushReaction.DESTROY) : Properties.of().mapColor(
            MapColor.WATER).replaceable().noCollission().strength(100.0F).noLootTable().liquid().pushReaction(
            PushReaction.DESTROY);
    public static final Supplier<LiquidBlock> BLACK_FLUID = CommonBlockRegistryInterface.registerBlock(
            CommonMod.MOD_ID,
            "black_fluid",
            () -> new GigLiquidBlock(GigFluids.BLACK_FLUID_STILL.get(), replaceCheck));

    public static void initialize() {
    }
}
