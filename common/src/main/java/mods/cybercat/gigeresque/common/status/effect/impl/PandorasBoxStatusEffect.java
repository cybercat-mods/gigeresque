package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.common.platform.Services;
import mod.azure.azurelib.core.object.Color;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
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
            var DungeonAdvancement = Constants.modResource("xeno_dungeon");
            var dungeonTest = player.server.getAdvancements().get(DungeonAdvancement);

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

                // If there are fewer than 4 entities and no dungeon/nest blocks are detected
                if (entityCount < 4 && spawnTimer == player.getRandom().nextIntBetweenInclusive(
                        60, 120)
                        && !dungeonBlockCheck && !nestBlockCheck) {
                    this.spawnWave(player); // Spawn the wave
                    spawnTimer = 0; // Reset the spawnTimer after spawning
                }
                // If there are already 4 or more entities, reset the spawnTimer
                else if (entityCount >= 4 || nonSolidBlockPos == null || dungeonBlockCheck || nestBlockCheck) {
                    spawnTimer = 0; // Reset spawnTimer when conditions are not met
                }
                if (spawnTimer >= 121)
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
                var faceHugger = GigEntities.FACEHUGGER.get().create(player.level());
                Objects.requireNonNull(faceHugger).setPos(player.getX() + offsetX, player.getY() + 0.5D,
                        player.getZ() + offsetZ);
                if (Services.PLATFORM.isDevelopmentEnvironment())
                    faceHugger.setGlowingTag(true);

                // Ensure the block is not solid and the world is loaded at that position
                BlockPos spawnPos = BlockPos.containing(faceHugger.getX(), faceHugger.getY(), faceHugger.getZ());
                if (player.level().isLoaded(spawnPos) && !faceHugger.level().getBlockState(
                        spawnPos).isSolid() && !player.level().getBiome(player.blockPosition()).is(
                        GigTags.AQUASPAWN_BIOMES)) {
                    player.level().addFreshEntity(faceHugger);
                }
            }
        } else {
            var aquaticAlien = GigEntities.AQUATIC_ALIEN.get().create(player.level());
            Objects.requireNonNull(aquaticAlien).setPos(player.getX() + offsetX, player.getY() - 9.5D,
                    player.getZ() + offsetZ);
            if (Services.PLATFORM.isDevelopmentEnvironment())
                aquaticAlien.setGlowingTag(true);

            // Ensure the block is not solid and the world is loaded at that position
            BlockPos spawnPos = BlockPos.containing(aquaticAlien.getX(), aquaticAlien.getY(), aquaticAlien.getZ());
            if (player.level().isLoaded(spawnPos)) {
                player.level().addFreshEntity(aquaticAlien);
            }
        }
    }
}
