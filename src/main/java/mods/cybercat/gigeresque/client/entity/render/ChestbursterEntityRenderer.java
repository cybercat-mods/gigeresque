package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.ChestbursterEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.BusterBloodFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;

public class ChestbursterEntityRenderer extends GeoEntityRenderer<ChestbursterEntity> {

    public ChestbursterEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChestbursterEntityModel());
        this.shadowRadius = 0.1f;
        this.addRenderLayer(new BusterBloodFeatureRenderer(this));
    }

    @Override
    public void render(
        ChestbursterEntity entity,
        float entityYaw,
        float partialTicks,
        PoseStack stack,
        @NotNull MultiBufferSource bufferIn,
        int packedLightIn
    ) {
        float scaleFactor = 1.0f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    protected float getDeathMaxRotation(ChestbursterEntity entityLivingBaseIn) {
        return 0;
    }
}
