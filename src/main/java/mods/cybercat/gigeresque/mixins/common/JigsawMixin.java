package mods.cybercat.gigeresque.mixins.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.gen.structure.JigsawStructure;

@Mixin(JigsawStructure.class)
public class JigsawMixin {

//	@SuppressWarnings("unchecked")
//	@Shadow
//	public static final Codec<JigsawStructure> CODEC = (Codec<JigsawStructure>) RecordCodecBuilder.mapCodec(instance -> instance.group(
//			JigsawStructure.configCodecBuilder(instance),
//			(StructurePool.REGISTRY_CODEC.fieldOf("start_pool")).forGetter(structure -> structure.startPool),
//			Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
//			(Codec.intRange(0, 7).fieldOf("size")).forGetter(structure -> structure.size),
//			(HeightProvider.CODEC.fieldOf("start_height")).forGetter(structure -> structure.startHeight),
//			(Codec.BOOL.fieldOf("use_expansion_hack")).forGetter(structure -> structure.useExpansionHack),
//			Heightmap.Type.CODEC.optionalFieldOf("project_start_to_heightmap")
//					.forGetter(structure -> structure.projectStartToHeightmap),
//			(Codec.intRange(1, 128).fieldOf("max_distance_from_center"))
//					.forGetter(structure -> structure.maxDistanceFromCenter))
//			.apply((Applicative<JigsawStructure, ?>) instance, JigsawStructure::new))
//			.flatXmap(JigsawStructure.createValidator(), JigsawStructure.createValidator()).codec());

}
