package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class FindNestGoal extends MoveToTargetPosGoal {
	private final AdultAlienEntity stepAndDestroyMob;
	public static final Predicate<BlockState> NEST = state -> state.isOf(GIgBlocks.NEST_RESIN_WEB_CROSS);

	public FindNestGoal(AdultAlienEntity strider) {
		super(strider, 2.5D, 8, 2);
		this.stepAndDestroyMob = strider;
	}

	@Override
	public BlockPos getTargetPos() {
		return this.targetPos;
	}

	@Override
	public boolean shouldContinue() {
		return this.isTargetPos(this.stepAndDestroyMob.world, this.targetPos);
	}

	@Override
	public boolean canStart() {
		if (this.cooldown > 0) {
			--this.cooldown;
			return false;
		}
		this.cooldown = this.getInterval(this.mob);
		return stepAndDestroyMob.hasPassengers() && this.findTargetPos();
	}

	@Override
	public boolean shouldResetPath() {
		return this.tryingTime % 20 == 0;
	}

	@Override
	public void stop() {
		super.stop();
		stepAndDestroyMob.setIsBreaking(false);
	}

	@Override
	public void tick() {
		super.tick();
		World world = this.stepAndDestroyMob.world;
		BlockPos blockPos = this.stepAndDestroyMob.getBlockPos();
		BlockPos blockPos2 = this.tweakToProperPos(blockPos, world);
		this.stepAndDestroyMob.setAttacking(true);
		if (this.hasReached() && blockPos2 != null) {
			stepAndDestroyMob.removeAllPassengers();
			this.cooldown = -15;
		}
	}

	@Nullable
	private BlockPos tweakToProperPos(BlockPos pos, BlockView world) {
		Stream<BlockState> list = this.mob.world
				.getStatesInBoxIfLoaded(this.mob.getBoundingBox().expand(8.0, 8.0, 8.0));
		@SuppressWarnings("unused")
		BlockPos[] blockPoss;
		if (list.anyMatch(NEST)) {
			return pos;
		}
		for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.down(), pos.west(), pos.east(), pos.north(),
				pos.south(), pos.down().down() }) {
			if (!list.anyMatch(NEST))
				continue;
			return blockPos;
		}
		return null;
	}

	@Override
	protected boolean isTargetPos(WorldView world, BlockPos pos) {
		Stream<BlockState> list = this.mob.world
				.getStatesInBoxIfLoaded(this.mob.getBoundingBox().expand(8.0, 8.0, 8.0));
		return list.anyMatch(NEST);
	}
}