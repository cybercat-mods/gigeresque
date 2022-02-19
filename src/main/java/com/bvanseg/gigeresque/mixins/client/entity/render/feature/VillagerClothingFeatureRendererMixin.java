package com.bvanseg.gigeresque.mixins.client.entity.render.feature;

import com.bvanseg.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerDataContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(VillagerClothingFeatureRenderer.class)
public abstract class VillagerClothingFeatureRendererMixin<T extends LivingEntity & VillagerDataContainer,
        M extends EntityModel<T> & ModelWithHat> extends FeatureRenderer<T, M> {

    private VillagerClothingFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Shadow
    protected abstract Identifier findTexture(String keyType, Identifier keyId);

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;" + "Lnet/minecraft/client/render" +
            "/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
                        T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (livingEntity instanceof Eggmorphable eggmorphable && eggmorphable.isEggmorphing()) {
            M entityModel = this.getContextModel();
            EggmorphFeatureRenderer.renderEggmorphedModel(entityModel, findTexture("type",
                            Registry.VILLAGER_TYPE.getId(livingEntity.getVillagerData().getType())), matrixStack,
                    vertexConsumerProvider, light, livingEntity, f, g, h, j, k, l);
        }
    }
}
