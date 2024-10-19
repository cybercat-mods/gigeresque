package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

import mods.cybercat.gigeresque.client.entity.model.blocks.PetrifiedObjectModel;
import mods.cybercat.gigeresque.common.block.entity.PetrifiedOjbectEntity;

public class PetrifiedObjectRender extends GeoBlockRenderer<PetrifiedOjbectEntity> {

    public PetrifiedObjectRender() {
        super(new PetrifiedObjectModel());
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        PetrifiedOjbectEntity animatable,
        BakedGeoModel model,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        float red,
        float green,
        float blue,
        float alpha
    ) {
        super.preRender(
            poseStack,
            animatable,
            model,
            bufferSource,
            buffer,
            isReRender,
            partialTick,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
        );
        if (animatable instanceof PetrifiedOjbectEntity)
            model.getBone("resin").get().setHidden(true);
    }
}
