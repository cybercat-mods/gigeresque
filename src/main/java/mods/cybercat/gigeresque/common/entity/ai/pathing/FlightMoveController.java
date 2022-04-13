package mods.cybercat.gigeresque.common.entity.ai.pathing;

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
			parentEntity.setVelocity(parentEntity.getVelocity().multiply(0.5D));
		} else if (this.state == State.STRAFE) {
			this.state = State.WAIT;
		}
	}
}