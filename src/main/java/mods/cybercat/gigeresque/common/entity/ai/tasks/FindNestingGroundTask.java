package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public class FindNestingGroundTask<E extends Mob> extends ExtendedBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
			Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
			Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
			Pair.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT));
	private boolean hasReachedNestingGround = false;
	private BlockPos targetPos = null;
	protected Function<E, Integer> attackIntervalSupplier = entity -> 20;

	public FindNestingGroundTask<E> attackInterval(Function<E, Integer> supplier) {
		this.attackIntervalSupplier = supplier;

		return this;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		return ((AdultAlienEntity) entity).getGrowth() == ((AdultAlienEntity) entity).getMaxGrowth()
				&& (!entity.getBrain().hasMemoryValue(MemoryModuleType.HOME) || !hasReachedNestingGround);
	}

	@Override
	protected void tick(ServerLevel level, E entity, long gameTime) {
		Level world = entity.getLevel();

		if (targetPos == null) {
			Vec3 targetVec = locateShadedPos(entity);
			if (targetVec == null)
				return;
			BlockPos targetPos = new BlockPos(targetVec);
			boolean canMoveTo = entity.getNavigation().moveTo(targetVec.x, targetVec.y, targetVec.z, 1.1F);

			if (!canMoveTo) {
				BlockPos offset = targetPos.offset(0, 10, 0);

				for (int i = 0; i < 20; i++) {
					BlockPos newPos = offset.offset(0, -i, 0);
					BlockPos downPos = newPos.below();

					if (world.getBlockState(newPos).isAir() && world.getBlockState(downPos).canOcclude()) {
						targetPos = newPos;

						if (entity.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.1F)) {
							break;
						}
					}
				}
			}

			this.targetPos = targetPos;
		} else {
			BlockPos targetPos = this.targetPos;
			if (targetPos.closerToCenterThan(entity.position(), 4.0)) {
				entity.getBrain().setMemory(MemoryModuleType.HOME, GlobalPos.of(world.dimension(), targetPos));
				hasReachedNestingGround = true;

				BlockPos resinPos;
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						resinPos = targetPos.offset(x, 0, z);
						BlockPos downPos = resinPos.below();

						boolean travelUp = !(world.getBlockState(resinPos).isAir()
								&& world.getBlockState(downPos).isAir());

						int i = 0;
						while (!world.getBlockState(resinPos).isAir() || !world.getBlockState(downPos).canOcclude()) {
							resinPos.offset(0, travelUp ? 1 : -1, 0);
							downPos.offset(0, travelUp ? 1 : -1, 0);

							i++;
							if (i > 4) { // Limit search to prevent infinite loop
								break;
							}
						}

						BlockState topState = world.getBlockState(resinPos);
						if (topState.isAir()) {
							BlockState downState = world.getBlockState(downPos);
							if (downState.getBlock() == GIgBlocks.NEST_RESIN) {
								world.setBlockAndUpdate(downPos, GIgBlocks.NEST_RESIN.defaultBlockState());
							} else if (downState.isSolidRender(world, downPos)) {
								world.setBlockAndUpdate(resinPos, GIgBlocks.NEST_RESIN.defaultBlockState());
							}
						} else if (world.getBlockState(resinPos) == GIgBlocks.NEST_RESIN.defaultBlockState()
								&& topState.isSolidRender(world, resinPos)) {
							world.setBlockAndUpdate(resinPos, GIgBlocks.NEST_RESIN.defaultBlockState());
						}
					}
				}
			}
		}
	}

	@Override
	protected void stop(E entity) {
		hasReachedNestingGround = false;
	}

	private Vec3 locateShadedPos(E entity) {
		RandomSource random = entity.getRandom();
		BlockPos blockPos = entity.blockPosition();

		if (!entity.level.canSeeSky(blockPos) && entity.level.getBlockState(blockPos).isAir()
				&& entity.level.getBlockState(blockPos.below()).canOcclude()
				&& entity.level.getBrightness(LightLayer.SKY, blockPos) < 4) {
			return Vec3.atBottomCenterOf(blockPos);
		}
		for (int i = 0; i < 10; i++) {
			BlockPos blockPos2 = blockPos.offset(random.nextInt(32) - 16, random.nextInt(6) - 3,
					random.nextInt(32) - 16);
			if (!entity.level.canSeeSky(blockPos2) && entity.level.getBlockState(blockPos2).isAir()
					&& entity.level.getBlockState(blockPos2.below()).canOcclude()
					&& entity.level.getBrightness(LightLayer.SKY, blockPos2) < 4) {
				return Vec3.atBottomCenterOf(blockPos2);
			}
		}
		return null;
	}
}