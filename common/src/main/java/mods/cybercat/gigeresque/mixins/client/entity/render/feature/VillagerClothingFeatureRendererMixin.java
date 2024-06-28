package mods.cybercat.gigeresque.mixins.client.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Aelpecyem
 */
@Mixin(VillagerProfessionLayer.class)
public abstract class VillagerClothingFeatureRendererMixin<T extends LivingEntity & VillagerDataHolder, M extends EntityModel<T> & VillagerHeadModel> extends RenderLayer<T, M> {

    private VillagerClothingFeatureRendererMixin(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Shadow
    protected abstract ResourceLocation getResourceLocation(String keyType, ResourceLocation keyId);

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;" + "Lnet/minecraft/client/renderer" + "/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
    private void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (livingEntity.hasEffect(GigStatusEffects.EGGMORPHING)) {
            var entityModel = this.getParentModel();
            EggmorphFeatureRenderer.renderEggmorphedModel(entityModel, getResourceLocation("type",
                            BuiltInRegistries.VILLAGER_TYPE.getKey(livingEntity.getVillagerData().getType())), matrixStack,
                    vertexConsumerProvider, light, livingEntity, f, g, h, j, k, l);
        }
    }
}
