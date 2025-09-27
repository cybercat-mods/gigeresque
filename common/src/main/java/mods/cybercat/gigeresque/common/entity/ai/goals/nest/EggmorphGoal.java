package mods.cybercat.gigeresque.common.entity.ai.goals.nest;

import mod.azure.azurelib.sblforked.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class EggmorphGoal extends Goal {

    protected final AlienEntity mob;

    public EggmorphGoal(AlienEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.hasHomeBlock()
            && !this.mob.isVehicle()
            && !this.mob.climbingManager.climbing;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse()
            && !this.mob.isFleeing()
            && !this.mob.stasisManager.isStasis();
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public void tick() {
        if (this.mob.hasHomeBlock() && isPathfindable(this.mob.getHomeBlock())) {
            var blockPos = this.mob.getHomeBlock();
            var centerPos = Vec3.atCenterOf(blockPos);
            var blockCenter = Vec3.atCenterOf(blockPos);
            var entityPos = this.mob.position();
            var passenger = this.mob.getFirstPassenger();
            var test = RandomUtil.getRandomPositionWithinRange(this.mob.blockPosition(), 3, 1, 3, false, this.mob.level());

            this.mob.getNavigation().moveTo(centerPos.x, centerPos.y, centerPos.z, 0.8F);
        }
    }

    private boolean isPathfindable(BlockPos blockPos) {
        return this.mob.getNavigation().createPath(blockPos, 0) != null;
    }
}
