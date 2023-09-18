package mods.cybercat.gigeresque.common.entity.helper;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.interfacing.IClimberEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class BetterSpiderPathNavigator<T extends Mob & IClimberEntity> extends AdvancedClimberPathNavigator<T> {
	private boolean useVanillaBehaviour;
	private BlockPos targetPosition;

	public BetterSpiderPathNavigator(T entity, Level worldIn, boolean useVanillaBehaviour) {
		super(entity, worldIn, false, true, true);
		this.useVanillaBehaviour = useVanillaBehaviour;
	}

	@Override
	public Path createPath(BlockPos pos, int p_179680_2_) {
		this.targetPosition = pos;
		return super.createPath(pos, p_179680_2_);
	}

	@Override
	public Path createPath(Entity entityIn, int p_75494_2_) {
		this.targetPosition = entityIn.blockPosition();
		return super.createPath(entityIn, p_75494_2_);
	}

	@Override
	public boolean moveTo(Entity entityIn, double speedIn) {
		Path path = this.createPath(entityIn, 0);
		if(path != null) {
			return this.moveTo(path, speedIn);
		} else {
			this.targetPosition = entityIn.blockPosition();
			this.speedModifier = speedIn;
			return true;
		}
	}

	@Override
	public void tick() {
		if(!this.isDone()) {
			super.tick();
		} else {
			if(this.targetPosition != null && this.useVanillaBehaviour) {
				// FORGE: Fix MC-94054
				if(!this.targetPosition.closerThan(this.mob.blockPosition(), Math.max((double) this.mob.getBbWidth(), 1.0D)) && (!(this.mob.getY() > (double) this.targetPosition.getY()) || !(Constants.blockPos(this.targetPosition.getX(), this.mob.getY(), this.targetPosition.getZ())).closerThan(this.mob.blockPosition(), Math.max((double) this.mob.getBbWidth(), 1.0D)))) {
					this.mob.getMoveControl().setWantedPosition((double) this.targetPosition.getX(), (double) this.targetPosition.getY(), (double) this.targetPosition.getZ(), this.speedModifier);
				} else {
					this.targetPosition = null;
				}
			}

		}
	}
}
