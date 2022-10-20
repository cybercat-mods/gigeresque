package mods.cybercat.gigeresque.common.entity.ai.goal.classic;

import static java.lang.Math.max;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.nest.NestBuildingHelper;
import net.minecraft.world.entity.ai.goal.Goal;

public class BuildNestGoal extends Goal {
	private final AdultAlienEntity mob;
	private int coolbelow = 0;

	public BuildNestGoal(AdultAlienEntity strider) {
		this.mob = strider;
	}

	@Override
	public boolean canContinueToUse() {
		return !mob.isAggressive() && !mob.isVehicle() && !mob.level.canSeeSky(mob.blockPosition())
				&& mob.level.getSkyDarken() <= 9
				&& !mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN)
				&& !mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN_WEB_CROSS)
				&& !mob.level.getBlockState(mob.blockPosition().below()).is(GIgBlocks.NEST_RESIN)
				&& !mob.level.getBlockState(mob.blockPosition().below()).is(GIgBlocks.NEST_RESIN_WEB_CROSS);
	}

	@Override
	public boolean canUse() {
		coolbelow = max(coolbelow - 1, 0);
		return !mob.isAggressive() && !mob.isVehicle() && !mob.level.canSeeSky(mob.blockPosition())
				&& mob.level.getSkyDarken() <= 9
				&& !mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN)
				&& !mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN_WEB_CROSS)
				&& !mob.level.getBlockState(mob.blockPosition().below()).is(GIgBlocks.NEST_RESIN)
				&& !mob.level.getBlockState(mob.blockPosition().below()).is(GIgBlocks.NEST_RESIN_WEB_CROSS);
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
		if (!mob.isVehicle() && !mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN)
				&& !mob.level.getBlockState(mob.blockPosition()).is(GIgBlocks.NEST_RESIN_WEB_CROSS)
				&& !mob.level.getBlockState(mob.blockPosition().below()).is(GIgBlocks.NEST_RESIN)
				&& !mob.level.getBlockState(mob.blockPosition().below()).is(GIgBlocks.NEST_RESIN_WEB_CROSS))
			this.coolbelow++;
		if (this.coolbelow == 200)
			NestBuildingHelper.tryBuildNestAround(mob);

		if (this.coolbelow >= 203)
			this.coolbelow = -500;
	}
}
