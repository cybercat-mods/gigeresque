package com.bvanseg.gigeresque.common.entity.ai.pathing;

import net.minecraft.entity.ai.pathing.AmphibiousPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmphibiousNavigation extends SwimNavigation {
    public AmphibiousNavigation(MobEntity entity, World world) {
        super(entity,world);
    }

    @Override
    protected boolean isAtValidPosition() {
        return true;
    }

    @Override
    protected PathNodeNavigator createPathNodeNavigator(int range) {
        nodeMaker = new AmphibiousPathNodeMaker(true);
        nodeMaker.setCanOpenDoors(false);
        nodeMaker.setCanEnterOpenDoors(false);
        return new PathNodeNavigator(nodeMaker, range);
    }

    @Override
    public boolean isValidPosition(BlockPos pos) {
        return !world.getBlockState(pos.down()).isAir();
    }
}
