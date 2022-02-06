package com.bvanseg.gigeresque.common.block;

import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NestResinWebFullBlockJava extends Block {
    public NestResinWebFullBlockJava(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof AlienEntityJava)) {
            entity.slowMovement(state, new Vec3d(0.25, 0.05000000074505806, 0.25));
        }
    }
}
