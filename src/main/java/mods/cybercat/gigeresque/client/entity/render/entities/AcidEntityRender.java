package mods.cybercat.gigeresque.client.entity.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class AcidEntityRender extends EntityRenderer<Entity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/allay/allay.png");

    public AcidEntityRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return TEXTURE;
    }

    @Override
    public void render(@NotNull Entity entity, float f, float g, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource vertexConsumerProvider, int i) {
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.pushPose();
        matrixStack.scale(0, 0, 0);
        matrixStack.popPose();
    }
}
