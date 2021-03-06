package mods.cybercat.gigeresque.mixins.client.entity.render.feature;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin
		extends FeatureRenderer<SheepEntity, SheepEntityModel<SheepEntity>> {
	@Shadow
	@Final
	private static Identifier SKIN;
	@Shadow
	@Final
	private SheepWoolEntityModel<SheepEntity> model;

	private SheepWoolFeatureRendererMixin(FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>> context) {
		super(context);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
			SheepEntity sheepEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (!sheepEntity.isSheared() && sheepEntity instanceof Eggmorphable eggmorphable
				&& eggmorphable.isEggmorphing()) {
			this.getContextModel().copyStateTo(this.model);
			EggmorphFeatureRenderer.renderEggmorphedModel(this.model, SKIN, matrixStack, vertexConsumerProvider, light,
					sheepEntity, f, g, h, j, k, l);
		}
	}
}
