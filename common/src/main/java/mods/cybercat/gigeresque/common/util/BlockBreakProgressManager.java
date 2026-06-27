package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BlockBreakProgressManager {

    private static final Map<BlockPos, Map.Entry<Long, Float>> BLOCK_BREAK_PROGRESS_MAP = new HashMap<>();

    public static void tick(Level level) {
        var gameTime = level.getGameTime();

        if (gameTime % (20 * 20) != 0) {
            return;
        }

        BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.entrySet().removeIf(entry -> {
            var lastUpdateTimeMillis = entry.getValue().getKey();
            return System.currentTimeMillis() > lastUpdateTimeMillis;
        });
    }

    public static void resetProgress(Level level, BlockPos pos) {
        BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.remove(pos);
        level.destroyBlockProgress(computeBlockPosHash(pos), pos, -1);
    }

    public static void setProgress(Level level, BlockPos pos, float progress) {
        var newEntry = Map.entry(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5), progress);
        BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.put(pos, newEntry);

        var clampedProgress = getClampedProgress(progress);
        level.destroyBlockProgress(computeBlockPosHash(pos), pos, clampedProgress);
    }

    public static Result damage(Level level, BlockPos blockPos, float damage) {
        var immutableBlockPos = blockPos.immutable();

        var entry = BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.get(immutableBlockPos);

        var blockState = level.getBlockState(immutableBlockPos);
        var block = blockState.getBlock();
        var currentDestroyProgress = entry == null ? 0 : entry.getValue();
        var defaultDestroyTimeInSeconds = block.defaultDestroyTime();

        if (defaultDestroyTimeInSeconds < 0 || blockState.is(Blocks.FIRE)) {
            return Result.NOT_DAMAGED;
        }

        var destroyTimeInTicks = block.defaultDestroyTime() * 20;
        var weight = Math.max(destroyTimeInTicks, 1);

        var newDestroyProgress = currentDestroyProgress + (damage / weight);

        if (newDestroyProgress >= 9) {
            resetProgress(level, immutableBlockPos);
            level.destroyBlock(immutableBlockPos, false);
            return Result.DESTROYED;
        }

        setProgress(level, immutableBlockPos, newDestroyProgress);

        return Result.DAMAGED;
    }

    private static int getClampedProgress(float progress) {
        return (int) Mth.clamp(progress, -1F, 9F);
    }

    private static int computeBlockPosHash(BlockPos pos) {
        return Objects.hash(pos);
    }

    private BlockBreakProgressManager() {
        throw new UnsupportedOperationException();
    }

    public enum Result {
        DAMAGED,
        DESTROYED,
        NOT_DAMAGED
    }
}
