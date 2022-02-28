package mods.cybercat.gigeresque.common.entity.ai.brain.sensor;

import java.util.Optional;
import java.util.Set;

import mods.cybercat.gigeresque.common.block.Blocks;
import mods.cybercat.gigeresque.common.entity.ai.brain.memory.MemoryModuleTypes;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class NearestAlienWebbingSensor<E extends LivingEntity> extends Sensor<E> {
	private Optional<BlockPos> findAlienWebbing(ServerWorld world, E alien) {
		return BlockPos.findClosest(alien.getBlockPos(), 8, 3, pos -> isAlienWebbing(alien, world, pos));
	}

	private boolean isAlienWebbing(E alien, ServerWorld world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return blockState.getBlock() == Blocks.NEST_RESIN_WEB_CROSS
				&& (world.getBlockState(pos.up()).isAir()
						|| NestBuildingHelper.isResinBlock(world.getBlockState(pos.up()).getBlock()))
				&& world.getBlockState(pos.down()).isOpaqueFullCube(world, pos)
				&& world.getOtherEntities(alien, new Box(pos)).isEmpty();
	}

	@Override
	public Set<MemoryModuleType<?>> getOutputMemoryModules() {
		return ImmutableSet.of(MemoryModuleTypes.NEAREST_ALIEN_WEBBING);
	}

	@Override
	protected void sense(ServerWorld world, E alien) {
		Brain<?> brain = alien.getBrain();
		brain.remember(MemoryModuleTypes.NEAREST_ALIEN_WEBBING, findAlienWebbing(world, alien));
	}
}
