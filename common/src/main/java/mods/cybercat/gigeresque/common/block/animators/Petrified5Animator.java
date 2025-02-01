package mods.cybercat.gigeresque.common.block.animators;

import mod.azure.azurelib.rewrite.animation.AzAnimatorConfig;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzBlockAnimator;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect5Entity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Petrified5Animator extends AzBlockAnimator<PetrifiedOjbect5Entity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
            "animations/block/neomorph_spore_pods/neomorph_spore_pods.animation.json"
    );

    public Petrified5Animator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<PetrifiedOjbect5Entity> animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                        .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PetrifiedOjbect5Entity animatable) {
        return ANIMATIONS;
    }
}
