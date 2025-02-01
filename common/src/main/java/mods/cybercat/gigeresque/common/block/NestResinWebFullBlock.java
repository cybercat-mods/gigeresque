package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class NestResinWebFullBlock extends AbstractNestBlock {

    private int standingTick = 0;

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
            entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(entity) && !livingEntity.hasEffect(
                GigStatusEffects.IMPREGNATION
            )
        ) {
            if (livingEntity instanceof Player player) {
                player.makeStuckInBlock(state, new Vec3(0.25, 0.05F, 0.25));
                if (!player.hasEffect(GigStatusEffects.EGGMORPHING))
                    player.addEffect(
                        new MobEffectInstance(
                            GigStatusEffects.EGGMORPHING,
                            (int) CommonMod.config.getEggmorphTickTimer(),
                            0
                        ),
                        entity
                    );
                if (!world.isClientSide())
                    standingTick++;
                if (standingTick >= 100) {
                    if (!world.getBlockState(pos.below()).is(GigBlocks.NEST_RESIN_WEB_CROSS.get()))
                        player.setPos(pos.getCenter().x, pos.getY(), pos.getCenter().z);
                    if (world.getBlockState(pos.below()).is(GigBlocks.NEST_RESIN_WEB_CROSS.get()))
                        player.setPos(pos.getCenter().x, pos.below().getY(), pos.getCenter().z);
                    player.makeStuckInBlock(state, new Vec3(0.25, 0.0F, 0.25));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, true, false), entity);
                    standingTick = 0;
                }
            } else if (livingEntity instanceof Mob) {
                if (!livingEntity.hasEffect(GigStatusEffects.EGGMORPHING))
                    livingEntity.addEffect(
                        new MobEffectInstance(
                            GigStatusEffects.EGGMORPHING,
                            (int) CommonMod.config.getEggmorphTickTimer(),
                            0
                        ),
                        entity
                    );
                livingEntity.makeStuckInBlock(state, new Vec3(0.25, 0.0F, 0.25));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, true, false), entity);
                standingTick = 0;
            } else {
                standingTick = 0;
            }
        }
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
        @NotNull BlockState state,
        @NotNull BlockGetter world,
        @NotNull BlockPos pos,
        @NotNull CollisionContext context
    ) {
        return context instanceof EntityCollisionContext entitycollisioncontext && entitycollisioncontext.getEntity() instanceof AlienEntity
            ? Block.box(0, 0, 0, 0, 0, 0)
            : super.getCollisionShape(state, world, pos, context);
    }
}
