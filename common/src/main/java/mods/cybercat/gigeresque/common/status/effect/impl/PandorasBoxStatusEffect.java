package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.platform.Services;
import mod.azure.azurelib.core.object.Color;
import mods.cybercat.gigeresque.CommonMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;

public class PandorasBoxStatusEffect extends MobEffect {

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
        if (!CommonMod.config.enablePandoraEffects) {
            return false;
        }

        if (livingEntity instanceof ServerPlayer player && Constants.isNotCreativeSpecPlayer.test(player)) {
            var dungeonAdvancement = Constants.modResource("xeno_dungeon");
            var dungeonTest = player.server.getAdvancements().get(dungeonAdvancement);

            if (dungeonTest != null && player.getAdvancements().getOrStartProgress(dungeonTest).isDone()) {
                ++spawnTimer;

                // Check entity count within 64 Blocks
                final var aabb = new AABB(player.blockPosition()).inflate(64D);
                final var entityCount = player.level()
                    .getEntities(
                        EntityTypeTest.forClass(AlienEntity.class),
                        aabb,
                        Entity::isAlive
                    )
                    .size();

                // Check if dungeon or nest blocks are present within the area
                final var dungeonNestBlockCheck = player.level()
                    .getBlockStates(new AABB(player.blockPosition()).inflate(8D))
                    .anyMatch(blockState -> blockState.is(GigTags.DUNGEON_BLOCKS) || blockState.is(GigTags.NEST_BLOCKS));

                // Time between spawn attempts
                var minTicksBetweenSpawns = player.getY() < 20 ? 6000 : 12000; // 5 to 10 mins under Y=20, 10 to 15 mins
                                                                               // above
                var maxTicksBetweenSpawns = player.getY() < 20 ? 12000 : 18000;

                // Calculate the chance to spawn based on the player's Y level
                var spawnChanceModifier = 1.0;
                if (player.getY() > 60)
                    spawnChanceModifier = 0.25; // Rare spawns on the surface
                else if (player.getY() < 20)
                    spawnChanceModifier = 1.5; // More likely to spawn bursters at low Y-level

                // Day/night spawn adjustment
                var dayTime = player.level().getDayTime() % 24000;
                if (dayTime >= 0 && dayTime < 13000)
                    spawnChanceModifier *= 0.5; // Less likely to spawn during the day
                else
                    spawnChanceModifier *= 1.5; // More likely to spawn at night

                // Check if the time has passed and conditions are met for spawning
                if (
                    spawnTimer >= minTicksBetweenSpawns && player.getRandom().nextDouble() < spawnChanceModifier && entityCount < 4
                        && !dungeonNestBlockCheck
                ) {
                    this.spawnWave(player);
                    spawnTimer = 0; // Reset spawnTimer after spawning
                }

                // Reset the spawn timer after max time
                if (spawnTimer >= maxTicksBetweenSpawns)
                    spawnTimer = 0;
            }
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    public void spawnWave(ServerPlayer player) {
        final var random = player.getRandom();
        var distance = 30 + random.nextDouble() * 30; // random distance from 30 to 60

        // Get the player's look direction and invert it to spawn behind the player
        var lookAngle = player.getLookAngle();
        var offsetX = -lookAngle.x * distance; // negative to get behind
        var offsetZ = -lookAngle.z * distance; // negative to get behind

        if (!player.level().getBiome(player.blockPosition()).is(GigTags.AQUASPAWN_BIOMES)) {
            var entities = List.of(
                GigEntities.RUNNERBURSTER.get(),
                GigEntities.CHESTBURSTER.get(),
                GigEntities.FACEHUGGER.get()
            );
            EntityType<? extends WaterAnimal> selectedEntity = null;
            for (var i = 0; i < entities.size(); i++) {
                if (player.getRandom().nextInt(100) < (i + 1) * 100 / entities.size()) {
                    selectedEntity = entities.get(i);
                    break;
                }
            }
            if (selectedEntity != null)
                for (var k = 1; k < 4; ++k) {
                    var faceHugger = selectedEntity.create(player.level());
                    if (faceHugger != null) {
                        faceHugger.setPos(
                            player.getX() + offsetX,
                            player.getY() + 0.5D,
                            player.getZ() + offsetZ
                        );
                        faceHugger.setOnGround(true);
                        if (Services.PLATFORM.isDevelopmentEnvironment())
                            faceHugger.setGlowingTag(true);

                        var spawnPos = BlockPos.containing(faceHugger.getX(), faceHugger.getY(), faceHugger.getZ());
                        if (
                            player.level().isLoaded(spawnPos) && (faceHugger.level()
                                .getBlockState(
                                    spawnPos
                                )
                                .isAir() || faceHugger.level()
                                    .getBlockState(
                                        spawnPos
                                    )
                                    .is(Blocks.WATER)) && !player.level()
                                        .getBiome(player.blockPosition())
                                        .is(
                                            GigTags.AQUASPAWN_BIOMES
                                        )
                        ) {
                            player.level().addFreshEntity(faceHugger);
                            if (Services.PLATFORM.isDevelopmentEnvironment())
                                AzureLib.LOGGER.info("Spawned Mob");
                            if (player instanceof ServerPlayer serverPlayer) {
                                var advancement = serverPlayer.server.getAdvancements()
                                    .get(
                                        Constants.modResource("firstspawnfromeffect")
                                    );
                                if (
                                    advancement != null && !serverPlayer.getAdvancements()
                                        .getOrStartProgress(
                                            advancement
                                        )
                                        .isDone()
                                )
                                    for (
                                        var s : serverPlayer.getAdvancements()
                                            .getOrStartProgress(
                                                advancement
                                            )
                                            .getRemainingCriteria()
                                    )
                                        serverPlayer.getAdvancements().award(advancement, s);
                            }

                            for (var x = -1; x <= 1; x++)
                                for (var z = -1; z <= 1; z++) {
                                    var resinPos = spawnPos.offset(x, 0, z);
                                    if (
                                        !player.level().getBlockState(resinPos).isAir() && player.level().isEmptyBlock(resinPos) && player
                                            .level()
                                            .isLoaded(resinPos)
                                    )
                                        NestBuildingHelper.tryBuildNestAround(player.level(), resinPos, faceHugger);
                                }
                        }
                    }
                }
        } else {
            var aquaticAlien = GigEntities.AQUATIC_CHESTBURSTER.get().create(player.level());
            if (aquaticAlien != null) {
                aquaticAlien.setPos(player.getX() + offsetX, player.getY() - 9.5D, player.getZ() + offsetZ);
                if (Services.PLATFORM.isDevelopmentEnvironment())
                    aquaticAlien.setGlowingTag(true);

                var spawnPos = BlockPos.containing(aquaticAlien.getX(), aquaticAlien.getY(), aquaticAlien.getZ());
                if (player.level().isLoaded(spawnPos)) {
                    if (Services.PLATFORM.isDevelopmentEnvironment())
                        AzureLib.LOGGER.info("Spawned Mob");
                    player.level().addFreshEntity(aquaticAlien);
                    if (player instanceof ServerPlayer serverPlayer) {
                        var advancement = serverPlayer.server.getAdvancements()
                            .get(
                                Constants.modResource("firstspawnfromeffect")
                            );
                        if (
                            advancement != null && !serverPlayer.getAdvancements()
                                .getOrStartProgress(
                                    advancement
                                )
                                .isDone()
                        )
                            for (
                                var s : serverPlayer.getAdvancements()
                                    .getOrStartProgress(
                                        advancement
                                    )
                                    .getRemainingCriteria()
                            )
                                serverPlayer.getAdvancements().award(advancement, s);
                    }

                    for (var x = -1; x <= 1; x++) {
                        for (var z = -1; z <= 1; z++) {
                            var resinPos = spawnPos.offset(x, 0, z);
                            if (
                                !player.level().getBlockState(resinPos).isAir() && player.level().isEmptyBlock(resinPos) && player.level()
                                    .isLoaded(resinPos)
                            )
                                NestBuildingHelper.tryBuildNestAround(player.level(), resinPos, aquaticAlien);
                        }
                    }
                }
            }
        }
    }
}
