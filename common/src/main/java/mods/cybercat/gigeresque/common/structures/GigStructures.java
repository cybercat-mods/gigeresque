package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.MapCodec;
import mods.cybercat.gigeresque.CommonMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public record GigStructures() implements CommonStructureRegistryInterface {
    public static final Supplier<StructureType<GigDungeonStructure>> GIG_DUNGEON = CommonStructureRegistryInterface.registerStructure(
            CommonMod.MOD_ID, "gig_dungeon",
            GigDungeonStructure.CODEC);
    public static final Supplier<StructureType<GigSculkDungeonStructure>> SCULK_GIG_DUNGEON = CommonStructureRegistryInterface.registerStructure(
            CommonMod.MOD_ID, "sculk_gig_dungeon",
            GigSculkDungeonStructure.CODEC);

    private static <S extends Structure> StructureType<S> register(ResourceLocation id, MapCodec<S> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, id, () -> codec);
    }

    public static void initialize() {
    }
}
