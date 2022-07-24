package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrawlerNavigation extends MobNavigation {
	private BlockPos targetPos;

	public CrawlerNavigation(MobEntity mobEntity, World world) {
		super(mobEntity, world);
	}

	public Path findPathTo(BlockPos target, int distance) {
		this.targetPos = target;
		return super.findPathTo(target, distance);
	}

	public Path findPathTo(Entity entity, int distance) {
		this.targetPos = entity.getBlockPos();
		return super.findPathTo(entity, distance);
	}

	public boolean startMovingTo(Entity entity, double speed) {
		Path path = this.findPathTo((Entity) entity, 0);
		if (path != null) {
			return this.startMovingAlong(path, speed);
		} else {
			this.targetPos = entity.getBlockPos();
			this.speed = speed;
			return true;
		}
	}

	public void tick() {
		if (!this.isIdle()) {
			super.tick();
		} else {
			if (this.targetPos != null) {
				if (!this.targetPos.isWithinDistance(this.entity.getPos(),
						Math.max((double) this.entity.getWidth(), 1.0D))
						&& (!(this.entity.getY() > (double) this.targetPos.getY())
								|| !(new BlockPos((double) this.targetPos.getX(), this.entity.getY(),
										(double) this.targetPos.getZ())).isWithinDistance(this.entity.getPos(),
												Math.max((double) this.entity.getWidth(), 1.0D)))) {
					this.entity.getMoveControl().moveTo((double) this.targetPos.getX(), (double) this.targetPos.getY(),
							(double) this.targetPos.getZ(), this.speed);
				} else {
					this.targetPos = null;
				}
			}

		}
	}

}
