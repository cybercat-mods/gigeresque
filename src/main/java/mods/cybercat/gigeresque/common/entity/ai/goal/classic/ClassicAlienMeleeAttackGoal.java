package mods.cybercat.gigeresque.common.entity.ai.goal.classic;

import java.util.EnumSet;
import java.util.SplittableRandom;
import java.util.function.Predicate;
import java.util.stream.Stream;

import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;

public class ClassicAlienMeleeAttackGoal extends Goal {
	protected final ClassicAlienEntity mob;
	private final double speed;
	private final boolean pauseWhenMobIdle;
	private Path path;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int updateCountdownTicks;
	private int cooldown;
	private long lastUpdateTime;
	public static final Predicate<BlockState> NEST = state -> state.isOf(GIgBlocks.NEST_RESIN_WEB_CROSS);
	private int holdingCounter = 0;
	private int meleeCounter = 0;

	public ClassicAlienMeleeAttackGoal(ClassicAlienEntity mob, double speed, boolean pauseWhenMobIdle) {
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
		Stream<BlockState> list2 = livingEntity.world
				.getStatesInBoxIfLoaded(livingEntity.getBoundingBox().expand(2.0, 2.0, 2.0));
		if (list2.anyMatch(NEST)) {
			return false;
		}
		if (livingEntity.getVehicle() != null && livingEntity.getVehicle().streamSelfAndPassengers()
				.anyMatch(AlienEntity.class::isInstance)) {
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
		Stream<BlockState> list2 = livingEntity.world
				.getStatesInBoxIfLoaded(livingEntity.getBoundingBox().expand(2.0, 2.0, 2.0));
		if (list2.anyMatch(NEST)) {
			return false;
		}
		if (livingEntity.getVehicle() != null && livingEntity.getVehicle().streamSelfAndPassengers()
				.anyMatch(AlienEntity.class::isInstance)) {
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
		double yOffset = mob.getEyeY() - ((mob.getEyeY() - mob.getBlockPos().getY()) / 2.0);
		double e = mob.getX() + ((mob.getRandom().nextDouble() / 2.0) - 0.5) * (mob.getRandom().nextBoolean() ? -1 : 1);
		double f = mob.getZ() + ((mob.getRandom().nextDouble() / 2.0) - 0.5) * (mob.getRandom().nextBoolean() ? -1 : 1);
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
		if (this.mob.hasPassengers()) {
			holdingCounter++;
			if (holdingCounter == 120) {
				this.mob.getNavigation().stop();
				mob.setIsExecuting(true);
				this.mob.setAttacking(false);
			}
			if (holdingCounter >= 125) {
				mob.getFirstPassenger().kill();
				mob.getFirstPassenger().world.addImportantParticle(Particles.BLOOD, e, yOffset, f, 0.0, -0.15, 0.0);
				mob.getFirstPassenger().setInvisible(false);
				mob.setIsExecuting(false);
				holdingCounter = 0;
			}
		}
	}

	protected void attack(LivingEntity target, double squaredDistance) {
		double d = this.getSquaredMaxAttackDistance(target);
		if (squaredDistance <= d) {
			Stream<BlockState> list = this.mob.world
					.getStatesInBoxIfLoaded(this.mob.getBoundingBox().expand(8.0, 8.0, 8.0));
			Stream<BlockState> list2 = target.world
					.getStatesInBoxIfLoaded(target.getBoundingBox().expand(2.0, 2.0, 2.0));
			SplittableRandom random = new SplittableRandom();
			int randomPhase = random.nextInt(0, 100);
			if ((list.anyMatch(NEST) && randomPhase >= 50) && !list2.anyMatch(NEST)) {
				this.mob.grabTarget(target);
			} else {
				if (!this.mob.hasPassengers()) {
					this.mob.tryAttack(target);
					this.mob.swingHand(Hand.MAIN_HAND);
					meleeCounter = -5;
				}
			}
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