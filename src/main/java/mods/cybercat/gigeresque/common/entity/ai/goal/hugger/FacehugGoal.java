package mods.cybercat.gigeresque.common.entity.ai.goal.hugger;

import java.util.EnumSet;
import java.util.List;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FacehugGoal extends Goal {
	protected final FacehuggerEntity mob;
	protected final double speed;
	private int attackTime = -1;

	public FacehugGoal(FacehuggerEntity mob, double speed) {
		this.mob = mob;
		this.speed = speed;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}

	@Override
	public boolean canStart() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (((Eggmorphable) livingEntity).isEggmorphing()) {
			return false;
		}
		if (livingEntity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (!this.mob.getVisibilityCache().canSee(livingEntity)) {
			return false;
		}
		if (livingEntity instanceof ArmorStandEntity) {
			return false;
		}
		if (ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, livingEntity)) {
			return false;
		}
		if (livingEntity.getVehicle() != null
				&& livingEntity.getVehicle().streamSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) {
			return false;
		}
		if (livingEntity.getGroup() == EntityGroup.UNDEAD) {
			return false;
		}
		if (livingEntity instanceof AlienEntity) {
			return false;
		}
		if (((Host) livingEntity).isBleeding()) {
			return false;
		}
		if (EntityUtils.isFacehuggerAttached(livingEntity)) {
			return false;
		}
		if (((Host) livingEntity).hasParasite()) {
			return false;
		}
		if (!livingEntity.isAlive()) {
			return false;
		}
		return this.mob.getTarget() != null;
	}

	@Override
	public boolean shouldContinue() {
		return (this.canStart() || !this.mob.getNavigation().isIdle());
	}

	@Override
	public void start() {
		super.start();
		this.mob.setAttacking(true);
	}

	@Override
	public void stop() {
		super.stop();
		LivingEntity livingEntity = this.mob.getTarget();
		if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.setTarget(null);
		}
		this.mob.setIsAttakcing(false);
		this.mob.setAttacking(false);
		mob.setIsJumping(false);
		mob.setUpsideDown(false);
		mob.setIsAttakcing(false);
		this.attackTime = -1;
	}

	@Override
	public void tick() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return;
		}
		float q = 2.0F;
		int k = MathHelper.floor(mob.getX() - (double) q - 1.0D);
		int l = MathHelper.floor(mob.getX() + (double) q + 1.0D);
		int t = MathHelper.floor(mob.getY() - (double) q - 1.0D);
		int u = MathHelper.floor(mob.getY() + (double) q + 1.0D);
		int v = MathHelper.floor(mob.getZ() - (double) q - 1.0D);
		int w = MathHelper.floor(mob.getZ() + (double) q + 1.0D);
		List<Entity> list = mob.world.getOtherEntities(mob,
				new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
		Vec3d vec3d1 = new Vec3d(mob.getX(), mob.getY(), mob.getZ());
		this.mob.getNavigation().startMovingTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.15);
		this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
		mob.setIsAttakcing(true);
		for (int x = 0; x < list.size(); ++x) {
			double y = (double) (MathHelper.sqrt((float) livingEntity.squaredDistanceTo(vec3d1)) / q);
			if (!this.mob.isSubmergedInWater()) {
				if (y <= 2) {
					this.mob.getNavigation().stop();
					this.mob.getNavigation().setSpeed(0);
					this.attackTime++;
					if (this.attackTime == 2) {
						mob.setIsJumping(true);
						mob.setIsAttakcing(false);
					}
					if (this.attackTime == 22) {
						mob.setUpsideDown(true);
						mob.setIsJumping(false);
					}
					if (this.attackTime == 23) {
						Vec3d vec3d = this.mob.getVelocity();
						Vec3d vec3d2 = new Vec3d(livingEntity.getX() - this.mob.getX(), 0.0,
								livingEntity.getZ() - this.mob.getZ());
						if (vec3d2.lengthSquared() > 1.0E-7) {
							vec3d2 = vec3d2.normalize().multiply(0.9).add(vec3d.multiply(0.9));
						}
						this.mob.setVelocity(vec3d2.x, 0.6, vec3d2.z);
					}
					if (this.attackTime == 25) {
						if (y <= 2) {
							mob.attachToHost(livingEntity);
						}
					}
					if (this.attackTime == 40) {
						this.attackTime = -50;
						mob.setUpsideDown(false);
						mob.setIsJumping(false);
						mob.setIsAttakcing(false);
					}
				} else {
					this.attackTime = -1;
					mob.setUpsideDown(false);
					mob.setIsJumping(false);
					this.mob.getNavigation().setSpeed(this.speed);
				}
			} else {
				this.attackTime++;
				if (this.attackTime == 2) {
					mob.attachToHost(livingEntity);
				}
				if (this.attackTime == 5) {
					this.attackTime = -50;
				}
			}
		}
	}

	protected double getAttackReachSqr(LivingEntity entity) {
		return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
	}
}