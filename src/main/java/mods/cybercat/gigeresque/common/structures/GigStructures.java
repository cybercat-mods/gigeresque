package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.Codec;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class GigStructures {
	public static StructureType<?> GIG_DUNGEON;

	public static void registerStructureFeatures() {
		GIG_DUNGEON = register(new ResourceLocation(Gigeresque.MOD_ID, "gig_dungeon"), GigDungeonStructure.CODEC);
	}

	private static <S extends Structure> StructureType<S> register(ResourceLocation id, Codec<S> codec) {
		return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, id, () -> codec);
	}
}
