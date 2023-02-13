package mods.cybercat.gigeresque.common.entity.ai.tasks;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.data.models.blockstates.PropertyDispatch.TriFunction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.TriPredicate;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

public class BreakBlocksTask<E extends AlienEntity> extends ExtendedBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList
			.of(Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));
	protected int timeToBreak = 0;
	protected int breakTime = 0;
	protected int breakProgress = -1;
	protected TriPredicate<E, BlockPos, BlockState> targetBlockPredicate = (entity, pos, state) -> state
			.is(BlockTags.DOORS);
	protected TriFunction<E, BlockPos, BlockState, Integer> digTimePredicate = (entity, pos, state) -> 240;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected void start(E entity) {
		entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
	}

	@Override
	protected void stop(E entity) {
		this.timeToBreak = 0;
		this.breakTime = 0;
		this.breakProgress = -1;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
		return entity.getTarget() != null;
	}

	public BreakBlocksTask<E> timeToBreak(TriFunction<E, BlockPos, BlockState, Integer> function) {
		this.digTimePredicate = function;

		return this;
	}

	@Override
	protected void tick(ServerLevel level, E entity, long gameTime) {
		var lightSourceLocation = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_BLOCKS.get()).orElse(null);
		if (lightSourceLocation == null)
			return;
		var lightPos = lightSourceLocation.stream().findFirst().get().getFirst();
		if (lightPos.closerToCenterThan(entity.position(), 2.0)) {
			this.breakTime++;
			int progress = (int) (this.breakTime / (float) this.timeToBreak * 10);
			entity.swing(InteractionHand.MAIN_HAND);
			if (progress != this.breakProgress) {
				entity.level.destroyBlockProgress(entity.getId(), lightPos, progress);

				this.breakProgress = progress;
			}

			if (this.breakTime >= this.timeToBreak) {
				entity.level.removeBlock(lightPos, false);
				entity.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, lightPos,
						Block.getId(entity.level.getBlockState(lightPos)));

				doStop((ServerLevel) entity.level, entity, entity.level.getGameTime());
			}
		}
	}

}
