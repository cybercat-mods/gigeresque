package mods.cybercat.gigeresque.mixins.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

/**
 * @author Aelpecyem
 */
@Mixin(SlimeOuterLayer.class)
public abstract class SlimeOverlayFeatureRendererMixin<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {

    @Shadow
    @Final
    private EntityModel<T> model;

    private SlimeOverlayFeatureRendererMixin(RenderLayerParent<T, SlimeModel<T>> context) {
        super(context);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void gigeresque$render(
        PoseStack matrixStack,
        MultiBufferSource vertexConsumerProvider,
        int light,
        T entity,
        float f,
        float g,
        float h,
        float j,
        float k,
        float l,
        CallbackInfo ci
    ) {
        if (entity.getInBlockState().is(GigBlocks.NEST_RESIN_WEB_CROSS.get())) {
            this.model.prepareMobModel(entity, f, g, h);
            this.model.setupAnim(entity, f, g, j, k, l);
            this.getParentModel().copyPropertiesTo(this.model);
            EggmorphFeatureRenderer.renderEggmorphedModel(
                this.model,
                getTextureLocation(entity),
                matrixStack,
                vertexConsumerProvider,
                light,
                entity,
                f,
                g,
                h,
                j,
                k,
                l
            );
        }
    }
}
