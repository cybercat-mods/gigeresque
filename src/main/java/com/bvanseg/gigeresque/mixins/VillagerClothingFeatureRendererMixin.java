package com.bvanseg.gigeresque.mixins;

import com.bvanseg.gigeresque.client.entity.render.feature.EggmorphFeatureRenderer;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerResourceMetadata;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerClothingFeatureRenderer.class)
public abstract class VillagerClothingFeatureRendererMixin<T extends LivingEntity & VillagerDataContainer,
		M extends EntityModel<T> & ModelWithHat> extends FeatureRenderer<T, M> {

	@Shadow
	@Final
	private Object2ObjectMap<VillagerType, VillagerResourceMetadata.HatType> villagerTypeToHat;
	@Shadow
	@Final
	private Object2ObjectMap<VillagerProfession, VillagerResourceMetadata.HatType> professionToHat;

	private VillagerClothingFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Shadow
	protected abstract Identifier findTexture(String keyType, Identifier keyId);

	@Shadow
	public abstract <K> VillagerResourceMetadata.HatType getHatType(Object2ObjectMap<K,
			VillagerResourceMetadata.HatType> hatLookUp, String keyType, DefaultedRegistry<K> registry, K key);

	@Inject(method = "render", at = @At("TAIL"))
	private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
						T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (livingEntity instanceof Eggmorphable eggmorphable && eggmorphable.isEggmorphing()) {
			VillagerType villagerType = livingEntity.getVillagerData().getType();
			M entityModel = this.getContextModel();
			VillagerResourceMetadata.HatType hatType = this.getHatType(this.villagerTypeToHat, "type",
					Registry.VILLAGER_TYPE, villagerType);
			EggmorphFeatureRenderer.renderEggmorphedModel(entityModel, findTexture("type",
					Registry.VILLAGER_TYPE.getId(livingEntity.getVillagerData().getType())), matrixStack,
					vertexConsumerProvider, light, livingEntity, f, g, h, j, k, l);
		}
	}
}
