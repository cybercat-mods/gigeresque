package mods.cybercat.gigeresque.common.entity.ai.goal.classic;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class KillLightsGoal extends MoveToBlockGoal {
	private final AdultAlienEntity stepAndDestroyMob;

	public KillLightsGoal(AdultAlienEntity strider) {
		super(strider, 2.5D, 32, 2);
		this.stepAndDestroyMob = strider;
	}

	@Override
	public BlockPos getMoveToTarget() {
		return this.blockPos;
	}

	@Override
	public boolean canContinueToUse() {
		return this.isValidTarget(this.stepAndDestroyMob.level, this.blockPos);
	}

	@Override
	public boolean canUse() {
		if (this.nextStartTick > 0) {
			--this.nextStartTick;
			return false;
		}
		if (stepAndDestroyMob.isAggressive())
			return false;
		this.nextStartTick = this.nextStartTick(this.mob);
		return this.findNearestBlock();
	}

	@Override
	public boolean shouldRecalculatePath() {
		return false;
	}

	@Override
	public void stop() {
		super.stop();
		stepAndDestroyMob.setIsBreaking(false);
	}

	@Override
	public void tick() {
		super.tick();
		Level level = this.stepAndDestroyMob.level;
		BlockPos blockPos = this.stepAndDestroyMob.blockPosition();
		BlockPos blockPos2 = this.tweakToProperPos(blockPos, level);
		this.stepAndDestroyMob.setAggressive(true);
		if (this.isReachedTarget() && blockPos2 != null) {
			level.destroyBlock(blockPos2, false);
			stepAndDestroyMob.setIsBreaking(true);
			this.nextStartTick = 0;
		}
	}

	@Nullable
	private BlockPos tweakToProperPos(BlockPos pos, BlockGetter level) {
		Vec3i radius = new Vec3i(2, 2, 2);
		for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.subtract(radius))) {
			@SuppressWarnings("unused")
			BlockState testState;

			if ((testState = level.getBlockState(testPos)).is(GigTags.DESTRUCTIBLE_LIGHT)) {
				return testPos;
			}
		}
		return null;
	}

	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos) {
		return level.getBlockState(pos).is(GigTags.DESTRUCTIBLE_LIGHT)
				&& level.getBlockState(pos.above()).isPathfindable(level, pos, PathComputationType.LAND);
	}
}