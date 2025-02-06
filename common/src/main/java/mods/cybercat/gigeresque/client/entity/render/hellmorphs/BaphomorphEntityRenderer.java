package mods.cybercat.gigeresque.client.entity.render.hellmorphs;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.hellmorphs.BaphomorphAnimator;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.BaphomorphEntity;

public class BaphomorphEntityRenderer extends AzEntityRenderer<BaphomorphEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/baphomorph/baphomorph.geo.json");

    public BaphomorphEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<BaphomorphEntity>builder(
                MODEL,
                EntityTextures.BAPHOMORPH
            )
                .setAnimatorProvider(BaphomorphAnimator::new)
                .setDeathMaxRotation(0.0F)
                .build(),
            context
        );
        this.shadowRadius = 1.0f;
    }
}
