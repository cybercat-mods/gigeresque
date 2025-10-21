package mods.cybercat.gigeresque.common.worlddata;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.tags.GigTags;

/**
 * The PandoraEffect class implements the CustomSpawner interface and handles the logic for entity spawning under
 * specific conditions in a server-level environment. This includes determining valid spawn times, selecting players,
 * and spawning entities while considering various constraints such as light levels, biomes, and configurations.
 */
public class PandoraEffect implements CustomSpawner {

    private int nextTick = 0;

    public PandoraEffect() {}

    /**
     * Attempts to execute the tick event within the Pandora effect, which manages specific game logic like spawning
     * entities under certain conditions.
     *
     * @param level           The current server level where the tick event is executed.
     * @param spawnEnemies    A boolean indicating whether hostile entities should be spawned.
     * @param spawnFriendlies A boolean indicating whether friendly entities should be spawned.
     * @return An integer representing the number of entities successfully spawned. Returns 0 if no entities were
     *         spawned.
     */
    @Override
    public int tick(@NotNull ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (
            !PandoraData.isTriggered() || !spawnEnemies || !level.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)
                || !CommonMod.config.generalConfigs.enablePandoraEffects
        ) {
            return 0;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return 0;
        }

        if (!PandoraData.isTriggered()) {
            return 0;
        }

        if (--this.nextTick > 0) {
            return 0;
        }

        this.nextTick = 6000 + level.random.nextInt(200);

        var randomSource = level.random;

        var player = getRandomPlayer(level);
        if (player == null) {
            return 0;
        }

        var playerPos = player.blockPosition();
        if (player.level().getBlockState(playerPos).is(GigTags.DUNGEON_BLOCKS)) {
            return 0;
        }

        if (level.getMaxLocalRawBrightness(player.blockPosition()) >= 8) {
            if (CommonMod.config.generalConfigs.enableLogging) {
                CommonMod.LOGGER.warn(
                    "Failed to spawn entity: Light level at {} is too high ({}).",
                    player.blockPosition(),
                    player.level()
                        .getBrightness(
                            LightLayer.SKY,
                            player.blockPosition()
                        )
                );
            }
            return 0;
        }

        var mutableBlockPos = getRandomNearbyPosition(player, randomSource);
        if (!isValidSpawnLocation(mutableBlockPos, level)) {
            return 0;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("firstspawnfromeffect"));
            if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone())
                for (var s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria())
                    serverPlayer.getAdvancements().award(advancement, s);
        }

        return spawnEggs(level, mutableBlockPos);
    }

    /**
     * Determines whether the current time in the given server level is suitable for spawning entities. Spawning is
     * valid if it is night and a random condition is met.
     *
     * @param level The server level being checked for spawn time validity.
     * @return True if the current time in the given level is a valid spawn time, false otherwise.
     */
    private boolean isValidSpawnTime(ServerLevel level) {
        return level.random.nextInt(5) == 0;
    }

    /**
     * Selects a random non-spectator player from the provided server level.
     *
     * @param level The server level from which to retrieve a random player.
     * @return A random non-spectator player, or null if no suitable players are found.
     */
    private Player getRandomPlayer(ServerLevel level) {
        var players = level.players();
        if (players.isEmpty()) {
            return null;
        }

        var player = players.get(level.random.nextInt(players.size()));

        if (player.isSpectator()) {
            CommonMod.LOGGER.warn("Excluded player {} because they are in Spectator mode.", player.getName().getString());
            return null;
        }

        return player;
    }

    /**
     * Generates a random nearby position around the given player by applying a random offset. The offset is determined
     * using a random number generator, ensuring the position is within a specific range in the X and Z coordinates.
     *
     * @param player       The player around whom the random nearby position is generated.
     * @param randomSource The random number generator used to calculate the offsets for the position.
     * @return A BlockPos object representing the new random position near the player.
     */
    private BlockPos getRandomNearbyPosition(Player player, RandomSource randomSource) {
        var xOffset = (24 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
        var zOffset = (24 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
        return player.blockPosition().mutable().move(xOffset, 0, zOffset);
    }

    /**
     * Determines whether the specified location is a valid spawn location for entities based on level conditions such
     * as chunk loading, light levels, and biome type.
     *
     * @param pos   The position in the world to be checked.
     * @param level The server-level instance to check against.
     * @return True if the position is a valid spawn location, false otherwise.
     */
    @SuppressWarnings("deprecation")
    private boolean isValidSpawnLocation(BlockPos pos, ServerLevel level) {
        if (!level.hasChunksAt(pos.getX() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getZ() + 10)) {
            return false;
        }

        if (level.getMaxLocalRawBrightness(pos) >= 8) {
            return false;
        }

        var biomeHolder = level.getBiome(pos);
        return biomeHolder.is(BiomeTags.IS_OVERWORLD);
    }

    /**
     * Spawns an egg entity at the specified position within the provided server level. The method adjusts the
     * Y-coordinate of the starting position based on the height map to ensure the egg is spawned at a valid altitude.
     * The position is then slightly randomized in the X and Z coordinates to create variation in spawning locations.
     *
     * @param level    The server level where the egg entity will be spawned.
     * @param startPos The initial position where the egg spawning will be attempted. The position is modified during
     *                 the execution of this method.
     * @return An integer representing the number of eggs successfully spawned, always 1 in this implementation.
     */
    private int spawnEggs(ServerLevel level, BlockPos startPos) {
        var randomSource = level.random;
        startPos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startPos).getY());

        this.spawnEgg(level, startPos);
        startPos.setX(startPos.getX() + randomSource.nextInt(5) - randomSource.nextInt(5));
        startPos.setZ(startPos.getZ() + randomSource.nextInt(5) - randomSource.nextInt(5));

        return 1;
    }

    /**
     * Spawns an egg entity at the specified position within the provided server level. The type of egg spawned is
     * determined by the biome at the given position. If the position below the specified block is not a grass block, no
     * egg is spawned. Additionally, logging and glowing status are applied based on configuration settings.
     *
     * @param level The current server level where the egg entity will be spawned.
     * @param pos   The position in the world where the egg entity will be placed.
     */
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

        if (CommonMod.config.generalConfigs.enableLogging) {
            eggEntity.setGlowingTag(true);
            CommonMod.LOGGER.info(
                "Spawned Pandora {} at {}, {}, {}",
                eggEntity.getDisplayName().getString(),
                pos.getX(),
                pos.getY(),
                pos.getZ()
            );
        }

        level.addFreshEntityWithPassengers(eggEntity);
    }
}
