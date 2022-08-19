package mods.cybercat.gigeresque.common.entity.ai.brain;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.ai.brain.task.FindNestingGroundTask;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import net.minecraft.entity.ai.brain.task.Task;

public class AdultAlienBrain extends ComplexBrain<AdultAlienEntity> {

	public AdultAlienBrain(AdultAlienEntity entity) {
		super(entity);
	}

	@Override
	protected void addCoreActivities(List<Task<? super AdultAlienEntity>> tasks) {
		tasks.add(new FindNestingGroundTask(2.0));
	}

	@Override
	protected void addAvoidActivities(List<Task<? super AdultAlienEntity>> tasks) {
	}
}