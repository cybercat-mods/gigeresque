package mods.cybercat.gigeresque.client.entity.model;

import java.util.List;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityModel extends AnimatedTickingGeoModel<ChestbursterEntity> {
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
	public void setCustomAnimations(ChestbursterEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("head");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
	}
}
