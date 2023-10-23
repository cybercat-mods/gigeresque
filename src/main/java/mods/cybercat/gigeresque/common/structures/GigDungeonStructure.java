package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;
import java.util.SplittableRandom;

public class GigDungeonStructure extends Structure {

    public static final Codec<GigDungeonStructure> CODEC = RecordCodecBuilder.<GigDungeonStructure>mapCodec(
                    instance -> instance.group(GigDungeonStructure.settingsCodec(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName), Codec.intRange(0, 16).fieldOf("size").forGetter(structure -> structure.size), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)).apply(instance,
                            GigDungeonStructure::new))
            .codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final int maxDistanceFromCenter;

    public GigDungeonStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int size, int maxDistanceFromCenter) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        SplittableRandom random = new SplittableRandom();
        int var = random.nextInt(-18, 0);
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), var, context.chunkPos().getMinBlockZ());

        Optional<GenerationStub> structurePiecesGenerator = JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.size, blockpos, false, Optional.empty(), this.maxDistanceFromCenter);
        return structurePiecesGenerator;
    }

    @Override
    public StructureType<?> type() {
        return GigStructures.GIG_DUNGEON;
    }
}