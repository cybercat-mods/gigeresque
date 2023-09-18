package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.Node;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class DirectionalPathPoint extends Node {
	protected static final long ALL_DIRECTIONS = AdvancedWalkNodeProcessor.packDirection(Direction.UP, AdvancedWalkNodeProcessor.packDirection(Direction.DOWN, AdvancedWalkNodeProcessor.packDirection(Direction.NORTH, AdvancedWalkNodeProcessor.packDirection(Direction.EAST, AdvancedWalkNodeProcessor.packDirection(Direction.SOUTH, AdvancedWalkNodeProcessor.packDirection(Direction.WEST, 0L))))));

	protected static final Direction[] DIRECTIONS = Direction.values();

	private final Direction[] pathableSides;
	private final Direction pathSide;

	private final boolean isDrop;
	
	public DirectionalPathPoint(int x, int y, int z, long packed, boolean isDrop) {
		super(x, y, z);

		EnumSet<Direction> directionsSet = EnumSet.noneOf(Direction.class);
		for(int i = 0; i < DIRECTIONS.length; i++) {
			Direction dir = DIRECTIONS[i];

			if(AdvancedWalkNodeProcessor.unpackDirection(dir, packed)) {
				directionsSet.add(dir);
			}
		}

		this.pathableSides = directionsSet.toArray(new Direction[0]);
		this.pathSide = null;
		
		this.isDrop = isDrop;
	}

	public DirectionalPathPoint(Node point, long packed, boolean isDrop) {
		this(point.x, point.y, point.z, packed, isDrop);

		this.heapIdx = point.heapIdx;
		this.g = point.g;
		this.h = point.h;
		this.f = point.f;
		this.cameFrom = point.cameFrom;
		this.closed = point.closed;
		this.walkedDistance = point.walkedDistance;
		this.costMalus = point.costMalus;
		this.type = point.type;
	}

	public DirectionalPathPoint(Node point) {
		this(point, ALL_DIRECTIONS, false);
	}

	private DirectionalPathPoint(int x, int y, int z, Direction[] pathableSides, Direction pathSide, boolean isDrop) {
		super(x, y, z);

		this.pathableSides = new Direction[pathableSides.length];
		System.arraycopy(pathableSides, 0, this.pathableSides, 0, pathableSides.length);

		this.pathSide = pathSide;
		
		this.isDrop = isDrop;
	}

	public DirectionalPathPoint(Node point, Direction pathSide) {
		super(point.x, point.y, point.z);

		this.heapIdx = point.heapIdx;
		this.g = point.g;
		this.h = point.h;
		this.f = point.f;
		this.cameFrom = point.cameFrom;
		this.closed = point.closed;
		this.walkedDistance = point.walkedDistance;
		this.costMalus = point.costMalus;
		this.type = point.type;

		if(point instanceof DirectionalPathPoint) {
			DirectionalPathPoint other = (DirectionalPathPoint) point;

			this.pathableSides = new Direction[other.pathableSides.length];
			System.arraycopy(other.pathableSides, 0, this.pathableSides, 0, other.pathableSides.length);
			
			this.isDrop = other.isDrop;
		} else {
			this.pathableSides = Direction.values();
		
			this.isDrop = false;
		}

		this.pathSide = pathSide;
	}
	
	public DirectionalPathPoint assignPathSide(Direction pathDirection) {
		return new DirectionalPathPoint(this, pathDirection);
	}

	@Override
	public Node cloneAndMove(int x, int y, int z) {
		Node pathPoint = new DirectionalPathPoint(x, y, z, this.pathableSides, this.pathSide, this.isDrop);
		pathPoint.heapIdx = this.heapIdx;
		pathPoint.g = this.g;
		pathPoint.h = this.h;
		pathPoint.f = this.f;
		pathPoint.cameFrom = this.cameFrom;
		pathPoint.closed = this.closed;
		pathPoint.walkedDistance = this.walkedDistance;
		pathPoint.costMalus = this.costMalus;
		pathPoint.type = this.type;
		return pathPoint;
	}

	/**
	 * Returns all pathable sides of this node, i.e. all sides the entity could potentially walk on
	 * @return
	 */
	public Direction[] getPathableSides() {
		return this.pathableSides;
	}

	/**
	 * Returns the side assigned to this node by the path this node is part of, or null if this node has not been assigned to a path
	 * @return
	 */
	@Nullable
	public Direction getPathSide() {
		return this.pathSide;
	}
	
	/**
	 * Returns whether this node represents a drop
	 * @return
	 */
	public boolean isDrop() {
		return this.isDrop;
	}
}
