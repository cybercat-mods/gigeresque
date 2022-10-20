package mods.cybercat.gigeresque.mixins.client.entity.render.feature;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(SheepFurLayer.class)
public abstract class SheepWoolFeatureRendererMixin extends RenderLayer<Sheep, SheepModel<Sheep>> {
	@Shadow
	@Final
	private static ResourceLocation SHEEP_FUR_LOCATION;
	@Shadow
	@Final
	private SheepModel<Sheep> model;

	private SheepWoolFeatureRendererMixin(RenderLayerParent<Sheep, SheepModel<Sheep>> context) {
		super(context);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, Sheep sheepEntity,
			float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (!sheepEntity.isSheared() && sheepEntity instanceof Eggmorphable eggmorphable
				&& eggmorphable.isEggmorphing()) {
			this.getParentModel().copyPropertiesTo(this.model);
			EggmorphFeatureRenderer.renderEggmorphedModel(this.model, SHEEP_FUR_LOCATION, matrixStack,
					vertexConsumerProvider, light, sheepEntity, f, g, h, j, k, l);
		}
	}
}
