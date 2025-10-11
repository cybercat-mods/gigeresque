package mods.cybercat.gigeresque.common.block.animators;

import mod.azure.azurelib.common.animation.AzAnimatorConfig;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzBlockAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;

public class JarAnimator extends AzBlockAnimator {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/block/jar/jar.animation.json"
    );

    public JarAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Object animatable) {
        return ANIMATIONS;
    }
}
