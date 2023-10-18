package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.function.Consumer;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public abstract class CustomDelayedRangedBehaviour<E extends AlienEntity> extends ExtendedBehaviour<E> {
	protected final int delayTime;
	protected long delayFinishedAt = 0;
	protected Consumer<E> delayedCallback = entity -> {
	};

	public CustomDelayedRangedBehaviour(int delayTicks) {
		this.delayTime = delayTicks;

		runFor(entity -> Math.max(delayTicks, 120));
	}

	public final CustomDelayedRangedBehaviour<E> whenActivating(Consumer<E> callback) {
		this.delayedCallback = callback;

		return this;
	}

	@Override
	protected final void start(ServerLevel level, E entity, long gameTime) {
		if (this.delayTime > 0) {
			this.delayFinishedAt = gameTime + this.delayTime;
			super.start(level, entity, gameTime);
		} else {
			super.start(level, entity, gameTime);
			doDelayedAction(entity);
		}
		entity.triggerAnim("attackController", "acidspit");
	}

	@Override
	protected final void stop(ServerLevel level, E entity, long gameTime) {
		super.stop(level, entity, gameTime);

		this.delayFinishedAt = 0;
	}

	@Override
	protected boolean shouldKeepRunning(E entity) {
		return this.delayFinishedAt >= entity.level().getGameTime();
	}

	@Override
	protected final void tick(ServerLevel level, E entity, long gameTime) {
		super.tick(level, entity, gameTime);
		if (entity.getTarget() != null)
			BehaviorUtils.lookAtEntity(entity, entity.getTarget());
		if (this.delayFinishedAt <= gameTime) {
			doDelayedAction(entity);
			this.delayedCallback.accept(entity);
		}
	}

	protected void doDelayedAction(E entity) {
	}
}
