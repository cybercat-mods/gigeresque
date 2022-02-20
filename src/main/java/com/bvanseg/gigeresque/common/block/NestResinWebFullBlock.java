package com.bvanseg.gigeresque.common.block;

import com.bvanseg.gigeresque.common.entity.AlienEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NestResinWebFullBlock extends Block {
	public NestResinWebFullBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!(entity instanceof AlienEntity)) {
			entity.slowMovement(state, new Vec3d(0.25, 0.05000000074505806, 0.25));
		}
	}
}
