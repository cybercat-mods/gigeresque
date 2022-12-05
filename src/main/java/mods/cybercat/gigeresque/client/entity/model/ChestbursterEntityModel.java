package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationEvent;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityModel extends GeoModel<ChestbursterEntity> {
	@Override
	public ResourceLocation getModelResource(ChestbursterEntity object) {
		return EntityModels.CHESTBURSTER;
	}

	@Override
	public ResourceLocation getTextureResource(ChestbursterEntity object) {
		return EntityTextures.CHESTBURSTER;
	}

	@Override
	public ResourceLocation getAnimationResource(ChestbursterEntity animatable) {
		return EntityAnimations.CHESTBURSTER;
	}

	@Override
	public void setCustomAnimations(ChestbursterEntity animatable, long instanceId,
			AnimationEvent<ChestbursterEntity> animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		CoreGeoBone neck = getAnimationProcessor().getBone("head");
		EntityModelData entityData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);
		if (neck != null) {
			neck.setRotY((entityData.netHeadYaw() * (((float) Math.PI) / 340f)));
		}
	}
}
