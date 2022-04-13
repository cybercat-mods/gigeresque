package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;

public class FlightMoveController extends MoveControl {
	private final MobEntity parentEntity;

	public FlightMoveController(MobEntity bird) {
		super(bird);
		this.parentEntity = bird;
	}

	public void tick() {
		if (this.state == MoveControl.State.MOVE_TO) {
			parentEntity.setVelocity(parentEntity.getVelocity().multiply(0.5D));
		} else if (this.state == State.STRAFE) {
			this.state = State.WAIT;
		}
	}
}