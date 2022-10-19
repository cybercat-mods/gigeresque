package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class FleeFireGoal<T extends LivingEntity> extends Goal {
	protected final PathAwareEntity mob;
	@Nullable
	protected Path fleePath;
	protected final EntityNavigation fleeingEntityNavigation;

	public FleeFireGoal(PathAwareEntity mob) {
		this.mob = mob;
		this.fleeingEntityNavigation = mob.getNavigation();
		this.setControls(EnumSet.of(Goal.Control.MOVE));
	}

	@Override
	public void tick() {
		Vec3i radius = new Vec3i(12, 12, 12);
		for (BlockPos testPos : BlockPos.iterate(this.mob.getBlockPos().subtract(radius),
				this.mob.getBlockPos().add(radius))) {
			@SuppressWarnings("unused")
			BlockState testState;

			if ((testState = this.mob.world.getBlockState(testPos)).isIn(BlockTags.FIRE)) {
				this.mob.getNavigation().startMovingTo(this.mob.getX() - 15, this.mob.getY() - 15, this.mob.getZ() - 15,
						3.15);
				this.mob.setAttacking(false);
			}
		}
	}

	@Override
	public boolean canStart() {
		Vec3i radius = new Vec3i(12, 12, 12);
		for (BlockPos testPos : BlockPos.iterate(this.mob.getBlockPos().subtract(radius),
				this.mob.getBlockPos().add(radius))) {
			@SuppressWarnings("unused")
			BlockState testState;

			if ((testState = this.mob.world.getBlockState(testPos)).isIn(BlockTags.FIRE)) {
				return true;
			}
		}
		return false;
	}

}
