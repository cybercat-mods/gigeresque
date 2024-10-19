package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Listener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem.User;

import mods.cybercat.gigeresque.common.Gigeresque;

public interface AzureTicker {

    static void tick(Level level, Data data, User user) {
        if (!(level instanceof ServerLevel serverLevel))
            return;
        if (data.getCurrentVibration() == null)
            AzureTicker.trySelectAndScheduleVibration(serverLevel, data, user);
        if (data.getCurrentVibration() == null)
            return;
        var travelCheck = data.getTravelTimeInTicks() > 0;
        data.decrementTravelTime();
        if (data.getTravelTimeInTicks() <= 0)
            travelCheck = AzureTicker.receiveVibration(serverLevel, data, user, data.getCurrentVibration());
        if (travelCheck)
            user.onDataChanged();
    }

    private static void trySelectAndScheduleVibration(ServerLevel serverLevel, Data data, User user) {
        data.getSelectionStrategy().chosenCandidate(serverLevel.getGameTime()).ifPresent(vibrationInfo -> {
            data.setCurrentVibration(vibrationInfo);
            var vec3 = vibrationInfo.pos();
            data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(vibrationInfo.distance()));
            if (Gigeresque.config.enableDevparticles)
                serverLevel.sendParticles(
                    new VibrationParticleOption(user.getPositionSource(), data.getTravelTimeInTicks()),
                    vec3.x,
                    vec3.y,
                    vec3.z,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                );
            user.onDataChanged();
            data.getSelectionStrategy().startOver();
        });
    }

    private static boolean receiveVibration(ServerLevel serverLevel, Data data, User user, VibrationInfo vibrationInfo) {
        var blockPos = BlockPos.containing(vibrationInfo.pos());
        var blockPos2 = user.getPositionSource().getPosition(serverLevel).map(BlockPos::containing).orElse(blockPos);
        if (user.requiresAdjacentChunksToBeTicking() && !AzureTicker.areAdjacentChunksTicking(serverLevel, blockPos2))
            return false;
        user.onReceiveVibration(
            serverLevel,
            blockPos,
            vibrationInfo.gameEvent(),
            vibrationInfo.getEntity(serverLevel).orElse(null),
            vibrationInfo.getProjectileOwner(serverLevel).orElse(null),
            Listener.distanceBetweenInBlocks(blockPos, blockPos2)
        );
        data.setCurrentVibration(null);
        return true;
    }

    private static boolean areAdjacentChunksTicking(Level level, BlockPos blockPos) {
        var chunkPos = new ChunkPos(blockPos);
        for (var i = chunkPos.x - 1; i < chunkPos.x + 1; ++i)
            for (var j = chunkPos.z - 1; j < chunkPos.z + 1; ++j) {
                var chunkAccess = level.getChunkSource().getChunkNow(i, j);
                if (chunkAccess != null && level.shouldTickBlocksAt(chunkAccess.getPos().toLong()))
                    continue;
                return false;
            }
        return true;
    }
}
