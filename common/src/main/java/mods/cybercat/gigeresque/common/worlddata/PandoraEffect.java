package mods.cybercat.gigeresque.common.worlddata;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

public class PandoraEffect implements CustomSpawner {

    private int nextTick;

    public PandoraEffect() {}

    @Override
    public int tick(@NotNull ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!spawnEnemies || !level.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING) || !CommonMod.config.enablePandoraEffects) {
            return 0;
        }

        if (!PandoraData.isTriggered()) {
            return 0;
        }

        var randomSource = level.random;
        --this.nextTick;

        if (this.nextTick > 0) {
            return 0;
        }

        this.nextTick += 12000 + randomSource.nextInt(1200);

        if (!isValidSpawnTime(level)) {
            return 0;
        }

        var player = getRandomPlayer(level);
        if (player == null) {
            return 0;
        }

        var playerPos = player.blockPosition();
        if (player.level().getBlockState(playerPos).is(GigTags.DUNGEON_BLOCKS)) {
            return 0;
        }

        if (level.getMaxLocalRawBrightness(player.blockPosition()) >= 8) {
            if (CommonMod.config.enableLogging) {
                CommonMod.LOGGER.warn("Failed to spawn entity: Light level at {} is too high ({}).",
                        player.blockPosition(), player.level().getBrightness(
                                LightLayer.SKY, player.blockPosition()));
            }
            return 0;
        }

        var mutableBlockPos = getRandomNearbyPosition(player, randomSource);
        if (!isValidSpawnLocation(mutableBlockPos, level)) {
            return 0;
        }

        return spawnEggs(level, mutableBlockPos);
    }

    private boolean isValidSpawnTime(ServerLevel level) {
        return !level.isDay() && level.random.nextInt(5) == 0;
    }

    private Player getRandomPlayer(ServerLevel level) {
        var players = level.players();
        if (players.isEmpty()) {
            return null;
        }

        var player = players.get(level.random.nextInt(players.size()));
        return player.isSpectator() ? null : player;
    }

    private BlockPos getRandomNearbyPosition(Player player, RandomSource randomSource) {
        var xOffset = (24 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
        var zOffset = (24 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
        return player.blockPosition().mutable().move(xOffset, 0, zOffset);
    }

    @SuppressWarnings("deprecation")
    private boolean isValidSpawnLocation(BlockPos pos, ServerLevel level) {
        if (!level.hasChunksAt(pos.getX() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getZ() + 10)) {
            return false;
        }

        var biomeHolder = level.getBiome(pos);
        return biomeHolder.is(BiomeTags.IS_OVERWORLD);
    }

    private int spawnEggs(ServerLevel level, BlockPos startPos) {
        var randomSource = level.random;
        var difficulty = (int) Math.ceil(level.getCurrentDifficultyAt(startPos).getEffectiveDifficulty()) + 1;

        var spawnCount = 0;
        for (var i = 0; i < difficulty; ++i) {
            spawnCount++;

            startPos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startPos).getY());
            this.spawnEgg(level, startPos);

            startPos.setX(startPos.getX() + randomSource.nextInt(5) - randomSource.nextInt(5));
            startPos.setZ(startPos.getZ() + randomSource.nextInt(5) - randomSource.nextInt(5));
        }

        return spawnCount;
    }

    private void spawnEgg(ServerLevel level, BlockPos pos) {
        var belowState = level.getBlockState(pos.below());

        if (!belowState.is(Blocks.GRASS_BLOCK)) {
            return;
        }

        var isWaterBiome = level.getBiome(pos).is(GigTags.AQUASPAWN_BIOMES);

        var eggEntity = isWaterBiome
                ? GigEntities.AQUA_EGG.get().create(level)
                : GigEntities.EGG.get().create(level);

        eggEntity.setPos(pos.getX(), pos.getY(), pos.getZ());

        if (CommonMod.config.enableLogging) {
            eggEntity.setGlowingTag(true);
            CommonMod.LOGGER.info("Spawned Mob at {}, {}, {}", pos.getX(), pos.getY(), pos.getZ());
        }

        level.addFreshEntityWithPassengers(eggEntity);
    }
}