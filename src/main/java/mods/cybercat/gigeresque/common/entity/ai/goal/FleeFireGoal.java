package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

public class FleeFireGoal<T extends LivingEntity> extends Goal {
	protected final PathfinderMob mob;
	@Nullable
	protected Path fleePath;
	protected final PathNavigation fleeingEntityNavigation;

	public FleeFireGoal(PathfinderMob mob) {
		this.mob = mob;
		this.fleeingEntityNavigation = mob.getNavigation();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public void tick() {
		Vec3i radius = new Vec3i(12, 12, 12);
		for (BlockPos testPos : BlockPos.betweenClosed(this.mob.blockPosition().subtract(radius),
				this.mob.blockPosition().offset(radius))) {
			@SuppressWarnings("unused")
			BlockState testState;

			if ((testState = this.mob.level.getBlockState(testPos)).is(BlockTags.FIRE)) {
				this.mob.getNavigation().moveTo(this.mob.getX() * 2, this.mob.getY() * 2, this.mob.getZ() * 2,
						3.15);
				this.mob.setAggressive(false);
			}
		}
	}

	@Override
	public boolean canUse() {
		Vec3i radius = new Vec3i(12, 12, 12);
		for (BlockPos testPos : BlockPos.betweenClosed(this.mob.blockPosition().subtract(radius),
				this.mob.blockPosition().offset(radius))) {
			@SuppressWarnings("unused")
			BlockState testState;

			if ((testState = this.mob.level.getBlockState(testPos)).is(BlockTags.FIRE)) {
				return true;
			}
		}
		return false;
	}

}
