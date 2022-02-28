package mods.cybercat.gigeresque.common.entity.ai.brain;

import java.util.List;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.brain.task.AlienMeleeAttackTask;
import mods.cybercat.gigeresque.common.entity.ai.brain.task.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.brain.task.DestroyLightTask;
import mods.cybercat.gigeresque.common.entity.ai.brain.task.EggmorphTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.brain.task.FindNestingGroundTask;
import mods.cybercat.gigeresque.common.entity.ai.brain.task.PickUpEggmorphableTargetTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.RangedApproachTask;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.brain.task.WalkTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;

public class AdultAlienBrain extends ComplexBrain<AdultAlienEntity> {
	private double intelligence = entity.getAttributes().getValue(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE);

	public AdultAlienBrain(AdultAlienEntity entity) {
		super(entity);
	}

	private boolean isAquatic() {
		return entity instanceof AquaticAlienEntity;
	}

	private float aquaticLandPenalty = (isAquatic() && !entity.isTouchingWater()) ? 0.5f : 1.0f;

	@Override
	protected void addCoreActivities(List<Task<? super AdultAlienEntity>> tasks) {
		if (!isAquatic()) {
			tasks.add(new StayAboveWaterTask(0.8f));
			tasks.add(new FindNestingGroundTask(2.0));
			tasks.add(new BuildNestTask());
			tasks.add(new PickUpEggmorphableTargetTask(3.0));
			tasks.add(new EggmorphTargetTask(3.0));
		}
		tasks.add(new WalkTask(2.0f * aquaticLandPenalty));
		tasks.add(new LookAroundTask(45, 90));
		tasks.add(new WanderAroundTask());
	}

	@Override
	protected void addIdleActivities(List<Task<? super AdultAlienEntity>> tasks) {
		tasks.add(new UpdateAttackTargetTask<>(it -> true, this::getPreferredTarget));

		if (intelligence >= AlienEntityAttributes.SABOTAGE_THRESHOLD) {
			tasks.add(new DestroyLightTask((2.0 * aquaticLandPenalty) + (2 * (intelligence / 0.85))));
		}

		if (intelligence >= AlienEntityAttributes.SELF_PRESERVE_THRESHOLD) {
			tasks.add(avoidRepellentTask());
		}

		tasks.add(makeRandomWanderTask());
	}

	@Override
	protected void addAvoidActivities(List<Task<? super AdultAlienEntity>> tasks) {
	}

	@Override
	protected void addFightActivities(List<Task<? super AdultAlienEntity>> tasks) {
		tasks.add(new ForgetAttackTargetTask<>(it -> (brain.hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT)
				&& intelligence >= AlienEntityAttributes.SELF_PRESERVE_THRESHOLD) || ((Host) it).hasParasite()
				|| EntityUtils.isFacehuggerAttached(it)
				|| (entity.getBrain().hasMemoryModule(MemoryModuleType.HOME) && EntityUtils.isEggmorphable(it))
				|| ((Eggmorphable) it).isEggmorphing()
				|| it.getVehicle() != null && it.getVehicle() instanceof AlienEntity));
		tasks.add(new RangedApproachTask(3.0f * aquaticLandPenalty));
		tasks.add(new AlienMeleeAttackTask(20 * (int) intelligence));
	}

	private RandomTask<AdultAlienEntity> makeRandomWanderTask() {
		return new RandomTask<>(ImmutableList.of(Pair.of(new StrollTask(1.0f), 2), Pair.of(new WaitTask(60, 120), 1)));
	}
}
