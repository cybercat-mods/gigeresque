package mods.cybercat.gigeresque.common.entity.ai.goal.classic;

import static java.lang.Math.max;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import net.minecraft.entity.ai.goal.Goal;

public class BuildNestGoal extends Goal {
	private final AdultAlienEntity mob;
	private int cooldown = 0;

	public BuildNestGoal(AdultAlienEntity strider) {
		this.mob = strider;
	}

	@Override
	public boolean shouldContinue() {
		return !mob.hasPassengers() && !mob.world.isSkyVisible(mob.getBlockPos()) && mob.world.getAmbientDarkness() <= 9
				&& !mob.world.getBlockState(mob.getBlockPos()).isOf(GIgBlocks.NEST_RESIN)
				&& !mob.world.getBlockState(mob.getBlockPos()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS)
				&& !mob.world.getBlockState(mob.getBlockPos().down()).isOf(GIgBlocks.NEST_RESIN)
				&& !mob.world.getBlockState(mob.getBlockPos().down()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS);
	}

	@Override
	public boolean canStart() {
		cooldown = max(cooldown - 1, 0);
		return !mob.hasPassengers() && !mob.world.isSkyVisible(mob.getBlockPos()) && mob.world.getAmbientDarkness() <= 9
				&& !mob.world.getBlockState(mob.getBlockPos()).isOf(GIgBlocks.NEST_RESIN)
				&& !mob.world.getBlockState(mob.getBlockPos()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS)
				&& !mob.world.getBlockState(mob.getBlockPos().down()).isOf(GIgBlocks.NEST_RESIN)
				&& !mob.world.getBlockState(mob.getBlockPos().down()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS);
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void tick() {
		super.tick();
		if (!mob.hasPassengers() && !mob.world.getBlockState(mob.getBlockPos()).isOf(GIgBlocks.NEST_RESIN)
				&& !mob.world.getBlockState(mob.getBlockPos()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS)
				&& !mob.world.getBlockState(mob.getBlockPos().down()).isOf(GIgBlocks.NEST_RESIN)
				&& !mob.world.getBlockState(mob.getBlockPos().down()).isOf(GIgBlocks.NEST_RESIN_WEB_CROSS))
			this.cooldown++;
		if (this.cooldown == 200)
			NestBuildingHelper.tryBuildNestAround(mob);

		if (this.cooldown >= 203)
			this.cooldown = -500;
	}
}
