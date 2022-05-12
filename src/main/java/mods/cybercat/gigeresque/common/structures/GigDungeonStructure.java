package mods.cybercat.gigeresque.common.structures;

import java.util.Optional;
import java.util.SplittableRandom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class GigDungeonStructure extends StructureFeature<StructurePoolFeatureConfig> {

	public static final Codec<StructurePoolFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
			.group((StructurePool.REGISTRY_CODEC.fieldOf("start_pool"))
					.forGetter(StructurePoolFeatureConfig::getStartPool),
					(Codec.intRange(0, 100).fieldOf("size")).forGetter(StructurePoolFeatureConfig::getSize))
			.apply(instance, StructurePoolFeatureConfig::new));

	public GigDungeonStructure() {
		super(CODEC, GigDungeonStructure::createPiecesGenerator, PostPlacementProcessor.EMPTY);
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
		SplittableRandom random = new SplittableRandom();
		int var = random.nextInt(-28, 0);
		BlockPos blockpos = new BlockPos(context.chunkPos().getStartX(), var, context.chunkPos().getStartZ());
		Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> structurePiecesGenerator = StructurePoolBasedGenerator
				.generate(context, PoolStructurePiece::new, blockpos, false, false);
		return structurePiecesGenerator;
	}
}