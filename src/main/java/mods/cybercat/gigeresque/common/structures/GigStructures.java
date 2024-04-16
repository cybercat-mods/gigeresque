package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.Codec;
import mods.cybercat.gigeresque.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public record GigStructures() {
    public static StructureType<?> GIG_DUNGEON;
    public static StructureType<?> SCULK_GIG_DUNGEON;

    public static void initialize() {
        GIG_DUNGEON = register(Constants.modResource("gig_dungeon"), GigDungeonStructure.CODEC);
        SCULK_GIG_DUNGEON = register(Constants.modResource("sculk_gig_dungeon"), GigSculkDungeonStructure.CODEC);
    }

    private static <S extends Structure> StructureType<S> register(ResourceLocation id, Codec<S> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, id, () -> codec);
    }
}
