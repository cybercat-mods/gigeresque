package mods.cybercat.gigeresque.common.structures;

import java.util.Optional;
import java.util.SplittableRandom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class GigDungeonStructure extends Structure {

	public static final Codec<GigDungeonStructure> CODEC = RecordCodecBuilder
			.<GigDungeonStructure>mapCodec(instance -> instance.group(GigDungeonStructure.configCodecBuilder(instance),
					StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
					Identifier.CODEC.optionalFieldOf("start_jigsaw_name")
							.forGetter(structure -> structure.startJigsawName),
					Codec.intRange(0, 101).fieldOf("size").forGetter(structure -> structure.size),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center")
							.forGetter(structure -> structure.maxDistanceFromCenter))
					.apply(instance, GigDungeonStructure::new))
			.codec();

	private final RegistryEntry<StructurePool> startPool;
	private final Optional<Identifier> startJigsawName;
	private final int size;
	private final int maxDistanceFromCenter;

	public GigDungeonStructure(Structure.Config config, RegistryEntry<StructurePool> startPool,
			Optional<Identifier> startJigsawName, int size, int maxDistanceFromCenter) {
		super(config);
		this.startPool = startPool;
		this.startJigsawName = startJigsawName;
		this.size = size;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}

	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		SplittableRandom random = new SplittableRandom();
		int var = random.nextInt(-28, 0);
		BlockPos blockpos = new BlockPos(context.chunkPos().getStartX(), var, context.chunkPos().getStartZ());

		Optional<StructurePosition> structurePiecesGenerator = StructurePoolBasedGenerator.generate(context,
				this.startPool, this.startJigsawName, this.size, blockpos, false, Optional.empty(),
				this.maxDistanceFromCenter);
		return structurePiecesGenerator;
	}

	@Override
	public StructureType<?> getType() {
		return GigStructures.GIG_DUNGEON;
	}
}