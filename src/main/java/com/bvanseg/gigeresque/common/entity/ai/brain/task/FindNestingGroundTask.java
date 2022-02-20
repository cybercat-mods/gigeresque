package com.bvanseg.gigeresque.common.entity.ai.brain.task;

import java.util.Random;

import com.bvanseg.gigeresque.common.block.Blocks;
import com.bvanseg.gigeresque.common.block.NestResinBlock;
import com.bvanseg.gigeresque.common.entity.AlienEntity;
import com.bvanseg.gigeresque.common.entity.impl.AdultAlienEntity;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class FindNestingGroundTask extends Task<AdultAlienEntity> {
	private final double speed;
	private boolean hasReachedNestingGround = false;
	private BlockPos targetPos = null;

	public FindNestingGroundTask(double speed) {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.WALK_TARGET,
				MemoryModuleState.VALUE_ABSENT, MemoryModuleType.HOME, MemoryModuleState.VALUE_ABSENT));
		this.speed = speed;
	}

	@Override
	protected boolean shouldRun(ServerWorld serverWorld, AdultAlienEntity alien) {
		return alien.getGrowth() == alien.getMaxGrowth()
				&& (!alien.getBrain().hasMemoryModule(MemoryModuleType.HOME) || !hasReachedNestingGround);
	}

	@Override
	protected void run(ServerWorld serverWorld, AdultAlienEntity alien, long l) {
		World world = alien.getWorld();

		if (targetPos == null) {
			Vec3d targetVec = locateShadedPos(alien);
			if (targetVec == null)
				return;
			BlockPos targetPos = new BlockPos(targetVec);
			boolean canMoveTo = alien.getNavigation().startMovingTo(targetVec.x, targetVec.y, targetVec.z, speed);

			if (!canMoveTo) {
				BlockPos offset = targetPos.add(0, 10, 0);

				for (int i = 0; i < 20; i++) {
					BlockPos newPos = offset.add(0, -i, 0);
					BlockPos downPos = newPos.down();

					if (world.getBlockState(newPos).isAir() && world.getBlockState(downPos).isOpaque()) {
						targetPos = newPos;

						if (alien.getNavigation().startMovingTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(),
								speed)) {
							break;
						}
					}
				}
			}

			this.targetPos = targetPos;
		} else {
			BlockPos targetPos = this.targetPos;
			if (targetPos.isWithinDistance(alien.getPos(), 4.0)) {
				alien.getBrain().remember(MemoryModuleType.HOME, GlobalPos.create(world.getRegistryKey(), targetPos));
				hasReachedNestingGround = true;

				BlockPos resinPos;
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						resinPos = targetPos.add(x, 0, z);
						BlockPos downPos = resinPos.down();

						boolean travelUp = !(world.getBlockState(resinPos).isAir()
								&& world.getBlockState(downPos).isAir());

						int i = 0;
						while (!world.getBlockState(resinPos).isAir() || !world.getBlockState(downPos).isOpaque()) {
							resinPos.add(0, travelUp ? 1 : -1, 0);
							downPos.add(0, travelUp ? 1 : -1, 0);

							i++;
							if (i > 4) { // Limit search to prevent infinite loop
								break;
							}
						}

						BlockState topState = world.getBlockState(resinPos);
						if (topState.isAir()) {
							BlockState downState = world.getBlockState(downPos);
							if (downState.getBlock() == Blocks.NEST_RESIN) {
								world.setBlockState(downPos, downState.with(NestResinBlock.LAYERS,
										downState.get(NestResinBlock.LAYERS) + 1));
							} else if (downState.isOpaqueFullCube(world, downPos)) {
								world.setBlockState(resinPos, Blocks.NEST_RESIN.getDefaultState());
							}
						} else if (world.getBlockState(resinPos) == Blocks.NEST_RESIN.getDefaultState()
								&& topState.isOpaqueFullCube(world, resinPos)) {
							world.setBlockState(resinPos,
									topState.with(NestResinBlock.LAYERS, topState.get(NestResinBlock.LAYERS) + 1));
						}
					}
				}
			}
		}
	}

	@Override
	public void finishRunning(ServerWorld world, AdultAlienEntity entity, long time) {
		super.finishRunning(world, entity, time);
		hasReachedNestingGround = false;
	}

	private Vec3d locateShadedPos(AlienEntity entity) {
		Random random = entity.getRandom();
		BlockPos blockPos = entity.getBlockPos();

		if (!entity.world.isSkyVisible(blockPos) && entity.world.getBlockState(blockPos).isAir()
				&& entity.world.getBlockState(blockPos.down()).isOpaque()
				&& entity.world.getLightLevel(LightType.SKY, blockPos) < 4) {
			return Vec3d.ofBottomCenter(blockPos);
		}
		for (int i = 0; i < 10; i++) {
			BlockPos blockPos2 = blockPos.add(random.nextInt(32) - 16, random.nextInt(6) - 3, random.nextInt(32) - 16);
			if (!entity.world.isSkyVisible(blockPos2) && entity.world.getBlockState(blockPos2).isAir()
					&& entity.world.getBlockState(blockPos2.down()).isOpaque()
					&& entity.world.getLightLevel(LightType.SKY, blockPos2) < 4) {
				return Vec3d.ofBottomCenter(blockPos2);
			}
		}
		return null;
	}
}
