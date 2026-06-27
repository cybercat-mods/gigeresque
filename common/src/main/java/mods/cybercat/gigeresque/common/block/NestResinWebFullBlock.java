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
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
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

    private static final int CHECK_RADIUS = CommonMod.config.alienblockConfigs.resinEntityCheckRange;

    public NestResinWebFullBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof AlienEntity) {
            return;
        }

        if (Constants.isCreativeSpecPlayer.test(entity)) {
            return;
        }

        if (
            entity instanceof LivingEntity livingEntity
                && GigEntityUtils.isTargetHostable(livingEntity)
                && !livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)
        ) {

            if (CommonMod.config.alienblockConfigs.enableResinAlienCheck && !isTaggedEntityNearby(world, pos)) {
                return;
            }

            if (livingEntity instanceof Mob mobEntity && !(livingEntity instanceof Player)) {
                AABB blockBox = new AABB(pos);
                AABB mobBox = mobEntity.getBoundingBox();
                if (blockBox.contains(mobBox.getCenter())) {
                    mobEntity.setNoAi(true);
                    mobEntity.getNavigation().stop();
                    mobEntity.setTarget(null);
                } else {
                    mobEntity.setNoAi(false);
                }
            }

            if (livingEntity instanceof Player player) {
                handleEggMorphingForPlayer(player, state, pos, entity, world);
            } else if (livingEntity instanceof Mob mob) {
                handleEggMorphingForMob(mob, state, entity);
            }
        }
    }

    public void entityExited(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof Mob mob && !(entity instanceof AlienEntity)) {
            mob.setNoAi(false);
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

    private boolean isTaggedEntityNearby(Level world, BlockPos pos) {
        return !world.getEntities(
            EntityTypeTest.forClass(AlienEntity.class),
            new AABB(pos).inflate(NestResinWebFullBlock.CHECK_RADIUS),
            e -> e.getType().is(GigTags.GIG_CLASSIC)
        ).isEmpty();
    }

    private void handleEggMorphingForPlayer(Player player, BlockState state, BlockPos pos, Entity sourceEntity, Level world) {
        if (sourceEntity instanceof AlienEntity) {
            return;
        }

        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;
        double dx = centerX - player.getX();
        double dz = centerZ - player.getZ();
        double distanceFromCenter = Math.sqrt(dx * dx + dz * dz);

        if (distanceFromCenter > 0.3) {
            player.setDeltaMovement(
                dx * 0.2,
                player.getDeltaMovement().y,
                dz * 0.2
            );
        } else {
            player.setPos(centerX, player.getY(), centerZ);
            player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
        }

        if (player.getDeltaMovement().y > 0) {
            player.setDeltaMovement(
                player.getDeltaMovement().x,
                0,
                player.getDeltaMovement().z
            );
        }

        player.makeStuckInBlock(state, new Vec3(0.25, 0.05F, 0.25));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, true, false));

        if (!player.hasEffect(GigStatusEffects.EGGMORPHING)) {
            player.addEffect(
                new MobEffectInstance(
                    GigStatusEffects.EGGMORPHING,
                    (int) CommonMod.config.getEggmorphTickTimer(),
                    0
                ),
                sourceEntity
            );
        }

        if (!world.isClientSide) {
            standingTick++;
        }

        if (standingTick >= 100) {
            if (!world.getBlockState(pos.below()).is(GigBlocks.NEST_RESIN_WEB_CROSS.get())) {
                player.setPos(pos.getCenter().x, pos.getY(), pos.getCenter().z);
            } else {
                player.setPos(pos.getCenter().x, pos.below().getY(), pos.getCenter().z);
            }
            player.makeStuckInBlock(state, new Vec3(0.25, 0.0F, 0.25));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, true, false), sourceEntity);
            standingTick = 0;
        }
    }

    private void handleEggMorphingForMob(LivingEntity mob, BlockState state, Entity sourceEntity) {
        if (mob instanceof AlienEntity || sourceEntity instanceof AlienEntity) {
            return;
        }

        double centerX = mob.blockPosition().getX() + 0.5;
        double centerZ = mob.blockPosition().getZ() + 0.5;
        double dx = centerX - mob.getX();
        double dz = centerZ - mob.getZ();
        double distanceFromCenter = Math.sqrt(dx * dx + dz * dz);

        if (distanceFromCenter > 0.3) {
            mob.setDeltaMovement(
                dx * 0.2,
                mob.getDeltaMovement().y,
                dz * 0.2
            );
        } else {
            mob.setPos(centerX, mob.getY(), centerZ);
            mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);
        }

        if (mob.getDeltaMovement().y > 0) {
            mob.setDeltaMovement(
                mob.getDeltaMovement().x,
                0,
                mob.getDeltaMovement().z
            );
        }

        if (
            !mob.hasEffect(GigStatusEffects.EGGMORPHING) &&
                GigEntityUtils.inResinEnoughToBeEggmorphed(mob)
        ) {
            mob.addEffect(
                new MobEffectInstance(
                    GigStatusEffects.EGGMORPHING,
                    (int) CommonMod.config.getEggmorphTickTimer(),
                    0
                ),
                sourceEntity
            );
        }

        mob.makeStuckInBlock(state, new Vec3(0.25, 0.0F, 0.25));
        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 100, true, false), sourceEntity);
    }

    @Override
    public void onRemove(
        @NotNull BlockState state,
        @NotNull Level world,
        @NotNull BlockPos pos,
        @NotNull BlockState newState,
        boolean movedByPiston
    ) {
        world.getEntitiesOfClass(Mob.class, new AABB(pos))
            .stream()
            .filter(mob -> !(mob instanceof AlienEntity))
            .forEach(mob -> {
                mob.setNoAi(false);
            });
        super.onRemove(state, world, pos, newState, movedByPiston);
    }

}
