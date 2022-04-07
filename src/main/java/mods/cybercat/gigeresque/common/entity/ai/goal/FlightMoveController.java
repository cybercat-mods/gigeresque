package mods.cybercat.gigeresque.common.entity.ai.goal;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FlightMoveController extends MoveControl {
	private final MobEntity parentEntity;
	private final float speedGeneral;
	private final boolean shouldLookAtTarget;
	private final boolean needsYSupport;

	public FlightMoveController(MobEntity bird, float speedGeneral, boolean shouldLookAtTarget, boolean needsYSupport) {
		super(bird);
		this.parentEntity = bird;
		this.shouldLookAtTarget = shouldLookAtTarget;
		this.speedGeneral = speedGeneral;
		this.needsYSupport = needsYSupport;
	}

	public FlightMoveController(MobEntity bird, float speedGeneral, boolean shouldLookAtTarget) {
		this(bird, speedGeneral, shouldLookAtTarget, false);
	}

	public FlightMoveController(MobEntity bird, float speedGeneral) {
		this(bird, speedGeneral, true);
	}

	public void tick() {
		if (this.state == MoveControl.State.MOVE_TO) {
			Vec3d vector3d = new Vec3d(this.targetX - parentEntity.getX(), this.targetY - parentEntity.getY(),
					this.targetZ - parentEntity.getZ());
			double d0 = vector3d.length();
			if (d0 < parentEntity.getBoundingBox().getAverageSideLength()) {
				this.state = MoveControl.State.WAIT;
				parentEntity.setVelocity(parentEntity.getVelocity().multiply(0.5D));
			} else {
				parentEntity.setVelocity(
						parentEntity.getVelocity().add(vector3d.multiply(this.speed * speedGeneral * 0.05D / d0)));
				if (needsYSupport) {
					double d1 = this.targetY - parentEntity.getY();
					parentEntity
							.setVelocity(parentEntity.getVelocity().add(0.0D, (double) parentEntity.getMovementSpeed()
									* speedGeneral * MathHelper.clamp(d1, -1, 1) * 0.6F, 0.0D));
				}
				if (parentEntity.getTarget() == null || !shouldLookAtTarget) {
					Vec3d vector3d1 = parentEntity.getVelocity();
					parentEntity.setYaw(-((float) Math.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI));
					parentEntity.bodyYaw = parentEntity.getYaw();
				} else {
					double d2 = parentEntity.getTarget().getX() - parentEntity.getX();
					double d1 = parentEntity.getTarget().getZ() - parentEntity.getZ();
					parentEntity.setYaw(-((float) Math.atan2(d2, d1)) * (180F / (float) Math.PI));
					parentEntity.bodyYaw = parentEntity.getYaw();
				}
			}

		} else if (this.state == State.STRAFE) {
			this.state = State.WAIT;
		}
	}
}