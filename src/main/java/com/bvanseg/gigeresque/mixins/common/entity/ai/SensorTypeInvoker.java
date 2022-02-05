package com.bvanseg.gigeresque.mixins.common.entity.ai;

import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

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
