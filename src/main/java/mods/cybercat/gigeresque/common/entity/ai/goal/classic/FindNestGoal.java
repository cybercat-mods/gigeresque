package mods.cybercat.gigeresque.common.entity.ai.goal.classic;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class FindNestGoal extends MoveToBlockGoal {
	private final AdultAlienEntity mob;

	public FindNestGoal(AdultAlienEntity strider) {
		super(strider, 2.5D, 8, 2);
		this.mob = strider;
	}

	@Override
	public BlockPos getMoveToTarget() {
		return this.blockPos;
	}

	@Override
	public boolean canContinueToUse() {
		return mob.isVehicle() && this.isValidTarget(this.mob.level, this.blockPos);
	}

	@Override
	public boolean canUse() {
		if (this.nextStartTick > 0) {
			--this.nextStartTick;
			return false;
		}
		this.nextStartTick = this.nextStartTick(this.mob);
		return mob.isVehicle() && this.findNearestBlock();
	}

	@Override
	public boolean shouldRecalculatePath() {
		return this.tryTicks % 20 == 0;
	}

	@Override
	public void stop() {
		super.stop();
		this.nextStartTick = 0;
		mob.getNavigation().stop();
	}

	@Override
	public void tick() {
		super.tick();
		Level level = this.mob.level;
		BlockPos blockPos = this.mob.blockPosition();
		BlockPos blockPos2 = this.tweakToProperPos(blockPos, level);
		this.mob.setAggressive(true);
		if (this.isReachedTarget() && blockPos2 != null && mob.isVehicle()) {
			this.nextStartTick++;
			mob.getFirstPassenger().setInvisible(false);
			if (this.nextStartTick > 10) {
				mob.getFirstPassenger().setPos(mob.getX(), mob.getY()+0.2, mob.getZ());
				mob.getFirstPassenger().removeVehicle();
			}
			if (this.nextStartTick >= 13)
				this.nextStartTick = 0;
		}
	}

	@Override
	protected boolean isReachedTarget() {
		return mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN_WEB_CROSS);
	}

	@Nullable
	private BlockPos tweakToProperPos(BlockPos pos, BlockGetter level) {
		Vec3i radius = new Vec3i(12, 12, 12);
		for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.subtract(radius))) {
			@SuppressWarnings("unused")
			BlockState testState;

			if ((testState = level.getBlockState(testPos)).is(GIgBlocks.NEST_RESIN_WEB_CROSS)) {
				return testPos;
			}
		}
		return null;
	}

	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos) {
		return level.getBlockState(pos).is(GIgBlocks.NEST_RESIN_WEB_CROSS);
	}
}