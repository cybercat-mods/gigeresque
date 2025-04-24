package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class BreakBlocksGoal extends Goal {

    protected final AlienEntity alienEntity;

    private BlockPos targetPos = BlockPos.ZERO;

    private BlockState targetBlock;

    private BlockHitResult targetHitResult;

    private boolean canReach;

    private int sightCounter;

    private int giveUpDelay;

    private int hitCounter;

    private float blockDamage;

    private int lastBlockDamage = -1;

    private final TagKey<Block> blockTagKey;

    private float range;

    public BreakBlocksGoal(AlienEntity alienEntity, TagKey<Block> blockTagKey, float range) {
        this.alienEntity = alienEntity;
        this.blockTagKey = blockTagKey;
        this.range = range;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (
            alienEntity.isPassenger() || alienEntity.isAggressive() || !alienEntity.level()
                .getGameRules()
                .getBoolean(
                    GameRules.RULE_MOBGRIEFING
                )
        ) {
            return false;
        }

        sightCounter--;
        if (sightCounter <= 0) {
            sightCounter = 2;

            final var rangeXZ = 12;
            final var rangeY = 6;
            var pos = new BlockPos.MutableBlockPos();
            for (var i = 0; i < 32; i++) {
                pos.set(
                    alienEntity.blockPosition().getX() + alienEntity.getRandom().nextInt(rangeXZ) - alienEntity.getRandom()
                        .nextInt(rangeXZ),
                    alienEntity.blockPosition().getY() + alienEntity.getRandom().nextInt(rangeY) - alienEntity.getRandom().nextInt(rangeY),
                    alienEntity.blockPosition().getZ() + alienEntity.getRandom().nextInt(rangeXZ) - alienEntity.getRandom().nextInt(rangeXZ)
                );
                if (tryTargetBlock(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (
            alienEntity.isPassenger() || targetBlock == null || alienEntity.level().getBlockState(targetPos).is(blockTagKey) || alienEntity
                .isAggressive() || !alienEntity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
        ) {
            return false;
        }

        return blockDamage > 0.0F || giveUpDelay < 400;
    }

    @Override
    public boolean isInterruptable() {
        return blockDamage <= 0.0F;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        targetHitResult = null;
        canReach = false;
        sightCounter = 0;
        giveUpDelay = 0;

        hitCounter = 0;
        blockDamage = 0.0F;
        lastBlockDamage = -1;

        pathToTarget();
    }

    @Override
    public void stop() {
        targetBlock = null;
        targetHitResult = null;
        canReach = false;
        sightCounter = 20;
        giveUpDelay = 0;
        hitCounter = 0;
        blockDamage = 0.0F;
        lastBlockDamage = -1;

        alienEntity.level().destroyBlockProgress(alienEntity.getId(), targetPos, -1);
    }

    @Override
    public void tick() {
        giveUpDelay++;

        if (targetPos != null) {
            alienEntity.getLookControl()
                .setLookAt(
                    targetPos.getX() + 0.5,
                    targetPos.getY() + 0.5,
                    targetPos.getZ() + 0.5,
                    30.0F,
                    30.0F
                );
        }

        if (canReach && targetHitResult != null && targetBlock != null) {
            performGriefing();
        } else {
            if (sightCounter-- <= 0) {
                sightCounter = 8 + alienEntity.getRandom().nextInt(5);
                if (checkSight()) {
                    sightCounter += 5;
                }
            }

            if (giveUpDelay > 400) {
                targetBlock = null;
            } else if (alienEntity.getNavigation().isDone()) {
                pathToTarget();
            }
        }
    }

    private void pathToTarget() {
        alienEntity.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 1.0);
    }

    private boolean tryTargetBlock(BlockPos pos) {
        var block = alienEntity.level().getBlockState(pos);
        if (block.isAir()) {
            return false;
        }

        return tryTargetBlockGriefing(block, pos);
    }

    private boolean checkSight() {
        var x = targetPos.getX() + 0.5;
        var y = targetPos.getY() + 0.5;
        var z = targetPos.getZ() + 0.5;
        if (alienEntity.distanceToSqr(x, y - alienEntity.getEyeHeight(), z) <= range * range) {
            var posVec = new Vec3(alienEntity.getX(), alienEntity.getY() + alienEntity.getEyeHeight(), alienEntity.getZ());

            if (
                checkSight(posVec, x, y + (alienEntity.getY() > y ? 0.5 : -0.5), z) ||
                    checkSight(posVec, x + (alienEntity.getX() > x ? 0.5 : -0.5), y, z) ||
                    checkSight(posVec, x, y, z + (alienEntity.getZ() > z ? 0.5 : -0.5))
            ) {
                canReach = true;
            }
            return true;
        }
        return false;
    }

    private boolean checkSight(final Vec3 posVec, double x, double y, double z) {
        final var targetVec = new Vec3(x, y, z);
        BlockHitResult hit = alienEntity.level()
            .clip(
                new ClipContext(
                    posVec,
                    targetVec,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    alienEntity
                )
            );

        if (HitResult.Type.MISS.equals(hit.getType())) {
            hit = new BlockHitResult(hit.getLocation(), hit.getDirection(), hit.getBlockPos(), hit.isInside());
        }

        if (
            targetPos.equals(hit.getBlockPos()) ||
                tryTargetObstructingBlock(hit)
        ) {
            targetHitResult = hit;
            return true;
        }
        return false;
    }

    private boolean tryTargetObstructingBlock(BlockHitResult hit) {
        var block = alienEntity.level().getBlockState(hit.getBlockPos());
        return tryTargetBlockGriefing(block, hit.getBlockPos());
    }

    private boolean tryTargetBlockGriefing(BlockState block, BlockPos pos) {
        if (block.is(blockTagKey)) {
            targetPos = pos.immutable();
            targetBlock = block;
            return true;
        }
        return false;
    }

    private void performGriefing() {
        if (alienEntity.getNavigation().isInProgress()) {
            alienEntity.getNavigation().stop();
        }

        var level = alienEntity.level();
        level.destroyBlock(targetPos, true);
        level.playSound(
            alienEntity,
            alienEntity.blockPosition(),
            targetBlock.getSoundType().getBreakSound(),
            SoundSource.BLOCKS,
            1.0F,
            1.0F
        );

        if (!alienEntity.swinging) {
            alienEntity.swing(alienEntity.getUsedItemHand());
            alienEntity.animationSelector.select(alienEntity);
        }
        blockDamage = 0.0F;
        targetBlock = null;
    }
}
