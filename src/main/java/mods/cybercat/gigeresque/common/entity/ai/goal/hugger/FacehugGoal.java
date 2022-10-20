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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FacehugGoal extends Goal {
	protected final FacehuggerEntity mob;
	protected final double speed;
	private int attackTime = -1;

	public FacehugGoal(FacehuggerEntity mob, double speed) {
		this.mob = mob;
		this.speed = speed;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (((Eggmorphable) livingEntity).isEggmorphing()) {
			return false;
		}
		if (livingEntity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (!this.mob.getSensing().hasLineOfSight(livingEntity)) {
			return false;
		}
		if (livingEntity instanceof ArmorStand) {
			return false;
		}
		if (ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, livingEntity)) {
			return false;
		}
		if (livingEntity.getVehicle() != null
				&& livingEntity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) {
			return false;
		}
		if (livingEntity.getMobType() == MobType.UNDEAD) {
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
	public boolean canContinueToUse() {
		return (this.canUse() || !this.mob.getNavigation().isDone());
	}

	@Override
	public void start() {
		super.start();
		this.mob.setAggressive(true);
	}

	@Override
	public void stop() {
		super.stop();
		LivingEntity livingEntity = this.mob.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.setTarget(null);
		}
		this.mob.setIsAttakcing(false);
		this.mob.setAggressive(false);
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
		int k = Mth.floor(mob.getX() - (double) q - 1.0D);
		int l = Mth.floor(mob.getX() + (double) q + 1.0D);
		int t = Mth.floor(mob.getY() - (double) q - 1.0D);
		int u = Mth.floor(mob.getY() + (double) q + 1.0D);
		int v = Mth.floor(mob.getZ() - (double) q - 1.0D);
		int w = Mth.floor(mob.getZ() + (double) q + 1.0D);
		List<Entity> list = mob.level.getEntities(mob,
				new AABB((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
		Vec3 vec3d1 = new Vec3(mob.getX(), mob.getY(), mob.getZ());
		this.mob.getNavigation().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.15);
		this.mob.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f);
		mob.setIsAttakcing(true);
		for (int x = 0; x < list.size(); ++x) {
			double y = (double) (Mth.sqrt((float) livingEntity.distanceToSqr(vec3d1)) / q);
			if (!this.mob.isUnderWater()) {
				if (y <= 2) {
					this.mob.getNavigation().stop();
					this.mob.getNavigation().setSpeedModifier(0);
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
						Vec3 vec3d = this.mob.getDeltaMovement();
						Vec3 vec3d2 = new Vec3(livingEntity.getX() - this.mob.getX(), 0.0,
								livingEntity.getZ() - this.mob.getZ());
						if (vec3d2.lengthSqr() > 1.0E-7) {
							vec3d2 = vec3d2.normalize().scale(0.9).add(vec3d.scale(0.9));
						}
						this.mob.setDeltaMovement(vec3d2.x, 0.6, vec3d2.z);
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
					this.mob.getNavigation().setSpeedModifier(this.speed);
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
		return this.mob.getBbWidth() * 2.0f * (this.mob.getBbWidth() * 2.0f) + entity.getBbWidth();
	}
}