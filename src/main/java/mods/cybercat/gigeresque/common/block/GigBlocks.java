package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.storage.*;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

public record GigBlocks() implements GigeresqueInitializer {

    public static final SporeBlock SPORE_BLOCK = new SporeBlock();
    public static final PetrifiedObjectBlock PETRIFIED_OBJECT_BLOCK = new PetrifiedObjectBlock();
    public static final AlienSarcophagusInvisBlock ALIEN_STORAGE_BLOCK_INVIS = new AlienSarcophagusInvisBlock();

    /*
     * NORMAL BLOCKS
     */
    public static final SittingIdolInvisBlock ALIEN_STORAGE_BLOCK_INVIS2 = new SittingIdolInvisBlock();
    public static final AlienSarcophagusBlock ALIEN_STORAGE_BLOCK_1 = new AlienSarcophagusBlock();
    public static final AlienSarcophagusHuggerBlock ALIEN_STORAGE_BLOCK_1_HUGGER = new AlienSarcophagusHuggerBlock();
    public static final AlienSarcophagusGooBlock ALIEN_STORAGE_BLOCK_1_GOO = new AlienSarcophagusGooBlock();
    public static final AlienSarcophagusSporeBlock ALIEN_STORAGE_BLOCK_1_SPORE = new AlienSarcophagusSporeBlock();
    public static final AlienJarBlock ALIEN_STORAGE_BLOCK_2 = new AlienJarBlock();
    public static final SittingIdolBlock ALIEN_STORAGE_BLOCK_3 = new SittingIdolBlock();
    public static final NestResinBlock NEST_RESIN = new NestResinBlock(
            FabricBlockSettings.create().sounds(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f));
    public static final Block NEST_RESIN_BLOCK = new GigBlock(
            FabricBlockSettings.create().sounds(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f));
    public static final NestResinWebBlock NEST_RESIN_WEB = new NestResinWebBlock(
            FabricBlockSettings.create().sounds(SoundType.HONEY_BLOCK).noCollision().strength(5.0f, 8.0f));
    public static final NestResinWebFullBlock NEST_RESIN_WEB_CROSS = new NestResinWebFullBlock(
            FabricBlockSettings.create().sounds(
                    SoundType.HONEY_BLOCK).noCollision().nonOpaque().requiresTool().strength(5.0f, 8.0f));
    public static final Block ORGANIC_ALIEN_BLOCK = new GigBlock(
            FabricBlockSettings.create().strength(Gigeresque.config.alienblockHardness,
                    Gigeresque.config.alienblockResistance).sounds(SoundType.NETHERRACK).explosionResistance(10));
    public static final Block RESINOUS_ALIEN_BLOCK = new GigBlock(
            FabricBlockSettings.create().requiresTool().strength(Gigeresque.config.alienblockHardness,
                    Gigeresque.config.alienblockResistance).sounds(SoundType.DEEPSLATE).explosionResistance(10));
    public static final Block RIBBED_ALIEN_BLOCK = new GigBlock(
            FabricBlockSettings.create().requiresTool().strength(Gigeresque.config.alienblockHardness,
                    Gigeresque.config.alienblockResistance).sounds(SoundType.DEEPSLATE).explosionResistance(10));
    public static final Block ROUGH_ALIEN_BLOCK = new GigBlock(
            FabricBlockSettings.create().requiresTool().strength(Gigeresque.config.alienblockHardness,
                    Gigeresque.config.alienblockResistance).sounds(SoundType.DEEPSLATE).explosionResistance(10));
    public static final Block SINOUS_ALIEN_BLOCK = new GigBlock(
            FabricBlockSettings.create().requiresTool().strength(Gigeresque.config.alienblockHardness,
                    Gigeresque.config.alienblockResistance).sounds(SoundType.DEEPSLATE).explosionResistance(10));
    public static final Block MURAL_ALIEN_BLOCK_1 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_2 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_3 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_4 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_5 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_6 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_7 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_8 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_9 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_10 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_11 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_12 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_13 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_14 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_15 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_16 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_17 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_18 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_19 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_20 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_21 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_22 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_23 = new MuralBlock();
    public static final Block MURAL_ALIEN_BLOCK_24 = new MuralBlock();
    /*
     * PILLARS
     */
    public static final RotatedPillarBlock RESINOUS_ALIEN_PILLAR = new RotatedPillarBlock(
            ofFullCopy(ROUGH_ALIEN_BLOCK));
    public static final RotatedPillarBlock RIBBED_ALIEN_PILLAR = new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(ROUGH_ALIEN_BLOCK));
    public static final CustomStairsBlock RIBBED_ALIEN_STAIRS = new CustomStairsBlock(RIBBED_ALIEN_PILLAR);
    public static final RotatedPillarBlock SMOOTH_ALIEN_PILLAR = new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(SINOUS_ALIEN_BLOCK));
    public static final CustomStairsBlock SMOOTH_ALIEN_STAIRS = new CustomStairsBlock(SMOOTH_ALIEN_PILLAR);
    /*
     * SLABS
     */
    public static final SlabBlock ORGANIC_ALIEN_SLAB = new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(ORGANIC_ALIEN_BLOCK));
    public static final SlabBlock RESINOUS_ALIEN_SLAB = new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(RESINOUS_ALIEN_BLOCK));
    public static final SlabBlock RIBBED_ALIEN_SLAB = new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(RIBBED_ALIEN_BLOCK));
    public static final SlabBlock ROUGH_ALIEN_SLAB = new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(ROUGH_ALIEN_BLOCK));
    public static final SlabBlock SINOUS_ALIEN_SLAB = new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(SINOUS_ALIEN_BLOCK));
    /*
     * STAIRS
     */
    public static final CustomStairsBlock ORGANIC_ALIEN_STAIRS = new CustomStairsBlock(ORGANIC_ALIEN_BLOCK);
    public static final CustomStairsBlock RESINOUS_ALIEN_STAIRS = new CustomStairsBlock(RESINOUS_ALIEN_BLOCK);
    public static final CustomStairsBlock ROUGH_ALIEN_STAIRS = new CustomStairsBlock(ROUGH_ALIEN_BLOCK);
    public static final CustomStairsBlock SINOUS_ALIEN_STAIRS = new CustomStairsBlock(SINOUS_ALIEN_BLOCK);
    /*
     * WALLS
     */
    public static final WallBlock ORGANIC_ALIEN_WALL = new WallBlock(
            BlockBehaviour.Properties.ofFullCopy(ORGANIC_ALIEN_BLOCK));
    public static final WallBlock RESINOUS_ALIEN_WALL = new WallBlock(
            BlockBehaviour.Properties.ofFullCopy(RESINOUS_ALIEN_BLOCK));
    public static final WallBlock RIBBED_ALIEN_WALL = new WallBlock(
            BlockBehaviour.Properties.ofFullCopy(RIBBED_ALIEN_BLOCK));
    public static final WallBlock ROUGH_ALIEN_WALL = new WallBlock(
            BlockBehaviour.Properties.ofFullCopy(ROUGH_ALIEN_BLOCK));
    public static final WallBlock SINOUS_ALIEN_WALL = new WallBlock(
            BlockBehaviour.Properties.ofFullCopy(SINOUS_ALIEN_BLOCK));
    /*
     * FLUID BLOCKS
     */
    private static final Properties replaceCheck = Gigeresque.config.blackfuildNonrepacle ? BlockBehaviour.Properties.of().mapColor(
            MapColor.WATER).noCollission().strength(100.0F).noLootTable().liquid().pushReaction(
            PushReaction.DESTROY) : BlockBehaviour.Properties.of().mapColor(
            MapColor.WATER).replaceable().noCollission().strength(100.0F).noLootTable().liquid().pushReaction(
            PushReaction.DESTROY);
    public static final LiquidBlock BLACK_FLUID = new LiquidBlock(GigFluids.BLACK_FLUID_STILL, replaceCheck) {
    };
    private static GigBlocks instance;

    public static synchronized GigBlocks getInstance() {
        if (instance == null) instance = new GigBlocks();
        return instance;
    }

    private void registerItemBlock(String path, Block block, FabricItemSettings settings) {
        Registry.register(BuiltInRegistries.BLOCK, Constants.modResource(path), block);
        Registry.register(BuiltInRegistries.ITEM, Constants.modResource(path), new BlockItem(block, settings));
    }

    private void registerItemBlock(String path, Block block) {
        registerItemBlock(path, block, new FabricItemSettings());
    }

    @Override
    public void initialize() {
        Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("black_fluid"), BLACK_FLUID);
        Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("alien_storage_invis"),
                ALIEN_STORAGE_BLOCK_INVIS);
        Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("alien_storage_invis2"),
                ALIEN_STORAGE_BLOCK_INVIS2);

        registerItemBlock("nest_resin", NEST_RESIN);
        registerItemBlock("nest_resin_block", NEST_RESIN_BLOCK);

        registerItemBlock("nest_resin_web", NEST_RESIN_WEB);
        registerItemBlock("nest_resin_web_cross", NEST_RESIN_WEB_CROSS);

        Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("neomorph_spore_pods"), SPORE_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, Constants.modResource("neomorph_spore_pods"),
                new GigBlockItem(SPORE_BLOCK, new FabricItemSettings()));

        registerItemBlock("petrified_object", PETRIFIED_OBJECT_BLOCK);
        registerItemBlock("alien_storage_block1", ALIEN_STORAGE_BLOCK_1);
        registerItemBlock("alien_storage_block1_hugger", ALIEN_STORAGE_BLOCK_1_HUGGER);
        registerItemBlock("alien_storage_block1_goo", ALIEN_STORAGE_BLOCK_1_GOO);
        registerItemBlock("alien_storage_block1_spore", ALIEN_STORAGE_BLOCK_1_SPORE);
        registerItemBlock("alien_storage_block2", ALIEN_STORAGE_BLOCK_2);
        registerItemBlock("alien_storage_block3", ALIEN_STORAGE_BLOCK_3);
//		registerItemBlock("alien_storage_block4", ALIEN_STORAGE_BLOCK_4);
//		registerItemBlock("alien_storage_block5", ALIEN_STORAGE_BLOCK_5);

        registerItemBlock("organic_alien_block", ORGANIC_ALIEN_BLOCK);
        registerItemBlock("resinous_alien_block", RESINOUS_ALIEN_BLOCK);
        registerItemBlock("ribbed_alien_block", RIBBED_ALIEN_BLOCK);
        registerItemBlock("rough_alien_block", ROUGH_ALIEN_BLOCK);
        registerItemBlock("sinous_alien_block", SINOUS_ALIEN_BLOCK);

        registerItemBlock("mural_block_1", MURAL_ALIEN_BLOCK_1);
        registerItemBlock("mural_block_2", MURAL_ALIEN_BLOCK_2);
        registerItemBlock("mural_block_3", MURAL_ALIEN_BLOCK_3);
        registerItemBlock("mural_block_4", MURAL_ALIEN_BLOCK_4);
        registerItemBlock("mural_block_5", MURAL_ALIEN_BLOCK_5);
        registerItemBlock("mural_block_6", MURAL_ALIEN_BLOCK_6);
        registerItemBlock("mural_block_7", MURAL_ALIEN_BLOCK_7);
        registerItemBlock("mural_block_8", MURAL_ALIEN_BLOCK_8);
        registerItemBlock("mural_block_9", MURAL_ALIEN_BLOCK_9);
        registerItemBlock("mural_block_10", MURAL_ALIEN_BLOCK_10);
        registerItemBlock("mural_block_11", MURAL_ALIEN_BLOCK_11);
        registerItemBlock("mural_block_12", MURAL_ALIEN_BLOCK_12);
        registerItemBlock("mural_block_13", MURAL_ALIEN_BLOCK_13);
        registerItemBlock("mural_block_14", MURAL_ALIEN_BLOCK_14);
        registerItemBlock("mural_block_15", MURAL_ALIEN_BLOCK_15);
        registerItemBlock("mural_block_16", MURAL_ALIEN_BLOCK_16);
        registerItemBlock("mural_block_17", MURAL_ALIEN_BLOCK_17);
        registerItemBlock("mural_block_18", MURAL_ALIEN_BLOCK_18);
        registerItemBlock("mural_block_19", MURAL_ALIEN_BLOCK_19);
        registerItemBlock("mural_block_20", MURAL_ALIEN_BLOCK_20);
        registerItemBlock("mural_block_21", MURAL_ALIEN_BLOCK_21);
        registerItemBlock("mural_block_22", MURAL_ALIEN_BLOCK_22);
        registerItemBlock("mural_block_23", MURAL_ALIEN_BLOCK_23);
        registerItemBlock("mural_block_24", MURAL_ALIEN_BLOCK_24);

        registerItemBlock("resinous_alien_pillar", RESINOUS_ALIEN_PILLAR);
        registerItemBlock("ribbed_alien_pillar", RIBBED_ALIEN_PILLAR);
        registerItemBlock("smooth_alien_pillar", SMOOTH_ALIEN_PILLAR);

        registerItemBlock("organic_alien_slab", ORGANIC_ALIEN_SLAB);
        registerItemBlock("resinous_alien_slab", RESINOUS_ALIEN_SLAB);
        registerItemBlock("ribbed_alien_slab", RIBBED_ALIEN_SLAB);
        registerItemBlock("rough_alien_slab", ROUGH_ALIEN_SLAB);
        registerItemBlock("sinous_alien_slab", SINOUS_ALIEN_SLAB);

        registerItemBlock("organic_alien_stairs", ORGANIC_ALIEN_STAIRS);
        registerItemBlock("resinous_alien_stairs", RESINOUS_ALIEN_STAIRS);
        registerItemBlock("ribbed_alien_stairs", RIBBED_ALIEN_STAIRS);
        registerItemBlock("rough_alien_stairs", ROUGH_ALIEN_STAIRS);
        registerItemBlock("smooth_alien_stairs", SMOOTH_ALIEN_STAIRS);
        registerItemBlock("sinous_alien_stairs", SINOUS_ALIEN_STAIRS);

        registerItemBlock("organic_alien_wall", ORGANIC_ALIEN_WALL);
        registerItemBlock("resinous_alien_wall", RESINOUS_ALIEN_WALL);
        registerItemBlock("ribbed_alien_wall", RIBBED_ALIEN_WALL);
        registerItemBlock("rough_alien_wall", ROUGH_ALIEN_WALL);
        registerItemBlock("sinous_alien_wall", SINOUS_ALIEN_WALL);
    }
}
