package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.BlockBreakProgressManager;

/**
 * Credit to Boston/AVP
 */
public class DigToTargetGoal extends Goal {

    private final AlienEntity mob;

    private final double reachDistance;

    private final double maxDistanceFromTarget;

    private final List<BlockPos> targetBlocks = new ArrayList<>();

    private BlockState blockState = null;

    private Vec3 lastPosition = null;

    private int lastPositionTickstamp = 0;

    public DigToTargetGoal(AlienEntity mob) {
        this(mob, 16);
    }

    public DigToTargetGoal(AlienEntity mob, double maxDistanceFromTarget) {
        this.mob = mob;
        this.reachDistance = 4;
        this.maxDistanceFromTarget = maxDistanceFromTarget * maxDistanceFromTarget;
    }

    @Override
    public boolean canUse() {
        if (!mob.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }

        if (mob.getTarget() == null) {
            return false;
        }

        return isStuck()
            && (mob.distanceToSqr(mob.getTarget()) > 2d || !mob.hasLineOfSight(mob.getTarget()))
            && mob.distanceToSqr(mob.getTarget()) < maxDistanceFromTarget;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetBlocks.isEmpty()) {
            return false;
        }

        var target = mob.getTarget();

        if (target == null || !target.isAlive()) {
            return false;
        }

        if (this.mob.isVehicle()) {
            return false;
        }

        return targetBlocks.getFirst().distSqr(mob.blockPosition()) < reachDistance * reachDistance
            && !mob.level().getBlockState(targetBlocks.getFirst()).isAir();
    }

    @Override
    public void start() {
        var target = mob.getTarget();

        if (target == null) {
            return;
        }

        gatherTargetBlocks();

        if (!targetBlocks.isEmpty()) {
            initBlockBreak();
            mob.setAggressive(true);
        }
    }

    @Override
    public void stop() {
        if (!targetBlocks.isEmpty()) {
            targetBlocks.clear();
        }

        this.blockState = null;
        this.lastPosition = null;
        this.mob.setAggressive(false);
    }

    @Override
    public void tick() {
        if (targetBlocks.isEmpty()) {
            return;
        }

        var target = mob.getTarget();
        var pos = targetBlocks.getFirst();
        mob.getLookControl().setLookAt(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);

        var attackAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        var damage = attackAttribute == null ? 10F : ((float) attackAttribute.getValue()) * 2F;

        if (mob.tickCount % 4 == 0 && mob.level().getBlockState(pos).is(GigTags.WEAK_BLOCKS)) {
            this.mob.animationSelector.select(this.mob);
            BlockBreakProgressManager.damage(mob.level(), pos, damage);

            var soundType = blockState.getSoundType();

            mob.level()
                .playSound(
                    null,
                    pos,
                    soundType.getHitSound(),
                    SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 8.0F,
                    soundType.getPitch() * 0.5F
                );
        } else if (mob.tickCount % 20 == 0) {
            this.mob.animationSelector.select(this.mob);
            var acidEntity = GigEntities.ACID.get().create(mob.level());
            acidEntity.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
            this.mob.level().addFreshEntity(acidEntity);
        }

        if (mob.level().getBlockState(pos).is(Blocks.AIR)) {
            targetBlocks.removeFirst();

            if (!targetBlocks.isEmpty()) {
                initBlockBreak();
            } else if (mob.distanceToSqr(target) > 2d && !mob.getSensing().hasLineOfSight(target)) {
                start();
            }
        }
    }

    private void initBlockBreak() {
        this.blockState = mob.level().getBlockState(targetBlocks.getFirst());
    }

    private void gatherTargetBlocks() {
        var target = mob.getTarget();
        int mobWidth = Mth.ceil(mob.getBbWidth());
        int mobHeight = Mth.ceil(mob.getBbHeight());

        for (var i = 0; i < mobHeight; i++) {
            for (var j = -mobWidth / 2; j <= mobWidth / 2; j++) { // Loop through the width
                for (var k = -mobWidth / 2; k <= mobWidth / 2; k++) {
                    var from = mob.position().add(j, i + 0.5d, k);
                    var to = target.getEyePosition(1f).add(j, i, k);
                    var clipContext = new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob);
                    var rayTraceResult = mob.level().clip(clipContext);

                    if (
                        rayTraceResult.getType() == HitResult.Type.MISS
                            || targetBlocks.contains(rayTraceResult.getBlockPos())
                            || rayTraceResult.getBlockPos().getY() > 320
                    ) {
                        continue;
                    }

                    double distance = mob.distanceToSqr(rayTraceResult.getLocation());

                    if (distance > reachDistance * reachDistance) {
                        continue;
                    }

                    BlockState state = mob.level().getBlockState(rayTraceResult.getBlockPos());

                    if (
                        state.hasBlockEntity()
                            || state.getDestroySpeed(mob.level(), rayTraceResult.getBlockPos()) == -1
                            || state.getBlock().defaultDestroyTime() >= Blocks.IRON_BLOCK.defaultDestroyTime()
                    ) {
                        continue;
                    }

                    targetBlocks.add(rayTraceResult.getBlockPos());
                }
            }
        }

        Collections.reverse(targetBlocks);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Returns true if the mob has been stuck in the same spot (radius 1.5 blocks) for more than 3 seconds
     */
    public boolean isStuck() {
        if (mob.getTarget() == null) {
            return false;
        }

        if (mob.distanceTo(mob.getTarget()) <= mob.getBbWidth()) {
            return false;
        }

        if (lastPosition == null || mob.distanceToSqr(lastPosition) > 2.25d) {
            this.lastPosition = mob.position();
            this.lastPositionTickstamp = mob.tickCount;
        }

        return mob.getNavigation().isDone() || mob.tickCount - lastPositionTickstamp >= 60;
    }
}
