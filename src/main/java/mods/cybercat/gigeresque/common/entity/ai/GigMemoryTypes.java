package mods.cybercat.gigeresque.common.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.SBLConstants;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public record GigMemoryTypes() implements GigeresqueInitializer {

    public static final Supplier<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEARBY_LIGHT_BLOCKS = register("nearby_light_blocks");
    public static final Supplier<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEARBY_NEST_BLOCKS = register("nearby_nest_blocks");
    public static final Supplier<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEARBY_REPELLENT_BLOCKS = register("nearby_repellent_blocks");
    public static final Supplier<MemoryModuleType<List<ItemEntity>>> FOOD_ITEMS = register("food_items");
    private static GigMemoryTypes instance;

    public static synchronized GigMemoryTypes getInstance() {
        if (instance == null) {
            instance = new GigMemoryTypes();
        }
        return instance;
    }

    private static <T> Supplier<MemoryModuleType<T>> register(String id) {
        return register(id, null);
    }

    private static <T> Supplier<MemoryModuleType<T>> register(String id, @Nullable Codec<T> codec) {
        return SBLConstants.SBL_LOADER.registerMemoryType(id, codec);
    }

    @Override
    public void initialize() {
        /*
        Fine to leave empty
         */
    }

}
