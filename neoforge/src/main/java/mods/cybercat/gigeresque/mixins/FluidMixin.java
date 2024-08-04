package mods.cybercat.gigeresque.mixins;

import mods.cybercat.gigeresque.NeoForgeMod;
import mods.cybercat.gigeresque.common.fluid.BlackFluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlackFluid.class)
public abstract class FluidMixin extends FlowingFluid {

    @Override
    public @NotNull FluidType getFluidType() {
        return NeoForgeMod.BLACKFLUID_TYPE.get();
    }
}