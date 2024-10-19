package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class NestResinWebFullBlock extends Block {

    public NestResinWebFullBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity.getType().is(GigTags.GIG_ALIENS))
            return;
        if (Constants.isCreativeSpecPlayer.test(entity))
            return;
        if (
            entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(
                entity
            ) && !livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)
        ) {
            livingEntity.makeStuckInBlock(state, new Vec3(0.25, 0.0, 0.25));
            if (!livingEntity.hasEffect(GigStatusEffects.EGGMORPHING))
                livingEntity.addEffect(
                    new MobEffectInstance(
                        GigStatusEffects.EGGMORPHING,
                        (int) Gigeresque.config.getEggmorphTickTimer(),
                        10
                    ),
                    entity
                );
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), entity);
            if (!world.getBlockState(pos.below()).is(GigBlocks.NEST_RESIN_WEB_CROSS))
                livingEntity.setPos(pos.getCenter().x, pos.getY(), pos.getCenter().z);
            if (world.getBlockState(pos.below()).is(GigBlocks.NEST_RESIN_WEB_CROSS))
                livingEntity.setPos(pos.getCenter().x, pos.below().getY(), pos.getCenter().z);
        }
    }
}
