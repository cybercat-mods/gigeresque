package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.common.platform.Services;
import mod.azure.azurelib.core.object.Color;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PandorasBoxStatusEffect extends MobEffect {
    private static BlockPos nonSolidBlockPos = null;
    private int spawnTimer = 0;

    public PandorasBoxStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.RED.getColor());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);

        if (livingEntity instanceof ServerPlayer player) {
            var dungeonAdvancement = Constants.modResource("xeno_dungeon");
            var dungeonTest = player.server.getAdvancements().get(dungeonAdvancement);

            if (dungeonTest != null && player.getAdvancements().getOrStartProgress(dungeonTest).isDone()) {
                ++spawnTimer;

                // Check entity count within 20 blocks around the player
                final var entityCount = player.level().getEntities(EntityTypeTest.forClass(FacehuggerEntity.class),
                        new AABB(player.blockPosition()).inflate(64D),
                        Entity::isAlive).size();

                // Check if dungeon or nest blocks are present within the area
                final var dungeonBlockCheck = player.level().getBlockStates(
                                new AABB(player.blockPosition()).inflate(64D))
                        .findAny().get().is(GigTags.DUNGEON_BLOCKS);
                final var nestBlockCheck = player.level().getBlockStates(new AABB(player.blockPosition()).inflate(64D))
                        .findAny().get().is(GigTags.NEST_BLOCKS);

                // Find non-solid block positions for spawning
                nonSolidBlockPos = GigEntityUtils.findFreeSpace(player.level(), player.blockPosition(), 64);

                // Check if the spawn location is out of the player's line of sight
                boolean isOutOfSight = true;
                if (nonSolidBlockPos != null) {
                    Vec3 playerEyePosition = player.getEyePosition();
                    Vec3 spawnPosition = new Vec3(nonSolidBlockPos.getX(), nonSolidBlockPos.getY(), nonSolidBlockPos.getZ());
                    ClipContext context = new ClipContext(playerEyePosition, spawnPosition, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player);
                    BlockHitResult hitResult = player.level().clip(context);
                    isOutOfSight = hitResult.getType() != HitResult.Type.MISS && !hitResult.getBlockPos().equals(nonSolidBlockPos);
                }

                // Calculate the chance to spawn based on player's Y level
                double spawnChanceModifier = 1.0;  // Default spawn chance modifier

                if (player.getY() > 60) {
                    // Rarer spawns on the surface (Y level above 60)
                    spawnChanceModifier = 0.25;  // 25% of normal spawn chance
                } else if (player.getY() < 20) {
                    // Increase chance for bursters in lower depths (Y level below 20)
                    spawnChanceModifier = 1.5;  // 150% spawn chance
                }

                // Day/night spawn chance adjustment
                long dayTime = player.level().getDayTime() % 24000; // Get the current time in the 24-hour Minecraft day cycle
                if (dayTime >= 0 && dayTime < 13000) {
                    // Less likely to spawn during the day
                    spawnChanceModifier *= 0.5; // 50% of the normal spawn chance during the day
                } else {
                    // Full chance to spawn at night
                    spawnChanceModifier *= 1.5; // 150% spawn chance at night
                }

                // Apply chance to spawn based on height and time of day
                if (player.getRandom().nextDouble() < spawnChanceModifier) {
                    // If there are fewer than 4 entities, no dungeon/nest blocks are detected, and the spawn is out of sight
                    if (entityCount < 4 && spawnTimer == player.getRandom().nextIntBetweenInclusive(6000, 12000)
                            && !dungeonBlockCheck && !nestBlockCheck && nonSolidBlockPos != null && isOutOfSight) {
                        this.spawnWave(player);
                        var advancement = player.server.getAdvancements().get(Constants.modResource("firstspawnfromeffect"));
                        var advancementProgress = player.getAdvancements().getOrStartProgress(advancement);
                        if (!advancementProgress.isDone()) {
                            for (var s : player.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
                                player.getAdvancements().award(advancement, s);
                            }
                        }
                        spawnTimer = 0; // Reset the spawnTimer after spawning
                    }
                    // Reset spawnTimer if conditions are not met
                    else if (entityCount >= 4 || nonSolidBlockPos == null || dungeonBlockCheck || nestBlockCheck || !isOutOfSight) {
                        spawnTimer = 0;
                    }
                }

                if (spawnTimer >= 12001)
                    spawnTimer = 0;
            }
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    public void spawnWave(ServerPlayer player) {
        final var random = player.getRandom();
        var distance = 30 + random.nextDouble() * 30; // random distance from 30 to 60
        var angle = random.nextDouble() * 2 * Math.PI; // angle in radians for random direction
        // Calculate the new position
        var offsetX = Math.cos(angle) * distance;
        var offsetZ = Math.sin(angle) * distance;
        if (!player.level().getBiome(player.blockPosition()).is(GigTags.AQUASPAWN_BIOMES)) {
            for (var k = 1; k < 4; ++k) {
                var entityTypeToSpawn = player.getY() < 20 ? GigEntities.CHESTBURSTER.get() : GigEntities.FACEHUGGER.get();
                var faceHugger = entityTypeToSpawn.create(player.level());
                Objects.requireNonNull(faceHugger).setPos(player.getX() + offsetX, player.getY() + 0.5D,
                        player.getZ() + offsetZ);
                faceHugger.setOnGround(true);
                if (Services.PLATFORM.isDevelopmentEnvironment())
                    faceHugger.setGlowingTag(true);

                // Ensure the block is not solid and the world is loaded at that position
                var spawnPos = BlockPos.containing(faceHugger.getX(), faceHugger.getY(), faceHugger.getZ());
                if (player.level().isLoaded(spawnPos) && !faceHugger.level().getBlockState(
                        spawnPos).isSolid() && !player.level().getBiome(player.blockPosition()).is(
                        GigTags.AQUASPAWN_BIOMES)) {
                    player.level().addFreshEntity(faceHugger);
                    // Place resin blocks in a 3x3 area around the spawn position
                    for (var x = -1; x <= 1; x++) {
                        for (var z = -1; z <= 1; z++) {
                            var resinPos = spawnPos.offset(x, 0, z); // Adjust position around the entity

                            // Randomly select between NEST_RESIN_BLOCK and NEST_RESIN_WEB_CROSS
                            var resinBlockState = player.getRandom().nextBoolean()
                                    ? GigBlocks.NEST_RESIN_BLOCK.get().defaultBlockState()
                                    : GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState();

                            // Ensure the block is not solid and the world is loaded at that position
                            if (player.level().isEmptyBlock(resinPos) && player.level().isLoaded(resinPos)) {
                                player.level().setBlockAndUpdate(resinPos, resinBlockState);
                            }
                        }
                    }
                }
            }
        } else {
            var aquaticAlien = GigEntities.AQUATIC_CHESTBURSTER.get().create(player.level());
            Objects.requireNonNull(aquaticAlien).setPos(player.getX() + offsetX, player.getY() - 9.5D,
                    player.getZ() + offsetZ);
            if (Services.PLATFORM.isDevelopmentEnvironment())
                aquaticAlien.setGlowingTag(true);

            // Ensure the block is not solid and the world is loaded at that position
            var spawnPos = BlockPos.containing(aquaticAlien.getX(), aquaticAlien.getY(), aquaticAlien.getZ());
            if (player.level().isLoaded(spawnPos)) {
                player.level().addFreshEntity(aquaticAlien);
                // Place resin blocks in a 3x3 area around the spawn position
                for (var x = -1; x <= 1; x++) {
                    for (var z = -1; z <= 1; z++) {
                        var resinPos = spawnPos.offset(x, 0, z); // Adjust position around the entity

                        // Randomly select between NEST_RESIN_BLOCK and NEST_RESIN_WEB_CROSS
                        var resinBlockState = player.getRandom().nextBoolean()
                                ? GigBlocks.NEST_RESIN_BLOCK.get().defaultBlockState()
                                : GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState();

                        // Ensure the block is not solid and the world is loaded at that position
                        if (player.level().isEmptyBlock(resinPos) && player.level().isLoaded(resinPos)) {
                            player.level().setBlockAndUpdate(resinPos, resinBlockState);
                        }
                    }
                }
            }
        }
    }
}
