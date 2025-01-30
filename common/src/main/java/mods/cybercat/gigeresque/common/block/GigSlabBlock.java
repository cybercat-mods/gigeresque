package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class GigSlabBlock extends SlabBlock {

    public GigSlabBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        var slabtype = state.getValue(TYPE);
        var startingY = switch (slabtype) {
            case DOUBLE, TOP -> 1.1D;
            default -> 0.55D;
        };
        var offsetX = random.nextDouble() - 0.5D;
        var offsetY = startingY + (random.nextDouble() * 1.3D);
        var offsetZ = random.nextDouble() - 0.5D;
        if (
            (level.getBlockState(pos.above()).isAir() || level.getBlockState(pos.above()).is(GigTags.ALLOW_MIST_BLOCKS)) && pos
                .getY() <= -50
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
    }
}
