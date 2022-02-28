package mods.cybercat.gigeresque.mixins.common.entity.ai;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.ai.brain.MemoryModuleType;

/**
 * @author Boston Vanseghi
 */
@Mixin(MemoryModuleType.class)
public interface MemoryModuleTypeInvoker {
	@Invoker("register")
	static <U> MemoryModuleType<U> register(String id) {
		throw new AssertionError();
	}
}
