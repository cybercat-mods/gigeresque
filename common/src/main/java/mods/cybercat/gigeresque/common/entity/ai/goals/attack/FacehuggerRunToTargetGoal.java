package mods.cybercat.gigeresque.common.entity.ai.goals.attack;

import net.minecraft.world.effect.MobEffects;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class FacehuggerRunToTargetGoal extends DelayedAttackGoal {

    public FacehuggerRunToTargetGoal(AlienEntity mob, double speedModifier, int delayTicksBeforeAttack) {
        super(mob, speedModifier, delayTicksBeforeAttack);
    }

    @Override
    protected boolean isAbleToAttack() {
        if (alienEntity.hasEffect(MobEffects.CONFUSION)) {
            return false;
        }
        if (alienEntity.isPassenger() || alienEntity.getTarget() == null) {
            return false;
        }
        if (
            alienEntity.getTarget().getType().is(GigTags.FACEHUGGER_BLACKLIST) || !GigEntityUtils.isTargetHostable(alienEntity.getTarget())
        ) {
            return false;
        }

        return !NEST.test(alienEntity.getTarget().getInBlockState());
    }

    @Override
    public void tick() {
        super.tick();
        attackAnimationCooldown.tick();

        if (
            // If target is not null
            alienEntity.getTarget() != null
                // AND we ran the attack animation.
                && ranAttackAnimation
                // AND the animation cooldown has finished
                && !attackAnimationCooldown.isActive()
                // AND we still have a line of sight of the target
                && alienEntity.getSensing().hasLineOfSight(alienEntity.getTarget())
        ) {
            resetAttackCooldown();

            this.ranAttackAnimation = false;
        }
    }
}
