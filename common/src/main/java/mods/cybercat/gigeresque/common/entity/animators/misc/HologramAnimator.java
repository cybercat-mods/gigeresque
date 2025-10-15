package mods.cybercat.gigeresque.common.entity.animators.misc;

import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.controller.keyframe.AzKeyframeCallbacks;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.misc.HologramEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;

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
                        .setSoundKeyframeHandler(event -> {
                            if (event.getKeyframeData().getSound().equals("step")) {
                                event.getAnimatable()
                                    .level()
                                    .playLocalSound(
                                        event.getAnimatable().getX(),
                                        event.getAnimatable().getY(),
                                        event.getAnimatable().getZ(),
                                        GigSounds.TRACKER_SUMMON.get(),
                                        SoundSource.HOSTILE,
                                        0.5F,
                                        1.0F,
                                        true
                                    );
                            }
                        })
                        .setParticleKeyframeHandler(
                            event -> {
                                if (event.getKeyframeData().getEffect().equals("smoke")) {
                                    double d2 = event.getAnimatable().getX() + (event.getAnimatable().getRandom().nextDouble()) * event
                                        .getAnimatable()
                                        .getBbWidth() * 0.5D;
                                    double f2 = event.getAnimatable().getZ() + (event.getAnimatable().getRandom().nextDouble()) * event
                                        .getAnimatable()
                                        .getBbWidth() * 0.5D;
                                    event.getAnimatable()
                                        .level()
                                        .addParticle(ParticleTypes.FLASH, true, d2, event.getAnimatable().getY(0.5), f2, 0, 0, 0);
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
