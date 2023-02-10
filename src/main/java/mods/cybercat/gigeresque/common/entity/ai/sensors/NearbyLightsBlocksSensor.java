package mods.cybercat.gigeresque.common.entity.ai.sensors;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.ai.GigSensors;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;

public class NearbyLightsBlocksSensor<E extends LivingEntity> extends PredicateSensor<BlockState, E> {
	private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList
			.of(GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get());

	protected SquareRadius radius = new SquareRadius(1, 1);

	public NearbyLightsBlocksSensor() {
		setPredicate((state, entity) -> !state.isAir());
	}

	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return MEMORIES;
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return GigSensors.NEARBY_LIGHT_BLOCKS.get();
	}

	/**
	 * Set the radius for the sensor to scan
	 * 
	 * @param radius The coordinate radius, in blocks
	 * @return this
	 */
	public NearbyLightsBlocksSensor<E> setRadius(double radius) {
		return setRadius(radius, radius);
	}

	/**
	 * Set the radius for the sensor to scan.
	 * 
	 * @param xz The X/Z coordinate radius, in blocks
	 * @param y  The Y coordinate radius, in blocks
	 * @return this
	 */
	public NearbyLightsBlocksSensor<E> setRadius(double xz, double y) {
		this.radius = new SquareRadius(xz, y);

		return this;
	}

	@Override
	protected void doTick(ServerLevel level, E entity) {
		List<Pair<BlockPos, BlockState>> blocks = new ObjectArrayList<>();

		for (BlockPos pos : BlockPos.betweenClosed(entity.blockPosition().subtract(this.radius.toVec3i()),
				entity.blockPosition().offset(this.radius.toVec3i()))) {
			BlockState state = level.getBlockState(pos);

			if (this.predicate().test(state, entity))
				blocks.add(Pair.of(pos.immutable(), state));
		}

		if (blocks.isEmpty())
			BrainUtils.clearMemory(entity, GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get());
		else
			BrainUtils.setMemory(entity, GigMemoryTypes.NEARBY_LIGHT_BLOCKS.get(), blocks);
	}
}