package mods.cybercat.gigeresque.common.block.animators;

import mod.azure.azurelib.common.animation.AzAnimatorConfig;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzBlockAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbectEntity;

public class PetrifiedAnimator extends AzBlockAnimator<PetrifiedOjbectEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/egg/egg.animation.json"
    );

    public PetrifiedAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<PetrifiedOjbectEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PetrifiedOjbectEntity animatable) {
        return ANIMATIONS;
    }
}
