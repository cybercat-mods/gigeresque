package mods.cybercat.gigeresque.common.entity.helper;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.interfacing.IClimberEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class AdvancedGroundPathNavigator<T extends Mob & IClimberEntity> extends GroundPathNavigation {
	protected AdvancedPathFinder pathFinder;
	protected long lastTimeUpdated;
	protected BlockPos targetPos;
	protected final T advancedPathFindingEntity;
	protected final boolean checkObstructions;
	protected int stuckCheckTicks = 0;
	protected int checkpointRange;

	public AdvancedGroundPathNavigator(T entity, Level worldIn) {
		this(entity, worldIn, true);
	}

	public AdvancedGroundPathNavigator(T entity, Level worldIn, boolean checkObstructions) {
		super(entity, worldIn);
		this.advancedPathFindingEntity = entity;
		this.checkObstructions = checkObstructions;
		if (this.nodeEvaluator instanceof AdvancedWalkNodeProcessor processor)
			processor.setCheckObstructions(checkObstructions);
	}

	public AdvancedPathFinder getAssignedPathFinder() {
		return this.pathFinder;
	}

	@Override
	protected final PathFinder createPathFinder(int maxExpansions) {
		this.pathFinder = this.createAdvancedPathFinder(maxExpansions);
		this.nodeEvaluator = this.pathFinder.getNodeProcessor();
		return this.pathFinder;
	}

	protected AdvancedPathFinder createAdvancedPathFinder(int maxExpansions) {
		var nodeProcessor = new AdvancedWalkNodeProcessor();
		nodeProcessor.setCanPassDoors(true);
		return new AdvancedPathFinder(nodeProcessor, maxExpansions);
	}

	@Nullable
	@Override
	protected Path createPath(Set<BlockPos> waypoints, int padding, boolean startAbove, int checkpointRange) {
		// Offset waypoints according to entity's size so that the lower AABB corner is at the offset waypoint and center is at the original waypoint
		Set<BlockPos> adjustedWaypoints = new HashSet<>();
		for (var pos : waypoints)
			adjustedWaypoints.add(pos.offset(-Mth.ceil(this.mob.getBbWidth()) + 1, -Mth.ceil(this.mob.getBbHeight()) + 1, -Mth.ceil(this.mob.getBbWidth()) + 1));
		var path = super.createPath(adjustedWaypoints, padding, startAbove, checkpointRange);
		if (path != null && path.getTarget() != null)
			this.checkpointRange = checkpointRange;
		return path;
	}

	@Override
	public void recomputePath() {
		if (this.level.getGameTime() - this.lastTimeUpdated > 20L)
			if (this.targetPos != null) {
				this.path = null;
				this.path = this.createPath(this.targetPos, this.checkpointRange);
				this.lastTimeUpdated = this.level.getGameTime();
				this.hasDelayedRecomputation = false;
			} else
				this.hasDelayedRecomputation = true;
	}

	@Override
	protected void doStuckDetection(Vec3 entityPos) {
		super.doStuckDetection(entityPos);

		if (this.checkObstructions && this.path != null && !this.path.isDone()) {
			var target = this.path.getEntityPosAtNode(this.advancedPathFindingEntity, Math.min(this.path.getNodeCount() - 1, this.path.getNextNodeIndex() + 0));
			var diff = target.subtract(entityPos);
			var axis = 0;
			double maxDiff = 0;
			for (int i = 0; i < 3; i++) {
				double d;
				switch (i) {
				default:
				case 0:
					d = Math.abs(diff.x);
					break;
				case 1:
					d = Math.abs(diff.y);
					break;
				case 2:
					d = Math.abs(diff.z);
					break;
				}

				if (d > maxDiff) {
					axis = i;
					maxDiff = d;
				}
			}

			var height = Mth.floor(this.advancedPathFindingEntity.getBbHeight() + 1.0F);
			var ceilHalfWidth = Mth.ceil(this.advancedPathFindingEntity.getBbWidth() / 2.0f + 0.05F);

			Vec3 checkPos;
			switch (axis) {
			default:
			case 0:
				checkPos = new Vec3(entityPos.x + Math.signum(diff.x) * ceilHalfWidth, entityPos.y, target.z);
				break;
			case 1:
				checkPos = new Vec3(entityPos.x, entityPos.y + (diff.y > 0 ? (height + 1) : -1), target.z);
				break;
			case 2:
				checkPos = new Vec3(target.x, entityPos.y, entityPos.z + Math.signum(diff.z) * ceilHalfWidth);
				break;
			}

			var facingDiff = checkPos.subtract(entityPos.add(0, axis == 1 ? this.mob.getBbHeight() / 2 : 0, 0));
			var facing = Direction.getNearest((float) facingDiff.x, (float) facingDiff.y, (float) facingDiff.z);
			var blocked = false;
			var checkBox = this.advancedPathFindingEntity.getBoundingBox().expandTowards(Math.signum(diff.x) * 0.2D, Math.signum(diff.y) * 0.2D, Math.signum(diff.z) * 0.2D);

			loop: for (var yo = 0; yo < height; yo++)
				for (var xzo = -ceilHalfWidth; xzo <= ceilHalfWidth; xzo++) {
					var pos = Constants.blockPos(checkPos.x + (axis != 0 ? xzo : 0), checkPos.y + (axis != 1 ? yo : 0), checkPos.z + (axis != 2 ? xzo : 0));
					var state = this.advancedPathFindingEntity.level().getBlockState(pos);
					var nodeType = state.isPathfindable(this.advancedPathFindingEntity.level(), pos, PathComputationType.LAND) ? BlockPathTypes.OPEN : BlockPathTypes.BLOCKED;

					if (nodeType == BlockPathTypes.BLOCKED) {
						var collisionShape = state.getShape(this.advancedPathFindingEntity.level(), pos, CollisionContext.of(this.advancedPathFindingEntity)).move(pos.getX(), pos.getY(), pos.getZ());
						if (collisionShape != null && collisionShape.toAabbs().stream().anyMatch(aabb -> aabb.intersects(checkBox))) {
							blocked = true;
							break loop;
						}
					}
				}

			if (blocked) {
				this.stuckCheckTicks++;
				if (this.stuckCheckTicks > this.advancedPathFindingEntity.getMaxStuckCheckTicks()) {
					this.advancedPathFindingEntity.onPathingObstructed(facing);
					this.stuckCheckTicks = 0;
				}
			} else
				this.stuckCheckTicks = Math.max(this.stuckCheckTicks - 2, 0);
		} else
			this.stuckCheckTicks = Math.max(this.stuckCheckTicks - 4, 0);
	}
}