package com.bvanseg.gigeresque.mixins;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Boston Vanseghi
 */
@Mixin(MemoryModuleType.class)
public interface MemoryModuleTypeInvoker {
    @Invoker("register")
    public static <U> MemoryModuleType<U> register(String id) {
        throw new AssertionError();
    }
}
