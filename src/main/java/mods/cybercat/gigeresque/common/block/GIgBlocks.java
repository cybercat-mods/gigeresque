package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.material.Materials;
import mods.cybercat.gigeresque.common.block.storage.AlienJarBlock;
import mods.cybercat.gigeresque.common.block.storage.AlienSarcophagusBlock;
import mods.cybercat.gigeresque.common.block.storage.AlienSarcophagusInvisBlock;
import mods.cybercat.gigeresque.common.block.storage.SittingIdolBlock;
import mods.cybercat.gigeresque.common.block.storage.SittingIdolInvisBlock;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class GIgBlocks implements GigeresqueInitializer {
	private GIgBlocks() {
	}

	private static GIgBlocks instance;

	synchronized public static GIgBlocks getInstance() {
		if (instance == null)
			instance = new GIgBlocks();
		return instance;
	}

	/*
	 * FLUID BLOCKS
	 */
	public static final LiquidBlock BLACK_FLUID = new LiquidBlock(GigFluids.BLACK_FLUID_STILL, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noLootTable()) {
	};

	/*
	 * NORMAL BLOCKS
	 */

	public static final AcidBlock ACID_BLOCK = new AcidBlock(FabricBlockSettings.of(Materials.ACID).nonOpaque().noLootTable());

	public static final AlienSarcophagusInvisBlock ALIEN_STORAGE_BLOCK_INVIS = new AlienSarcophagusInvisBlock();
	public static final SittingIdolInvisBlock ALIEN_STORAGE_BLOCK_INVIS2 = new SittingIdolInvisBlock();

	public static final AlienSarcophagusBlock ALIEN_STORAGE_BLOCK_1 = new AlienSarcophagusBlock();
	public static final AlienJarBlock ALIEN_STORAGE_BLOCK_2 = new AlienJarBlock();
	public static final SittingIdolBlock ALIEN_STORAGE_BLOCK_3 = new SittingIdolBlock();
//	public static final AlienJarBlock ALIEN_STORAGE_BLOCK_4 = new AlienJarBlock();
//	public static final AlienJarBlock ALIEN_STORAGE_BLOCK_5 = new AlienJarBlock();

	public static final NestResinBlock NEST_RESIN = new NestResinBlock(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f));
	public static final Block NEST_RESIN_BLOCK = new GigBlock(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(SoundType.HONEY_BLOCK).strength(5.0f, 8.0f));
	public static final NestResinWebBlock NEST_RESIN_WEB = new NestResinWebBlock(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(SoundType.HONEY_BLOCK).noCollision().strength(5.0f, 8.0f));
	public static final NestResinWebFullBlock NEST_RESIN_WEB_CROSS = new NestResinWebFullBlock(FabricBlockSettings.of(Materials.NEST_RESIN_WEB).sounds(SoundType.HONEY_BLOCK).noCollision().nonOpaque().requiresTool().strength(5.0f, 8.0f));

	public static final Block ORGANIC_ALIEN_BLOCK = new GigBlock(FabricBlockSettings.of(Materials.ORGANIC_ALIEN_BLOCK).strength(3.0F, 6.0F).sounds(SoundType.NETHERRACK));
	public static final Block RESINOUS_ALIEN_BLOCK = new GigBlock(FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool().strength(3.0F, 6.0F).sounds(SoundType.DEEPSLATE));
	public static final Block RIBBED_ALIEN_BLOCK = new GigBlock(FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool().strength(3.0F, 6.0F).sounds(SoundType.DEEPSLATE));
	public static final Block ROUGH_ALIEN_BLOCK = new GigBlock(FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool().strength(3.0F, 6.0F).sounds(SoundType.DEEPSLATE));
	public static final Block SINOUS_ALIEN_BLOCK = new GigBlock(FabricBlockSettings.of(Materials.SINOUS_ALIEN_BLOCK).requiresTool().strength(3.0F, 6.0F).sounds(SoundType.DEEPSLATE));

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
	public static final RotatedPillarBlock RESINOUS_ALIEN_PILLAR = new RotatedPillarBlock(BlockBehaviour.Properties.copy(ROUGH_ALIEN_BLOCK));
	public static final RotatedPillarBlock RIBBED_ALIEN_PILLAR = new RotatedPillarBlock(BlockBehaviour.Properties.copy(ROUGH_ALIEN_BLOCK));
	public static final RotatedPillarBlock SMOOTH_ALIEN_PILLAR = new RotatedPillarBlock(BlockBehaviour.Properties.copy(SINOUS_ALIEN_BLOCK));

	/*
	 * SLABS
	 */
	public static final SlabBlock ORGANIC_ALIEN_SLAB = new SlabBlock(BlockBehaviour.Properties.copy(ORGANIC_ALIEN_BLOCK));
	public static final SlabBlock RESINOUS_ALIEN_SLAB = new SlabBlock(BlockBehaviour.Properties.copy(RESINOUS_ALIEN_BLOCK));
	public static final SlabBlock RIBBED_ALIEN_SLAB = new SlabBlock(BlockBehaviour.Properties.copy(RIBBED_ALIEN_BLOCK));
	public static final SlabBlock ROUGH_ALIEN_SLAB = new SlabBlock(BlockBehaviour.Properties.copy(ROUGH_ALIEN_BLOCK));
	public static final SlabBlock SINOUS_ALIEN_SLAB = new SlabBlock(BlockBehaviour.Properties.copy(SINOUS_ALIEN_BLOCK));

	/*
	 * STAIRS
	 */
	public static final CustomStairsBlock ORGANIC_ALIEN_STAIRS = new CustomStairsBlock(ORGANIC_ALIEN_BLOCK);
	public static final CustomStairsBlock RESINOUS_ALIEN_STAIRS = new CustomStairsBlock(RESINOUS_ALIEN_BLOCK);
	public static final CustomStairsBlock RIBBED_ALIEN_STAIRS = new CustomStairsBlock(RIBBED_ALIEN_PILLAR);
	public static final CustomStairsBlock ROUGH_ALIEN_STAIRS = new CustomStairsBlock(ROUGH_ALIEN_BLOCK);
	public static final CustomStairsBlock SINOUS_ALIEN_STAIRS = new CustomStairsBlock(SINOUS_ALIEN_BLOCK);
	public static final CustomStairsBlock SMOOTH_ALIEN_STAIRS = new CustomStairsBlock(SMOOTH_ALIEN_PILLAR);

	/*
	 * WALLS
	 */
	public static final WallBlock ORGANIC_ALIEN_WALL = new WallBlock(BlockBehaviour.Properties.copy(ORGANIC_ALIEN_BLOCK));
	public static final WallBlock RESINOUS_ALIEN_WALL = new WallBlock(BlockBehaviour.Properties.copy(RESINOUS_ALIEN_BLOCK));
	public static final WallBlock RIBBED_ALIEN_WALL = new WallBlock(BlockBehaviour.Properties.copy(RIBBED_ALIEN_BLOCK));
	public static final WallBlock ROUGH_ALIEN_WALL = new WallBlock(BlockBehaviour.Properties.copy(ROUGH_ALIEN_BLOCK));
	public static final WallBlock SINOUS_ALIEN_WALL = new WallBlock(BlockBehaviour.Properties.copy(SINOUS_ALIEN_BLOCK));

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
		Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("alien_storage_invis"), ALIEN_STORAGE_BLOCK_INVIS);
		Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("alien_storage_invis2"), ALIEN_STORAGE_BLOCK_INVIS2);

		registerItemBlock("nest_resin", NEST_RESIN);
		registerItemBlock("nest_resin_block", NEST_RESIN_BLOCK);

		registerItemBlock("nest_resin_web", NEST_RESIN_WEB);
		registerItemBlock("nest_resin_web_cross", NEST_RESIN_WEB_CROSS);

		// Flammability;
		FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_BLOCK, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_WEB, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_WEB_CROSS, 5, 5);

		Registry.register(BuiltInRegistries.BLOCK, Constants.modResource("acid_block"), ACID_BLOCK);
		registerItemBlock("alien_storage_block1", ALIEN_STORAGE_BLOCK_1);
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
