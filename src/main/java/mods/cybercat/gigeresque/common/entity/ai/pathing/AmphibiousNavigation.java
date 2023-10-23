package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;

public class AmphibiousNavigation extends WaterBoundPathNavigation {
    public AmphibiousNavigation(Mob entity, Level world) {
        super(entity, world);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected PathFinder createPathFinder(int range) {
        nodeEvaluator = new AmphibiousNodeEvaluator(true);
        nodeEvaluator.setCanOpenDoors(false);
        nodeEvaluator.setCanPassDoors(false);
        return new PathFinder(nodeEvaluator, range);
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !level.getBlockState(pos.below()).isAir();
    }
}
