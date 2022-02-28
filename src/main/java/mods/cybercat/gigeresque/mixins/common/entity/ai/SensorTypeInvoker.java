package mods.cybercat.gigeresque.mixins.common.entity.ai;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

/**
 * @author Boston Vanseghi
 */
@Mixin(SensorType.class)
public interface SensorTypeInvoker {
	@Invoker("register")
	static <U extends Sensor<?>> SensorType<U> register(String id, Supplier<U> factory) {
		throw new AssertionError();
	}
}
