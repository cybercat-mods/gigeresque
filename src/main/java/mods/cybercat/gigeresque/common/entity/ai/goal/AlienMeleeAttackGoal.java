package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

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
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		long l = this.mob.level.getGameTime();
		if (l - this.lastUpdateTime < 20L) {
			return false;
		}
		this.lastUpdateTime = l;
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (livingEntity.getVehicle() != null && livingEntity.getVehicle().getSelfAndPassengers()
				.anyMatch(AlienEntity.class::isInstance)) {
			return false;
		}
		if (((Host) livingEntity).hasParasite()) {
			return false;
		}
		if (!livingEntity.isAlive()) {
			return false;
		}
		if (livingEntity instanceof ArmorStand) {
			return false;
		}
		this.path = this.mob.getNavigation().createPath(livingEntity, 0);
		if (this.path != null) {
			return true;
		}
		if (livingEntity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (mob.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (!this.mob.getSensing().hasLineOfSight(livingEntity)) {
			return false;
		}
		if (livingEntity.getMobType() == MobType.UNDEAD) {
			return false;
		}
		if (livingEntity instanceof AlienEntity) {
			return false;
		}
		if (this.mob.isVehicle())
			return false;
		if (((Host) livingEntity).isBleeding()) {
			return false;
		}
		if (EntityUtils.isFacehuggerAttached(livingEntity)) {
			return false;
		}
		return this.getSquaredMaxAttackDistance(livingEntity) >= this.mob.distanceToSqr(livingEntity.getX(),
				livingEntity.getY(), livingEntity.getZ());
	}

	@Override
	public boolean canContinueToUse() {
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
		if (this.mob.isVehicle())
			return false;
		if (!this.pauseWhenMobIdle) {
			return !this.mob.getNavigation().isDone();
		}
		if (livingEntity instanceof ArmorStand) {
			return false;
		}
		if (livingEntity.getVehicle() != null && livingEntity.getVehicle().getSelfAndPassengers()
				.anyMatch(AlienEntity.class::isInstance)) {
			return false;
		}
		if (!this.mob.isWithinRestriction(livingEntity.blockPosition())) {
			return false;
		}
		if (livingEntity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (mob.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) {
			return false;
		}
		if (!this.mob.getSensing().hasLineOfSight(livingEntity)) {
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
		return !(livingEntity instanceof Player)
				|| !livingEntity.isSpectator() && !((Player) livingEntity).isCreative();
	}

	@Override
	public void start() {
		this.mob.getNavigation().moveTo(this.path, this.speed);
		this.mob.setAggressive(true);
		this.updateCountdownTicks = 0;
		this.cooldown = 0;
		mob.setIsExecuting(false);
	}

	@Override
	public void stop() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.setTarget(null);
		}
		this.mob.setAggressive(false);
		this.mob.getNavigation().stop();
		mob.setIsExecuting(false);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return;
		}
		this.mob.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f);
		double d = this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
		this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
		if ((this.pauseWhenMobIdle || this.mob.getSensing().hasLineOfSight(livingEntity))
				&& this.updateCountdownTicks <= 0
				&& (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
						|| livingEntity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0
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
			if (!this.mob.getNavigation().moveTo(livingEntity, this.speed)) {
				this.updateCountdownTicks += 15;
			}
			this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
		}
		this.cooldown = Math.max(this.cooldown - 1, 0);
		if (!this.mob.isVehicle()) {
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
			this.mob.doHurtTarget(target);
			this.mob.swing(InteractionHand.MAIN_HAND);
			meleeCounter = -5;
		}
	}

	protected void resetCooldown() {
		this.cooldown = this.adjustedTickDelay(20);
	}

	protected boolean isCooledDown() {
		return this.cooldown <= 0;
	}

	protected int getCooldown() {
		return this.cooldown;
	}

	protected int getMaxCooldown() {
		return this.adjustedTickDelay(20);
	}

	protected double getSquaredMaxAttackDistance(LivingEntity entity) {
		return this.mob.getBbWidth() * 2.0f * (this.mob.getBbWidth() * 2.0f) + entity.getBbWidth();
	}
}