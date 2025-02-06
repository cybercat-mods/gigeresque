package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.HellmorphRunnerAnimator;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;

public class HellmorphRunnerEntityRenderer extends AzEntityRenderer<HellmorphRunnerEntity> {

    public HellmorphRunnerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HellmorphRunnerEntity>builder(
                EntityModels.HELLMORPH_RUNNER,
                EntityTextures.HELLMORPH_RUNNER
            )
                .setAnimatorProvider(HellmorphRunnerAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }
}
