package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class BeaconBlock extends Block {

    public BeaconBlock() {
        super(
            Properties.of()
                .sound(SoundType.DRIPSTONE_BLOCK)
                .strength(Float.MAX_VALUE, Float.MAX_VALUE)
                .noOcclusion()
                .noLootTable()
                .randomTicks()
                .noTerrainParticles()
                .lightLevel(blockState -> 2)
        );
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isClientSide()) {
            var nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(30));
            for (var player : nearbyPlayers) {
                if (
                        !player.getBlockStateOn().is(GigTags.DUNGEON_BLOCKS) && !player.hasEffect(
                                MobEffects.DARKNESS
                        ) && Constants.isNotCreativeSpecPlayer.test(player)
                ) {
                    player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 10));
                    level.playSound(null, pos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 1F, 1F);
                }
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }
}
