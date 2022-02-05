package com.bvanseg.gigeresque.mixins.client.entity.render.feature;

import com.bvanseg.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(SlimeOverlayFeatureRenderer.class)
public abstract class SlimeOverlayFeatureRendererMixin<T extends LivingEntity> extends FeatureRenderer<T,
        SlimeEntityModel<T>> {
    @Shadow
    @Final
    private EntityModel<T> model;

    private SlimeOverlayFeatureRendererMixin(FeatureRendererContext<T, SlimeEntityModel<T>> context) {
        super(context);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T entity,
                        float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (entity instanceof Eggmorphable eggmorphable && eggmorphable.isEggmorphing()) {
            this.model.animateModel(entity, f, g, h);
            this.model.setAngles(entity, f, g, j, k, l);
            this.getContextModel().copyStateTo(this.model);
            EggmorphFeatureRenderer.renderEggmorphedModel(this.model, getTexture(entity), matrixStack,
                    vertexConsumerProvider, light, entity, f, g, h, j, k, l);
        }
    }
}
