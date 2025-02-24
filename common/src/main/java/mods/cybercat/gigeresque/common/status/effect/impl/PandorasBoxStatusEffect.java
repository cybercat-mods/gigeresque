package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.core.object.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.tags.GigTags;

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
        if (!CommonMod.config.enablePandoraEffects || !(livingEntity instanceof ServerPlayer player)) {
            return false;
        }

        var dungeonAdvancement = Constants.modResource("xeno_dungeon");
        var advancement = player.server.getAdvancements().get(dungeonAdvancement);

        if (advancement == null || !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
            return false;
        }

        if (Constants.isCreativeSpecPlayer.test(player)) {
            return false;
        }

        ++spawnTimer;

        int spawnInterval = 12000 + player.getRandom().nextInt(600); // 10 to 15 mins
        if (spawnTimer >= spawnInterval) {
            spawnWave(player);
            spawnTimer = 0;
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    public void spawnWave(ServerPlayer player) {
        final var random = player.getRandom();
        var maxAttempts = 50;
        BlockPos spawnPos = null;
        var isWaterBiome = player.level().getBiome(player.blockPosition()).is(GigTags.AQUASPAWN_BIOMES);

        for (int i = 0; i < maxAttempts; i++) {
            var distance = 30 + (double) random.nextInt(31);
            var lookAngle = player.getLookAngle();
            var offsetX = -lookAngle.x * distance;
            var offsetZ = -lookAngle.z * distance;

            var potentialPos = BlockPos.containing(
                    player.getX() + offsetX,
                    player.getY() + 0.5D,
                    player.getZ() + offsetZ
            );

            if (player.level().getBlockState(potentialPos).isAir() || player.level().getBlockState(potentialPos).is(Blocks.WATER)) {
                spawnPos = potentialPos;
                break;
            }
        }

        if (spawnPos == null) {
            if (CommonMod.config.enableLogging)
                CommonMod.LOGGER.warn("Failed to find a valid spawn position after {} attempts.", maxAttempts);
            return;
        }

        var eggEntity = isWaterBiome
                ? GigEntities.AQUA_EGG.get().create(player.level())
                : GigEntities.EGG.get().create(player.level());

        if (eggEntity != null) {
            eggEntity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            eggEntity.setOnGround(true);
            if (CommonMod.config.enableLogging) {
                eggEntity.setGlowingTag(true);
            }

            if (player.level().getBrightness(LightLayer.SKY, spawnPos) <= 5) {
                player.level().playSound(player, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), SoundEvents.GLASS_BREAK,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                player.level().addFreshEntity(eggEntity);
                if (CommonMod.config.enableLogging) {
                    CommonMod.LOGGER.info("Spawned Mob at {}, {}, {}", spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                }
            } else {
                if (CommonMod.config.enableLogging)
                    CommonMod.LOGGER.warn("Failed to spawn entity: Light level at {} is too high ({}).", spawnPos, player.level().getBrightness(LightLayer.SKY, spawnPos));
            }
        } else {
            if (CommonMod.config.enableLogging)
                CommonMod.LOGGER.warn("Failed to create egg entity. Entity type might be null: {}", isWaterBiome ? "AQUA_EGG" : "EGG");
        }
    }

}
