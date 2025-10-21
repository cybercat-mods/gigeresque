package mods.cybercat.gigeresque.client.entity.render.neo;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeobursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo.NeobursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;

public class NeobursterRenderer extends AzEntityRenderer<NeobursterEntity> {

    public NeobursterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeobursterEntity>builder(EntityModels.NEOBURSTER, EntityTextures.NEOBURSTER)
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(NeobursterAnimator::new)
                .setRenderEntry(renderEntry -> {
                    NeobursterAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .addRenderLayer(new BloodAzLayer<>())
                .setShadowRadius(0.25F)
                .setScale(bursterEntity -> 1.0f + ((bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()) / 5.0f))
                .setPrerenderEntry(rendererPipelineContext -> {
                    if (rendererPipelineContext.bakedModel().getBone("sac").isPresent()) {
                        rendererPipelineContext.bakedModel().getBone("sac").get().setHidden(true);
                    }
                    return rendererPipelineContext;
                })
                .build(),
            context
        );
    }
}
