package mods.cybercat.gigeresque.common.entity.ai.goal;

import java.util.EnumSet;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
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
import net.minecraft.world.phys.AABB;

public class AlienMeleeAttackGoal extends Goal {
	protected final AlienEntity mob;
	protected final double speed;
	protected final boolean pauseWhenMobIdle;
	protected Path path;
	protected int cooldown;
	protected long lastUpdateTime;

	public AlienMeleeAttackGoal(AlienEntity mob, double speed, boolean pauseWhenMobIdle) {
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
		if (livingEntity.getVehicle() != null
				&& livingEntity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) {
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
		if (livingEntity.getVehicle() != null
				&& livingEntity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) {
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
		this.cooldown = 0;
		this.mob.setAttackingState(0);
	}

	@Override
	public void stop() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.setTarget(null);
		}
		this.mob.setAggressive(false);
		this.mob.getNavigation().stop();
		this.mob.setAttackingState(0);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity != null) {
			boolean inLineOfSight = this.mob.getSensing().hasLineOfSight(livingEntity);
			this.mob.lookAt(livingEntity, 30.0F, 30.0F);
			final AABB aabb2 = new AABB(this.mob.blockPosition()).inflate(1.5D);
			if (inLineOfSight) {
				cooldown++;
				this.mob.getNavigation().moveTo(livingEntity, this.speed);
				this.mob.getCommandSenderWorld().getEntities(this.mob, aabb2).forEach(e -> {
					if ((e instanceof LivingEntity) && this.cooldown > 0) {
						this.attack(livingEntity);
						this.cooldown = -20;
					} else {
						this.mob.setAttackingState(0);
						this.cooldown = -20;
					}
				});
			} else {
				this.cooldown = -20;
				this.mob.setAttackingState(0);
			}
		}
	}

	protected void attack(LivingEntity target) {
		this.mob.doHurtTarget(target);
		this.mob.swing(InteractionHand.MAIN_HAND);
	}

	protected double getSquaredMaxAttackDistance(LivingEntity entity) {
		return this.mob.getBbWidth() * 4.0f * (this.mob.getBbWidth() * 4.0f) + entity.getBbWidth();
	}
}