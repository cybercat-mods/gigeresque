package mods.cybercat.gigeresque.common.entity.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;

public class AdvancedPathFinder extends CustomPathFinder {
	private static class TPONode {
		private final TPONode previous;
		private final DirectionalPathPoint pathPoint;
		private final Direction side;
		private final int depth;

		private TPONode(@Nullable TPONode previous, DirectionalPathPoint pathPoint) {
			this.previous = previous;
			this.depth = previous != null ? previous.depth + 1 : 0;
			this.pathPoint = pathPoint;
			this.side = pathPoint.getPathSide();
		}

		private TPONode(TPONode previous, int depth, DirectionalPathPoint pathPoint) {
			this.previous = previous;
			this.depth = depth;
			this.pathPoint = pathPoint;
			this.side = pathPoint.getPathSide();
		}

		@Override
		public int hashCode() {
			final var prime = 31;
			var result = 1;
			result = prime * result + ((this.pathPoint == null) ? 0 : this.pathPoint.hashCode());
			result = prime * result + ((this.side == null) ? 0 : this.side.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) 
				return true;
			if (obj == null) 
				return false;
			if (this.getClass() != obj.getClass()) 
				return false;
			var other = (TPONode) obj;
			if (this.pathPoint == null) 
				if (other.pathPoint != null) 
					return false;
			else if (!this.pathPoint.equals(other.pathPoint)) 
				return false;
			if (this.side != other.side) 
				return false;
			return true;
		}
	}

	private static final Direction[] DOWN = new Direction[] { Direction.DOWN };

	public AdvancedPathFinder(NodeEvaluator processor, int maxExpansions) {
		super(processor, maxExpansions);
	}

	@Override
	protected Path createPath(Node _targetPoint, BlockPos target, boolean isTargetReached) {
		List<Node> points = new ArrayList<>();
		// Backtrack path from target point back to entity
		this.backtrackPath(points, _targetPoint);
		// Retrace path with valid side transitions
		var end = this.retraceSidedPath(points, true);
		if (end == null) 
			return new Path(Collections.emptyList(), target, isTargetReached);
		points.clear();
		// Backtrack retraced path
		this.backtrackPath(points, end);

		return new Path(points, target, isTargetReached);
	}

	private void backtrackPath(List<Node> points, Node start) {
		var currentPathPoint = start;
		points.add(start);
		while (currentPathPoint.cameFrom != null) {
			currentPathPoint = currentPathPoint.cameFrom;
			points.add(currentPathPoint);
		}
	}

	private void backtrackPath(List<Node> points, TPONode start) {
		var currentTPONode = start;
		points.add(start.pathPoint);
		while (currentTPONode.previous != null) {
			currentTPONode = currentTPONode.previous;
			points.add(currentTPONode.pathPoint);
		}
	}

	private static Direction[] getPathableSidesWithFallback(DirectionalPathPoint point) {
		if (point.getPathableSides().length == 0)
			return DOWN;
		else
			return point.getPathableSides();
	}

	protected static boolean isOmnidirectionalPoint(DirectionalPathPoint point) {
		return point.type == BlockPathTypes.WATER || point.type == BlockPathTypes.LAVA;
	}

	private TPONode retraceSidedPath(List<Node> points, boolean isReversed) {
		if (points.isEmpty())
			return null;
		final Deque<TPONode> queue = new LinkedList<>();
		final var targetPoint = this.ensureDirectional(points.get(0));
		for (var direction : getPathableSidesWithFallback(targetPoint))
			queue.add(new TPONode(null, targetPoint.assignPathSide(direction)));
		TPONode end = null;
		final var maxExpansions = 200;
		final Set<TPONode> checkedSet = new HashSet<>();
		var expansions = 0;
		while (!queue.isEmpty()) {
			if (expansions++ > maxExpansions)
				break;
			var current = queue.removeFirst();
			if (current.depth == points.size() - 1) {
				end = current;
				break;
			}
			var currentSide = current.side;
			var next = this.ensureDirectional(points.get(current.depth + 1));

			for (var nextSide : getPathableSidesWithFallback(next)) {
				TPONode nextTPONode = null;
				if ((isReversed && current.pathPoint.isDrop()) || (!isReversed && next.isDrop()))
					// Side doesn't matter if node represents a drop
					nextTPONode = new TPONode(current, next.assignPathSide(nextSide));
				else {
					var dx = (int) Math.signum(next.x - current.pathPoint.x);
					var dy = (int) Math.signum(next.y - current.pathPoint.y);
					var dz = (int) Math.signum(next.z - current.pathPoint.z);
					var adx = Math.abs(dx);
					var ady = Math.abs(dy);
					var adz = Math.abs(dz);
					var d = adx + ady + adz;

					if (d == 1) {
						// Path is straight line
						if (nextSide == currentSide)
							// Allow movement on the same side
							nextTPONode = new TPONode(current, next.assignPathSide(nextSide));
						else if (nextSide.getAxis() != currentSide.getAxis()) {
							// Allow movement around corners, but insert new point with transitional side inbetween
							TPONode intermediary;
							if (Math.abs(currentSide.getStepX()) == adx && Math.abs(currentSide.getStepY()) == ady && Math.abs(currentSide.getStepZ()) == adz)
								intermediary = new TPONode(current, current.pathPoint.assignPathSide(nextSide));
							else
								intermediary = new TPONode(current, next.assignPathSide(currentSide));
							nextTPONode = new TPONode(intermediary, intermediary.depth, next.assignPathSide(nextSide));

						}
					} else if (d == 2) {
						// Diagonal
						var currentSidePlaneMatch = (currentSide.getStepX() == -dx ? 1 : 0) + (currentSide.getStepY() == -dy ? 1 : 0) + (currentSide.getStepZ() == -dz ? 1 : 0);
						if (currentSide == nextSide && currentSidePlaneMatch == 0)
							// Allow diagonal movement, no need to insert transitional side since the diagonal's plane's normal is the same as the path's side
							nextTPONode = new TPONode(current, next.assignPathSide(nextSide));
						else {
							// Allow movement, but insert new point with transitional side inbetween
							TPONode intermediary = null;
							if (currentSidePlaneMatch == 2) {
								for (var intermediarySide : getPathableSidesWithFallback(current.pathPoint))
									if (intermediarySide != currentSide && (intermediarySide.getStepX() == dx ? 1 : 0) + (intermediarySide.getStepY() == dy ? 1 : 0) + (intermediarySide.getStepZ() == dz ? 1 : 0) == 2) {
										intermediary = new TPONode(current, current.pathPoint.assignPathSide(intermediarySide));
										break;
									}
							} else
								for (var intermediarySide : getPathableSidesWithFallback(next))
									if (intermediarySide != nextSide && (intermediarySide.getStepX() == -dx ? 1 : 0) + (intermediarySide.getStepY() == -dy ? 1 : 0) + (intermediarySide.getStepZ() == -dz ? 1 : 0) == 2) {
										intermediary = new TPONode(current, next.assignPathSide(intermediarySide));
										break;
									}

							if (intermediary != null)
								nextTPONode = new TPONode(intermediary, intermediary.depth, next.assignPathSide(nextSide));
							else
								nextTPONode = new TPONode(current, next.assignPathSide(nextSide));
						}
					}
				}

				if (nextTPONode != null && checkedSet.add(nextTPONode))
					queue.addLast(nextTPONode);
			}
		}

		return end;
	}

	private DirectionalPathPoint ensureDirectional(Node point) {
		if (point instanceof DirectionalPathPoint)
			return (DirectionalPathPoint) point;
		else
			return new DirectionalPathPoint(point);
	}
}
