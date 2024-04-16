package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mods.cybercat.gigeresque.client.entity.model.NeomorphModel;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class NeomorphRenderer extends GeoEntityRenderer<NeomorphEntity> {
    public NeomorphRenderer(EntityRendererProvider.Context context) {
        super(context, new NeomorphModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void preRender(PoseStack poseStack, NeomorphEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.scale(0.76F, 0.76F, 0.76F);
    }

    @Override
    protected float getDeathMaxRotation(NeomorphEntity entityLivingBaseIn) {
        return 0.0F;
    }
}
