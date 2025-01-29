package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.sound.GigSounds;

import java.util.HashMap;
import java.util.Map;

public class GigBlock extends Block {

    public GigBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        var offsetX = random.nextDouble() - 0.5D;
        var offsetY = 1.1D + (random.nextDouble() * 1.3D);
        var offsetZ = random.nextDouble() - 0.5D;
        if (
            (level.getBlockState(pos.above()).isAir() || level.getBlockState(pos.above())
                .is(
                    GigBlocks.ALIEN_STORAGE_BLOCK_2.get()
                )) && pos.getY() <= -50
        ) {
            if (Constants.particleCount < 1500) {
                level.addParticle(
                        GigParticles.MIST.get(),
                        pos.getX() + 0.5D + offsetX,
                        pos.getY() + offsetY,
                        pos.getZ() + 0.5D + offsetZ,
                        0.0D,
                        0.002D,
                        0.0D
                );
            }
        }
        if (!level.getBlockState(pos.above()).isAir() && random.nextInt(1000) == 0) {
            level.playLocalSound(pos, GigSounds.DUNGEON_IDLE.get(), SoundSource.BLOCKS, 0.2F, 0.5F, true);
        }
    }

}
