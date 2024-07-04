package mods.cybercat.gigeresque.common.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import mod.azure.azurelib.sblforked.SBLConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record GigMemoryTypes() {

    public static final Supplier<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEARBY_LIGHT_BLOCKS = register("nearby_light_blocks");
    public static final Supplier<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEARBY_NEST_BLOCKS = register("nearby_nest_blocks");
    public static final Supplier<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEARBY_REPELLENT_BLOCKS = register("nearby_repellent_blocks");
    public static final Supplier<MemoryModuleType<List<ItemEntity>>> FOOD_ITEMS = register("food_items");

    private static <T> Supplier<MemoryModuleType<T>> register(String id) {
        return register(id, null);
    }

    private static <T> Supplier<MemoryModuleType<T>> register(String id, @Nullable Codec<T> codec) {
        return SBLConstants.SBL_LOADER.registerMemoryType(id, Optional.ofNullable(codec));
    }

    public static void initialize() {
    }

}
