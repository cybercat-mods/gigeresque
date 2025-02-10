package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class StrollAroundInWaterGoal extends Goal {

    private final PathfinderMob mob;

    private double wantedX;

    private double wantedY;

    private double wantedZ;

    private final double speedModifier;

    private final Level level;

    public StrollAroundInWaterGoal(PathfinderMob pathfinderMob, double d) {
        this.mob = pathfinderMob;
        this.speedModifier = d;
        this.level = pathfinderMob.level();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != null || !mob.isUnderWater()) {
            return false;
        }

        var vec3 = getWaterPos();

        if (vec3 == null) {
            return false;
        } else {
            wantedX = vec3.x;
            wantedY = vec3.y;
            wantedZ = vec3.z;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getTarget() == null && !mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(wantedX, wantedY, wantedZ, speedModifier);
    }

    @Nullable
    private Vec3 getWaterPos() {
        var randomSource = mob.getRandom();
        var blockPos = mob.blockPosition();

        for (var i = 0; i < 10; i++) {
            var blockPos2 = blockPos.offset(randomSource.nextInt(20) - 10, 2 - randomSource.nextInt(8), randomSource.nextInt(20) - 10);

            if (level.getBlockState(blockPos2).is(Blocks.WATER)) {
                return Vec3.atBottomCenterOf(blockPos2);
            }
        }

        return null;
    }
}
