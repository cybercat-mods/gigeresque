package mods.cybercat.gigeresque.common.entity.ai.goal;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class KillLightsGoal extends MoveToTargetPosGoal {
	private final AdultAlienEntity stepAndDestroyMob;

	public KillLightsGoal(AdultAlienEntity strider) {
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
		return this.findTargetPos();
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
			world.breakBlock(blockPos2, false);
			stepAndDestroyMob.setIsBreaking(true);
			this.cooldown = -15;
		}
	}

	@Nullable
	private BlockPos tweakToProperPos(BlockPos pos, BlockView world) {
		@SuppressWarnings("unused")
		BlockPos[] blockPoss;
		if (world.getBlockState(pos).isIn(GigTags.DESTRUCTIBLE_LIGHT)) {
			return pos;
		}
		for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.down(), pos.west(), pos.east(), pos.north(),
				pos.south(), pos.down().down() }) {
			if (!world.getBlockState(blockPos).isIn(GigTags.DESTRUCTIBLE_LIGHT))
				continue;
			return blockPos;
		}
		return null;
	}

	@Override
	protected boolean isTargetPos(WorldView world, BlockPos pos) {
		return world.getBlockState(pos).isIn(GigTags.DESTRUCTIBLE_LIGHT)
				&& world.getBlockState(pos.up()).canPathfindThrough(world, pos, NavigationType.LAND);
	}
}