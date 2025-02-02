package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

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

    public static void damage(Level level, BlockPos blockPos, float damage) {
        var immutableBlockPos = blockPos.immutable();

        BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.compute(immutableBlockPos, (key, entry) -> {
            var blockState = level.getBlockState(immutableBlockPos);
            var block = blockState.getBlock();
            var currentDestroyProgress = entry == null ? 0 : entry.getValue();
            var defaultDestroyTimeInSeconds = block.defaultDestroyTime();

            if (defaultDestroyTimeInSeconds < 0) {
                return null;
            }

            var destroyTimeInTicks = block.defaultDestroyTime() * 20;
            var weight = Math.max(destroyTimeInTicks, 1);

            var newDestroyProgress = currentDestroyProgress + (damage / weight);
            var progress = (int) Mth.clamp(newDestroyProgress, 0F, 9F);
            var hash = Objects.hash(immutableBlockPos);

            if (progress >= 9) {
                level.destroyBlockProgress(hash, immutableBlockPos, -1);
                level.destroyBlock(immutableBlockPos, false);
                return null;
            } else {
                level.destroyBlockProgress(hash, immutableBlockPos, progress);
            }
            return Map.entry(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5), newDestroyProgress);
        });
    }

    private BlockBreakProgressManager() {
        throw new UnsupportedOperationException();
    }
}
