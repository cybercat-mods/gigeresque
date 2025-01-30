package mods.cybercat.gigeresque.common.entity.animators.misc;

import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.controller.keyframe.AzKeyframeCallbacks;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HologramAnimator extends AzEntityAnimator<HologramEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
            "animations/entity/engineer_hologram/engineer_hologram.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<HologramEntity> animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                        .setTransitionLength(5)
                        .setKeyframeCallbacks(
                                AzKeyframeCallbacks.<HologramEntity>builder()
                                        .setParticleKeyframeHandler(
                                                event -> {
                                                    if (event.getKeyframeData().getEffect().equals("smoke")) {
                                                        double d2 = event.getAnimatable().getX() + (event.getAnimatable().getRandom().nextDouble()) * event.getAnimatable().getBbWidth() * 0.5D;
                                                        double f2 = event.getAnimatable().getZ() + (event.getAnimatable().getRandom().nextDouble()) * event.getAnimatable().getBbWidth() * 0.5D;
                                                        event.getAnimatable().level().addParticle(ParticleTypes.FLASH, true, d2, event.getAnimatable().getY(0.5), f2, 0, 0, 0);
                                                    }
                                                }
                                        )
                                        .build()
                        )
                        .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(HologramEntity animatable) {
        return ANIMATIONS;
    }
}
