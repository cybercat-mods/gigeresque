package mods.cybercat.gigeresque.common.entity.helper;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.interfacing.IClimberEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ClimberMoveController<T extends Mob & IClimberEntity> extends MoveControl {
	protected final IClimberEntity climber;

	@Nullable
	protected BlockPos block;

	@Nullable
	protected Direction side;

	public ClimberMoveController(T entity) {
		super(entity);
		this.climber = entity;
	}

	@Override
	public void setWantedPosition(double x, double y, double z, double speedIn) {
		this.setMoveTo(x, y, z, null, null, speedIn);
	}

	public void setMoveTo(double x, double y, double z, BlockPos block, Direction side, double speedIn) {
		super.setWantedPosition(x, y, z, speedIn);
		this.block = block;
		this.side = side;
	}

	@Override
	public void tick() {
		var speed = this.climber.getMovementSpeed() * this.speedModifier;

		if (this.operation == Operation.STRAFE) {
			this.operation = Operation.WAIT;

			var forward = this.strafeForwards;
			var strafe = this.strafeRight;
			var moveSpeed = Mth.sqrt(forward * forward + strafe * strafe);
			if (moveSpeed < 1.0F)
				moveSpeed = 1.0F;
			moveSpeed = (float) speed / moveSpeed;
			forward = forward * moveSpeed;
			strafe = strafe * moveSpeed;
			var orientation = this.climber.getOrientation();
			var forwardVector = orientation.getGlobal(this.mob.yRot, 0);
			var strafeVector = orientation.getGlobal(this.mob.yRot + 90.0f, 0);

			if (!this.isWalkableAtOffset(forwardVector.x * forward + strafeVector.x * strafe, forwardVector.y * forward + strafeVector.y * strafe, forwardVector.z * forward + strafeVector.z * strafe)) {
				this.strafeForwards = 1.0F;
				this.strafeRight = 0.0F;
			}

			this.mob.setSpeed((float) speed);
			this.mob.setZza(this.strafeForwards);
			this.mob.setXxa(this.strafeRight);
		} else if (this.operation == Operation.MOVE_TO) {
			this.operation = Operation.WAIT;
			var dx = this.wantedX - this.mob.getX();
			var dy = this.wantedY - this.mob.getY();
			var dz = this.wantedZ - this.mob.getZ();
			if (this.side != null && this.block != null) {
				var shape = this.mob.level().getBlockState(this.block).getCollisionShape(this.mob.level(), this.block);
				var aabb = this.mob.getBoundingBox();
				var ox = 0;
				var oy = 0;
				var oz = 0;

				// Use offset towards pathing side if mob is above that pathing side
				switch (this.side) {
				case DOWN:
					if (aabb.minY >= this.block.getY() + shape.max(Direction.Axis.Y) - 0.01D)
						ox -= 0.1D;
					break;
				case UP:
					if (aabb.maxY <= this.block.getY() + shape.min(Direction.Axis.Y) + 0.01D)
						oy += 0.1D;
					break;
				case WEST:
					if (aabb.minX >= this.block.getX() + shape.max(Direction.Axis.X) - 0.01D)
						ox -= 0.1D;
					break;
				case EAST:
					if (aabb.maxX <= this.block.getX() + shape.min(Direction.Axis.X) + 0.01D)
						ox += 0.1D;
					break;
				case NORTH:
					if (aabb.minZ >= this.block.getZ() + shape.max(Direction.Axis.Z) - 0.01D)
						oz -= 0.1D;
					break;
				case SOUTH:
					if (aabb.maxZ <= this.block.getZ() + shape.min(Direction.Axis.Z) + 0.01D)
						oz += 0.1D;
					break;
				}

				var blockAabb = new AABB(this.block.relative(this.side.getOpposite()));

				// If mob is on the pathing side block then only apply the offsets if the block is above the according side of the voxel shape
				if (aabb.intersects(blockAabb)) {
					var.Axis offsetAxis = this.side.getAxis();
					double offset;

					switch (offsetAxis) {
					default:
					case X:
						offset = this.side.getStepX() * 0.5f;
						break;
					case Y:
						offset = this.side.getStepY() * 0.5f;
						break;
					case Z:
						offset = this.side.getStepZ() * 0.5f;
						break;
					}

					var allowedOffset = shape.collide(offsetAxis, aabb.move(-this.block.getX(), -this.block.getY(), -this.block.getZ()), offset);

					switch (this.side) {
					case DOWN:
						if (aabb.minY + allowedOffset < this.block.getY() + shape.max(Direction.Axis.Y) - 0.01D)
							oy = 0;
						break;
					case UP:
						if (aabb.maxY + allowedOffset > this.block.getY() + shape.min(Direction.Axis.Y) + 0.01D)
							oy = 0;
						break;
					case WEST:
						if (aabb.minX + allowedOffset < this.block.getX() + shape.max(Direction.Axis.X) - 0.01D)
							ox = 0;
						break;
					case EAST:
						if (aabb.maxX + allowedOffset > this.block.getX() + shape.min(Direction.Axis.X) + 0.01D)
							ox = 0;
						break;
					case NORTH:
						if (aabb.minZ + allowedOffset < this.block.getZ() + shape.max(Direction.Axis.Z) - 0.01D)
							oz = 0;
						break;
					case SOUTH:
						if (aabb.maxZ + allowedOffset > this.block.getZ() + shape.min(Direction.Axis.Z) + 0.01D)
							oz = 0;
						break;
					}
				}

				dx += ox;
				dy += oy;
				dz += oz;
			}

			var mainOffsetDir = Direction.getNearest(dx, dy, dz);

			float reach;
			switch (mainOffsetDir) {
			case DOWN:
				reach = 0;
				break;
			case UP:
				reach = this.mob.getBbHeight();
				break;
			default:
				reach = this.mob.getBbWidth() * 0.5f;
				break;
			}

			var verticalOffset = Math.abs(mainOffsetDir.getStepX() * dx) + Math.abs(mainOffsetDir.getStepY() * dy) + Math.abs(mainOffsetDir.getStepZ() * dz);
			var groundDir = this.climber.getGroundDirection().getLeft();
			Vec3 jumpDir = null;

			if (this.side != null && verticalOffset > reach - 0.05f && groundDir != this.side && groundDir.getAxis() != this.side.getAxis()) {
				var hdx = (1 - Math.abs(mainOffsetDir.getStepX())) * dx;
				var hdy = (1 - Math.abs(mainOffsetDir.getStepY())) * dy;
				var hdz = (1 - Math.abs(mainOffsetDir.getStepZ())) * dz;
				var hdsq = hdx * hdx + hdy * hdy + hdz * hdz;
				if (hdsq < 0.707f) {
					dx -= this.side.getStepX() * 0.2f;
					dy -= this.side.getStepY() * 0.2f;
					dz -= this.side.getStepZ() * 0.2f;
					if (hdsq < 0.1f)
						jumpDir = new Vec3(mainOffsetDir.getStepX(), mainOffsetDir.getStepY(), mainOffsetDir.getStepZ());
				}
			}

			var orientation = this.climber.getOrientation();
			var up = orientation.getGlobal(this.mob.yRot, -90);
			var offset = new Vec3(dx, dy, dz);
			var targetDir = offset.subtract(up.scale(offset.dot(up)));
			var targetDist = targetDir.length();
			targetDir = targetDir.normalize();

			if (targetDist < 0.0001D)
				this.mob.setZza(0);
			else {
				var rx = (float) orientation.localZ().dot(targetDir);
				var ry = (float) orientation.localX().dot(targetDir);
				this.mob.yRot = this.rotlerp(this.mob.yRot, 270.0f - (float) Math.toDegrees(Mth.atan2(rx, ry)), 90.0f);
				if (jumpDir == null && this.side != null && targetDist < 0.1D && groundDir == this.side.getOpposite())
					jumpDir = new Vec3(this.side.getStepX(), this.side.getStepY(), this.side.getStepZ());
				if (jumpDir != null) {
					this.mob.setSpeed((float) speed * 0.5f);
					var jumpController = this.mob.getJumpControl();
					if (jumpController instanceof ClimberJumpController climbercontroller)
						climbercontroller.setJumping(jumpDir);
				} else
					this.mob.setSpeed((float) speed);
			}
		} else if (this.operation == Operation.JUMPING) {
			this.mob.setSpeed((float) speed);
			if (this.mob.onGround())
				this.operation = Operation.WAIT;
		} else
			this.mob.setZza(0);
	}

	private boolean isWalkableAtOffset(double x, double y, double z) {
		var navigator = this.mob.getNavigation();
		if (navigator != null) {
			var processor = navigator.getNodeEvaluator();
			if (processor != null && processor.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + x), Mth.floor(this.mob.getY() + this.mob.getBbHeight() * 0.5f + y), Mth.floor(this.mob.getZ() + z)) != BlockPathTypes.WALKABLE)
				return false;
		}
		return true;
	}
}