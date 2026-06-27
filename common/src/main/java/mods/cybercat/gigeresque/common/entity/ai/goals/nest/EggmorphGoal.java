package mods.cybercat.gigeresque.common.entity.ai.goals.nest;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class EggmorphGoal extends Goal {

    protected final AlienEntity mob;

    private int navigationTimer = 0;

    private BlockPos cachedTarget = null;

    private int searchTimer = 0;

    private int canUseSearchTimer = 0;

    private int carryTimer = 0;

    private Entity currentPassenger = null;

    private final Set<BlockPos> unreachableTargets = new HashSet<>();

    public EggmorphGoal(AlienEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.noneOf(Goal.Flag.class));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isVehicle())
            return false;
        canUseSearchTimer++;
        if (canUseSearchTimer >= 20 || cachedTarget == null) {
            canUseSearchTimer = 0;
            cachedTarget = findNearestWebBlock();
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isVehicle() && !this.mob.stasisManager.isStasis();
    }

    @Override
    public void start() {
        cachedTarget = findNearestWebBlock();
        navigationTimer = 0;
        searchTimer = 0;
        canUseSearchTimer = 0;
        carryTimer = 0;
        this.mob.allowExecution = false;
        currentPassenger = this.mob.getFirstPassenger();
        unreachableTargets.clear();
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        cachedTarget = null;
        canUseSearchTimer = 0;
        carryTimer = 0;
        this.mob.allowExecution = false;
        if (currentPassenger instanceof Mob mob) {
            mob.setNoAi(false);
        }
        currentPassenger = null;
        unreachableTargets.clear();
    }

    @Override
    public void tick() {
        carryTimer++;

        searchTimer++;
        if (searchTimer >= 60 || cachedTarget == null) {
            BlockPos newTarget = findNearestWebBlock();
            if (newTarget != null && cachedTarget == null) {
                carryTimer = 0;
            }
            cachedTarget = newTarget;
            searchTimer = 0;
        }

        int killTimer = cachedTarget == null ? 10 : 300;

        if (carryTimer >= killTimer) {
            this.mob.getNavigation().stop();
            this.mob.setSpeed(0.0F);

            if (carryTimer == killTimer + 20) {
                this.mob.animationDispatcher.sendExecution();
                this.mob.setIsExecuting(true);
                this.mob.setIsBiting(true);
                this.mob.level()
                    .playSound(
                        null,
                        this.mob.blockPosition(),
                        GigSounds.ALIEN_HEADBITE.get(),
                        this.mob.getSoundSource(),
                        1.0F,
                        1.0F
                    );
            }

            if (carryTimer == killTimer + 60) {
                this.mob.heal(50);
                if (this.mob.getFirstPassenger() != null) {
                    this.mob.getFirstPassenger()
                        .hurt(
                            GigDamageSources.of(this.mob.level(), GigDamageSources.EXECUTION),
                            Integer.MAX_VALUE
                        );
                }
                this.mob.setIsExecuting(false);
                this.mob.setIsBiting(false);
                this.mob.allowExecution = false;
                carryTimer = 0;
            }
            return;
        }

        if (cachedTarget == null)
            return;

        Vec3 centerPos = Vec3.atCenterOf(cachedTarget);
        double distanceSq = this.mob.distanceToSqr(centerPos);

        if (distanceSq > 2.25D) {
            navigationTimer = 0;
            if (isPathfindable(cachedTarget)) {
                this.mob.getNavigation()
                    .moveTo(
                        centerPos.x,
                        centerPos.y,
                        centerPos.z,
                        1.2F
                    );
            } else {
                BlockPos mobPos = this.mob.blockPosition();
                Vec3 direction = Vec3.atCenterOf(cachedTarget)
                    .subtract(Vec3.atCenterOf(mobPos))
                    .normalize();
                BlockPos blockToBreak = BlockPos.containing(
                    mobPos.getX() + direction.x,
                    mobPos.getY() + direction.y,
                    mobPos.getZ() + direction.z
                );

                if (!this.mob.level().getBlockState(blockToBreak).isAir()) {
                    if (!(this.mob.level() instanceof ServerLevel serverLevel2))
                        return;
                    var blockState = serverLevel2.getBlockState(blockToBreak);
                    if (blockState.getDestroySpeed(serverLevel2, blockToBreak) >= 0) {
                        serverLevel2.destroyBlock(blockToBreak, false);
                    }

                } else {
                    unreachableTargets.add(cachedTarget);
                    cachedTarget = findNearestWebBlock();
                }
            }
        } else {
            this.mob.getNavigation().stop();
            if (this.mob.isVehicle() && this.mob.level() instanceof ServerLevel serverLevel) {
                Entity passenger = this.mob.getPassengers().get(0);
                GigEntityUtils.placeInNest(serverLevel, this.mob, passenger);
                carryTimer = 0;
                this.mob.allowExecution = false;
            }
            cachedTarget = null;
        }
    }

    private BlockPos findNearestWebBlock() {
        BlockPos mobPos = this.mob.blockPosition();
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;

        for (
            BlockPos pos : BlockPos.betweenClosed(
                mobPos.offset(-32, -8, -32),
                mobPos.offset(32, 8, 32)
            )
        ) {
            if (this.mob.level().getBlockState(pos).is(GigBlocks.NEST_RESIN_WEB_CROSS.get())) {
                if (this.mob.level().getBlockState(pos.below()).isSolid()) {
                    if (unreachableTargets.contains(pos.immutable()))
                        continue;
                    double dist = mobPos.distSqr(pos);
                    if (dist < nearestDist) {
                        nearestDist = dist;
                        nearest = pos.immutable();
                    }
                }
            }
        }
        return nearest;
    }

    private boolean isPathfindable(BlockPos blockPos) {
        return this.mob.getNavigation().createPath(blockPos, 0) != null;
    }
}
