package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.tags.GigTags;

import java.util.HashMap;
import java.util.Map;

public class FragileRoughBlock extends Block implements Fallable {

    private int standingTick = 0;

    public FragileRoughBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        var offsetX = random.nextDouble() - 0.5D;
        var offsetY = 1.1D + (random.nextDouble() * 1.3D);
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

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, Entity entity) {
        if (entity.getType().is(GigTags.GIG_ALIENS))
            return;
        if (Constants.isCreativeSpecPlayer.test(entity))
            return;
        super.stepOn(level, pos, state, entity);
        if (entity instanceof LivingEntity livingEntity) {
            standingTick++;
            if (standingTick >= (livingEntity.isSteppingCarefully() ? 80 : 40)) {
                var areaEffectCloudEntity = new AreaEffectCloud(level, pos.getX(), pos.getY(), pos.getZ());
                areaEffectCloudEntity.setRadius(1.0F);
                areaEffectCloudEntity.setDuration(60);
                areaEffectCloudEntity.setParticle(ParticleTypes.ASH);
                areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration());
                level.addFreshEntity(areaEffectCloudEntity);
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                standingTick = 0;
            }
        }
    }
}
