package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

/**
 * Credit to Boston/AVP
 */
public class WaterMoveControl extends MoveControl {

    private final AlienEntity xenomorph;

    public WaterMoveControl(AlienEntity xenomorph) {
        super(xenomorph);
        this.xenomorph = xenomorph;
    }

    @Override
    public void tick() {
        var livingEntity = xenomorph.getTarget();
        if (xenomorph.isUnderWater()) {
            if (livingEntity != null && livingEntity.getY() > xenomorph.getY()) {
                xenomorph.setDeltaMovement(xenomorph.getDeltaMovement().add(0.0, 0.002, 0.0));
            }

            if (operation != MoveControl.Operation.MOVE_TO || xenomorph.getNavigation().isDone()) {
                xenomorph.setSpeed(0.0F);
                return;
            }

            double d = wantedX - xenomorph.getX();
            double e = wantedY - xenomorph.getY();
            double f = wantedZ - xenomorph.getZ();
            double g = Math.sqrt(d * d + e * e + f * f);
            e /= g;
            float h = (float) (Mth.atan2(f, d) * 180.0F / (float) Math.PI) - 90.0F;
            xenomorph.setYRot(rotlerp(xenomorph.getYRot(), h, 90.0F));
            xenomorph.yBodyRot = xenomorph.getYRot();
            float i = (float) (speedModifier * xenomorph.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float j = Mth.lerp(0.125F, xenomorph.getSpeed(), i);
            xenomorph.setSpeed(j);
            xenomorph.setDeltaMovement(
                xenomorph.getDeltaMovement().add((double) j * d * 0.005, (double) j * e * 0.1, (double) j * f * 0.005)
            );
        } else {
            if (!xenomorph.onGround()) {
                xenomorph.setDeltaMovement(xenomorph.getDeltaMovement().add(0.0, -0.008, 0.0));
            }

            super.tick();
        }
    }
}
