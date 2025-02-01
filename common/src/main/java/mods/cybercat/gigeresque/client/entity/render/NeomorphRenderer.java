package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeomorphAnimator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NeomorphRenderer extends AzEntityRenderer<NeomorphEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/neomorph/neomorph.geo.json");

    public NeomorphRenderer(EntityRendererProvider.Context context) {
        super(
                AzEntityRendererConfig.<NeomorphEntity>builder(
                                MODEL,
                                EntityTextures.NEOMORPH
                        )
                        .setAnimatorProvider(NeomorphAnimator::new)
                        .setDeathMaxRotation(0.0F)
                        .build(),
                context
        );
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(@NotNull NeomorphEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.scale(0.76F, 0.76F, 0.76F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
