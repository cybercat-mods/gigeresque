package com.bvanseg.gigeresque.common.block;

import com.bvanseg.gigeresque.common.Gigeresque;
import com.bvanseg.gigeresque.common.block.material.Materials;
import com.bvanseg.gigeresque.common.fluid.Fluids;
import com.bvanseg.gigeresque.common.item.group.ItemGroups;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks implements GigeresqueInitializer {
    private Blocks() {}
    private static Blocks instance;

    synchronized public static Blocks getInstance() {
        if (instance == null)
            instance = new Blocks();
        return instance;
    }

    /*
        FLUID BLOCKS
     */
    public static final FluidBlock BLACK_FLUID = new FluidBlock(
            Fluids.BLACK_FLUID_STILL,
            AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0F).dropsNothing()
    ) {
    };

    /*
        NORMAL BLOCKS
     */

    public static final AcidBlock ACID_BLOCK = new AcidBlock(FabricBlockSettings.of(Materials.ACID).noCollision());

    public static final NestResinBlock NEST_RESIN = new
            NestResinBlock(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(BlockSoundGroup.HONEY).strength(5.0f, 8.0f));
    public static final Block NEST_RESIN_BLOCK = new
            Block(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(BlockSoundGroup.HONEY).strength(5.0f, 8.0f));
    public static final NestResinWebBlock NEST_RESIN_WEB = new
            NestResinWebBlock(
            FabricBlockSettings.of(Materials.NEST_RESIN).sounds(BlockSoundGroup.HONEY).noCollision()
                    .strength(5.0f, 8.0f)
    );
    public static final NestResinWebFullBlock NEST_RESIN_WEB_CROSS = new
            NestResinWebFullBlock(
            FabricBlockSettings.of(Materials.NEST_RESIN_WEB).sounds(BlockSoundGroup.HONEY).noCollision().nonOpaque()
                    .requiresTool().strength(5.0f, 8.0f)
    );

    private static final Block ORGANIC_ALIEN_BLOCK = new Block(FabricBlockSettings.of(Materials.ORGANIC_ALIEN_BLOCK).strength(3.0F, 6.0F).sounds(BlockSoundGroup.NETHERRACK));
    private static final Block RESINOUS_ALIEN_BLOCK = new Block(FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool().strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE));
    private static final Block RIBBED_ALIEN_BLOCK = new Block(
            FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool()
                    .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    );
    private static final Block ROUGH_ALIEN_BLOCK = new Block(
            FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool()
                    .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    );
    private static final Block SINOUS_ALIEN_BLOCK = new Block(
            FabricBlockSettings.of(Materials.SINOUS_ALIEN_BLOCK).requiresTool()
                    .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    );

    /*
        PILLARS
     */
    private static final PillarBlock RESINOUS_ALIEN_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK));
    private static final PillarBlock RIBBED_ALIEN_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK));
    private static final PillarBlock SMOOTH_ALIEN_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(SINOUS_ALIEN_BLOCK));

    /*
        SLABS
     */
    private static final SlabBlock ORGANIC_ALIEN_SLAB = new SlabBlock(AbstractBlock.Settings.copy(ORGANIC_ALIEN_BLOCK));
    private static final SlabBlock RESINOUS_ALIEN_SLAB = new SlabBlock(AbstractBlock.Settings.copy(RESINOUS_ALIEN_BLOCK));
    private static final SlabBlock RIBBED_ALIEN_SLAB = new SlabBlock(AbstractBlock.Settings.copy(RIBBED_ALIEN_BLOCK));
    private static final SlabBlock ROUGH_ALIEN_SLAB = new SlabBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK));
    private static final SlabBlock SINOUS_ALIEN_SLAB = new SlabBlock(AbstractBlock.Settings.copy(SINOUS_ALIEN_BLOCK));

    /*
        STAIRS
     */
    private static final CustomStairsBlock ORGANIC_ALIEN_STAIRS = new CustomStairsBlock(ORGANIC_ALIEN_BLOCK);
    private static final CustomStairsBlock RESINOUS_ALIEN_STAIRS = new CustomStairsBlock(RESINOUS_ALIEN_BLOCK);
    private static final CustomStairsBlock RIBBED_ALIEN_STAIRS = new CustomStairsBlock(RIBBED_ALIEN_PILLAR);
    private static final CustomStairsBlock ROUGH_ALIEN_STAIRS = new CustomStairsBlock(ROUGH_ALIEN_BLOCK);
    private static final CustomStairsBlock SINOUS_ALIEN_STAIRS = new CustomStairsBlock(SINOUS_ALIEN_BLOCK);
    private static final CustomStairsBlock SMOOTH_ALIEN_STAIRS = new CustomStairsBlock(SMOOTH_ALIEN_PILLAR);

    /*
        WALLS
     */
    private static final WallBlock ORGANIC_ALIEN_WALL = new WallBlock(AbstractBlock.Settings.copy(ORGANIC_ALIEN_BLOCK));
    private static final WallBlock RESINOUS_ALIEN_WALL = new WallBlock(AbstractBlock.Settings.copy(RESINOUS_ALIEN_BLOCK));
    private static final WallBlock RIBBED_ALIEN_WALL = new WallBlock(AbstractBlock.Settings.copy(RIBBED_ALIEN_BLOCK));
    private static final WallBlock ROUGH_ALIEN_WALL = new WallBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK));
    private static final WallBlock SINOUS_ALIEN_WALL = new WallBlock(AbstractBlock.Settings.copy(SINOUS_ALIEN_BLOCK));

    private void registerItemBlock(String path, Block block, FabricItemSettings settings) {
        Registry.register(Registry.BLOCK, new Identifier(Gigeresque.MOD_ID, path), block);
        Registry.register(Registry.ITEM, new Identifier(Gigeresque.MOD_ID, path), new BlockItem(block, settings.group(ItemGroups.GENERAL)));
    }

    private void registerItemBlock(String path, Block block) {
        registerItemBlock(path, block, new FabricItemSettings().group(ItemGroup.MISC));
    }

    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("Blocks", this::initializeImpl);
    }

    private void initializeImpl() {
        Registry.register(Registry.BLOCK, new Identifier(Gigeresque.MOD_ID, "black_fluid"), BLACK_FLUID);

        registerItemBlock("nest_resin", NEST_RESIN);
        registerItemBlock("nest_resin_block", NEST_RESIN_BLOCK);

        registerItemBlock("nest_resin_web", NEST_RESIN_WEB);
        registerItemBlock("nest_resin_web_cross", NEST_RESIN_WEB_CROSS);

        // Flammability;
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_BLOCK, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_WEB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_WEB_CROSS, 5, 5);

        Registry.register(Registry.BLOCK, new Identifier(Gigeresque.MOD_ID, "acid_block"), ACID_BLOCK);

        registerItemBlock("organic_alien_block", ORGANIC_ALIEN_BLOCK);
        registerItemBlock("resinous_alien_block", RESINOUS_ALIEN_BLOCK);
        registerItemBlock("ribbed_alien_block", RIBBED_ALIEN_BLOCK);
        registerItemBlock("rough_alien_block", ROUGH_ALIEN_BLOCK);
        registerItemBlock("sinous_alien_block", SINOUS_ALIEN_BLOCK);

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
