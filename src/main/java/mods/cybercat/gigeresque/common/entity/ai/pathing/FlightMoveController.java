package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class FlightMoveController extends MoveControl {
    private final Mob parentEntity;

    public FlightMoveController(Mob bird) {
        super(bird);
        this.parentEntity = bird;
    }

    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO)
            parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().scale(0.5D));
        else if (this.operation == Operation.STRAFE)
            this.operation = Operation.WAIT;
    }
}