package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;

public class AlienMeleeAttackGoal extends Goal {
	protected final AdultAlienEntity mob;
	private final double speed;
	private final boolean pauseWhenMobIdle;
	private Path path;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int updateCountdownTicks;
	private int cooldown;
	private long lastUpdateTime;
	private int meleeCounter = 0;

	public AlienMeleeAttackGoal(AdultAlienEntity mob, double speed, boolean pauseWhenMobIdle) {
		this.mob = mob;
		this.speed = speed;
		this.pauseWhenMobIdle = pauseWhenMobIdle;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}

	@Override
	public boolean canStart() {
		long l = this.mob.world.getTime();
		if (l - this.lastUpdateTime < 20L) {
			return false;
		}
		this.lastUpdateTime = l;
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (((Host) livingEntity).hasParasite()) {
			return false;
		}
		if (!livingEntity.isAlive()) {
			return false;
		}
		this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
		if (this.path != null) {
			return true;
		}
		if (livingEntity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (mob.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (!this.mob.getVisibilityCache().canSee(livingEntity)) {
			return false;
		}
		if (livingEntity.getGroup() == EntityGroup.UNDEAD) {
			return false;
		}
		if (livingEntity instanceof AlienEntity) {
			return false;
		}
		if (this.mob.hasPassengers())
			return false;
		if (((Host) livingEntity).isBleeding()) {
			return false;
		}
		if (EntityUtils.isFacehuggerAttached(livingEntity)) {
			return false;
		}
		return this.getSquaredMaxAttackDistance(livingEntity) >= this.mob.squaredDistanceTo(livingEntity.getX(),
				livingEntity.getY(), livingEntity.getZ());
	}

	@Override
	public boolean shouldContinue() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (((Host) livingEntity).hasParasite()) {
			return false;
		}
		if (!livingEntity.isAlive()) {
			return false;
		}
		if (this.mob.hasPassengers())
			return false;
		if (!this.pauseWhenMobIdle) {
			return !this.mob.getNavigation().isIdle();
		}
		if (!this.mob.isInWalkTargetRange(livingEntity.getBlockPos())) {
			return false;
		}
		if (livingEntity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (mob.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (!this.mob.getVisibilityCache().canSee(livingEntity)) {
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
		return !(livingEntity instanceof PlayerEntity)
				|| !livingEntity.isSpectator() && !((PlayerEntity) livingEntity).isCreative();
	}

	@Override
	public void start() {
		this.mob.getNavigation().startMovingAlong(this.path, this.speed);
		this.mob.setAttacking(true);
		this.updateCountdownTicks = 0;
		this.cooldown = 0;
		mob.setIsExecuting(false);
	}

	@Override
	public void stop() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.setTarget(null);
		}
		this.mob.setAttacking(false);
		this.mob.getNavigation().stop();
		mob.setIsExecuting(false);
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return;
		}
		this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
		double d = this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
		this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
		if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingEntity))
				&& this.updateCountdownTicks <= 0
				&& (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
						|| livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0
						|| this.mob.getRandom().nextFloat() < 0.05f)) {
			this.targetX = livingEntity.getX();
			this.targetY = livingEntity.getY();
			this.targetZ = livingEntity.getZ();
			this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
			if (d > 1024.0) {
				this.updateCountdownTicks += 10;
			} else if (d > 256.0) {
				this.updateCountdownTicks += 5;
			}
			if (!this.mob.getNavigation().startMovingTo(livingEntity, this.speed)) {
				this.updateCountdownTicks += 15;
			}
			this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
		}
		this.cooldown = Math.max(this.cooldown - 1, 0);
		if (!this.mob.hasPassengers()) {
			if (meleeCounter == 1) {
				this.attack(livingEntity, d);
			}
			if (meleeCounter >= 10) {
				meleeCounter = -15;
			}
		}
		this.attack(livingEntity, d);
	}

	protected void attack(LivingEntity target, double squaredDistance) {
		double d = this.getSquaredMaxAttackDistance(target);
		if (squaredDistance <= d) {
			this.mob.tryAttack(target);
			this.mob.swingHand(Hand.MAIN_HAND);
			meleeCounter = -5;
		}
	}

	protected void resetCooldown() {
		this.cooldown = this.getTickCount(20);
	}

	protected boolean isCooledDown() {
		return this.cooldown <= 0;
	}

	protected int getCooldown() {
		return this.cooldown;
	}

	protected int getMaxCooldown() {
		return this.getTickCount(20);
	}

	protected double getSquaredMaxAttackDistance(LivingEntity entity) {
		return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
	}
}