package mods.cybercat.gigeresque.hacky;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class BlackFluidClientExtensions implements IClientFluidTypeExtensions {
    private static final ResourceLocation LAVA_STILL = Constants.modResource("block/black_fluid_still");
    private static final ResourceLocation LAVA_FLOW = Constants.modResource("block/black_fluid_flow");

    @Override
    public @NotNull ResourceLocation getStillTexture() {
        return LAVA_STILL;
    }

    @Override
    public @NotNull ResourceLocation getFlowingTexture() {
        return LAVA_FLOW;
    }
}