package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.HellmorphRunnerAnimator;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellmorphRunnerEntity;

public class HellmorphRunnerEntityRenderer extends AzEntityRenderer<HellmorphRunnerEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/hellmorph_runner/hellmorph_runner.geo.json");

    public HellmorphRunnerEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<HellmorphRunnerEntity>builder(
                MODEL,
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
