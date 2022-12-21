package mods.cybercat.gigeresque.common.entity.ai.goal;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class LongerAlienMeleeAttackGoal extends AlienMeleeAttackGoal {

	public LongerAlienMeleeAttackGoal(AlienEntity mob, double speed, boolean pauseWhenMobIdle) {
		super(mob, speed, pauseWhenMobIdle);
	}

	@Override
	public void tick() {
		LivingEntity livingentity = this.mob.getTarget();
		if (livingentity != null) {
			boolean inLineOfSight = this.mob.getSensing().hasLineOfSight(livingentity);
			this.cooldown++;
			this.mob.lookAt(livingentity, 30.0F, 30.0F);
			final AABB aabb2 = new AABB(this.mob.blockPosition()).inflate(2.5D);
			if (inLineOfSight) {
				this.mob.getNavigation().moveTo(livingentity, this.speed * 1.25);
				this.mob.getCommandSenderWorld().getEntities(this.mob, aabb2).forEach(e -> {
					int state = this.mob.getRandom().nextInt(0, 3);
					if (this.cooldown == 1) {
						this.mob.setAttackingState(state);
					}
					if (this.cooldown == 8) {
						if ((e instanceof LivingEntity) && this.mob.getAttckingState() >= 1) {
							this.mob.doHurtTarget(livingentity);
						}
					}
					if (this.cooldown >= 20) {
						this.cooldown = -15;
						this.mob.setAttackingState(0);
					}
				});
			} 
		}
	}

}
