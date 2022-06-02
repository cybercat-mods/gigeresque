package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.Codec;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class GigStructures {
	public static StructureType<?> GIG_DUNGEON;

	public static void registerStructureFeatures() {
		GIG_DUNGEON = register(new Identifier(Gigeresque.MOD_ID, "gig_dungeon"), GigDungeonStructure.CODEC);
	}

	private static <S extends Structure> StructureType<S> register(Identifier id, Codec<S> codec) {
		return Registry.register(Registry.STRUCTURE_TYPE, id, () -> codec);
	}
}
