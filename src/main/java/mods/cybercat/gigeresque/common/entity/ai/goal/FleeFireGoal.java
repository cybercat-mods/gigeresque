package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.EnumSet;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

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
		float q = 12.0F;
		int k = MathHelper.floor(mob.getX() - (double) q - 1.0D);
		int l = MathHelper.floor(mob.getX() + (double) q + 1.0D);
		int t = MathHelper.floor(mob.getY() - (double) q - 1.0D);
		int u = MathHelper.floor(mob.getY() + (double) q + 1.0D);
		int v = MathHelper.floor(mob.getZ() - (double) q - 1.0D);
		int w = MathHelper.floor(mob.getZ() + (double) q + 1.0D);
		Stream<BlockState> list = mob.getWorld().getStatesInBoxIfLoaded(
				new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
		if (list.anyMatch(state -> state.isOf(Blocks.FIRE))) {
			this.mob.getNavigation().startMovingTo(this.mob.getX() - 15, this.mob.getY() - 15, this.mob.getZ() - 15, 3.15);
			this.mob.setAttacking(false);
		} 
	}

	@Override
	public boolean canStart() {
		float q = 12.0F;
		int k = MathHelper.floor(mob.getX() - (double) q - 1.0D);
		int l = MathHelper.floor(mob.getX() + (double) q + 1.0D);
		int t = MathHelper.floor(mob.getY() - (double) q - 1.0D);
		int u = MathHelper.floor(mob.getY() + (double) q + 1.0D);
		int v = MathHelper.floor(mob.getZ() - (double) q - 1.0D);
		int w = MathHelper.floor(mob.getZ() + (double) q + 1.0D);
		Stream<BlockState> list = mob.getWorld().getStatesInBoxIfLoaded(
				new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
		return list.anyMatch(state -> state.isOf(Blocks.FIRE));
	}

}
