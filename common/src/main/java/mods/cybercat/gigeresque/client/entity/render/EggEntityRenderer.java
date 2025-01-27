package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.common.entity.helper.states.EggStates;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.AlienEggAnimator;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;

public class EggEntityRenderer extends AzEntityRenderer<AlienEggEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/egg/egg.geo.json");

    private static final RenderType EGG_RENDER_TYPE = RenderType.entityCutoutNoCull(EntityTextures.EGG);

    private static final RenderType EGG_ACTIVE_RENDER_TYPE = RenderType.entityTranslucent(EntityTextures.EGG_ACTIVE);

    public EggEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AlienEggEntity>builder(
                alienEggEntity -> MODEL,
                alienEggEntity -> alienEggEntity.getEggState() == EggStates.HATCHING.ordinal() || alienEggEntity.getEggState() == EggStates.HATCHED.ordinal() ? EntityTextures.EGG_ACTIVE : EntityTextures.EGG
            )
                .setAnimatorProvider(AlienEggAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setRenderType(
                    alienEggEntity -> alienEggEntity.getEggState() == EggStates.HATCHING.ordinal() || alienEggEntity.getEggState() == EggStates.HATCHED.ordinal()
                        ? EGG_ACTIVE_RENDER_TYPE
                        : EGG_RENDER_TYPE
                )
                .build(),
            context
        );
        this.shadowRadius = 0.5f;
    }
}
