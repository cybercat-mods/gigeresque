package mods.cybercat.gigeresque.common.structures;

import java.util.Optional;
import java.util.SplittableRandom;

import mods.cybercat.gigeresque.common.Gigeresque;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class GigDungeonStructure extends StructureFeature<StructurePoolFeatureConfig> {

	public GigDungeonStructure(Codec<StructurePoolFeatureConfig> codec) {
		super(codec, GigDungeonStructure::createPiecesGenerator, PostPlacementProcessor.EMPTY);
	}

	private static boolean isFeatureChunk(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
		BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
		int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(),
				Heightmap.Type.WORLD_SURFACE_WG, context.world());
		VerticalBlockSample columnOfBlocks = context.chunkGenerator().getColumnSample(spawnXZPosition.getX(),
				spawnXZPosition.getZ(), context.world());
		BlockState topBlock = columnOfBlocks.getState(landHeight);
		return topBlock.getFluidState().isEmpty();
	}

	public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(
			StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {

		if (!GigDungeonStructure.isFeatureChunk(context)) {
			return Optional.empty();
		}

		StructurePoolFeatureConfig newConfig = new StructurePoolFeatureConfig(() -> context.registryManager()
				.get(Registry.STRUCTURE_POOL_KEY).get(new Identifier(Gigeresque.MOD_ID, "gig_dungeon/start_pool")), 10);

		StructureGeneratorFactory.Context<StructurePoolFeatureConfig> newContext = new StructureGeneratorFactory.Context<>(
				context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), newConfig,
				context.world(), context.validBiome(), context.structureManager(), context.registryManager());

		SplittableRandom random = new SplittableRandom();
		int var = random.nextInt(-64, -12);
		BlockPos blockpos = new BlockPos(context.chunkPos().getStartX(), var, context.chunkPos().getStartZ());
		Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> structurePiecesGenerator = StructurePoolBasedGenerator
				.generate(newContext, PoolStructurePiece::new, blockpos, false, false);
		return structurePiecesGenerator;
	}
}