package mods.cybercat.gigeresque.client.entity.render.templebeast;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.templebeast.DraconicTempleBeastAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.templebeast.DraconicTempleBeastAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;

public class DraconicTempleBeastEntityRenderer extends AzEntityRenderer<DraconicTempleBeastEntity> {

    public DraconicTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<DraconicTempleBeastEntity>builder(
                EntityModels.DRACONICTEMPLEBEAST,
                EntityTextures.DRACONICTEMPLEBEAST
            )
                .setAnimatorProvider(DraconicTempleBeastAnimator::new)
                .setRenderEntry(renderEntry -> {
                    DraconicTempleBeastAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setScale(1.23F)
                .setShadowRadius(1.0F)
                .build(),
            context
        );
    }
}
