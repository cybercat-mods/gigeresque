package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObjectModel;
import mods.cybercat.gigeresque.common.block.entity.PetrifiedOjbectEntity;
import net.minecraft.client.renderer.MultiBufferSource;

public class PetrifiedObjectRender extends GeoBlockRenderer<PetrifiedOjbectEntity> {
    public PetrifiedObjectRender() {
        super(new PetrifiedObjectModel());
    }

    @Override
    public void preRender(PoseStack poseStack, PetrifiedOjbectEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, color);
        if (animatable instanceof PetrifiedOjbectEntity)
            model.getBone("resin").get().setHidden(true);
    }
}