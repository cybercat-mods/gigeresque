package com.bvanseg.gigeresque.client.entity.model;

import java.util.List;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations;
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures;
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityModel extends AnimatedGeoModel<AquaticChestbursterEntity> {
	@Override
	public Identifier getModelLocation(AquaticChestbursterEntity object) {
		return EntityModels.AQUATIC_CHESTBURSTER;
	}

	@Override
	public Identifier getTextureLocation(AquaticChestbursterEntity object) {
		return EntityTextures.AQUATIC_CHESTBURSTER;
	}

	@Override
	public Identifier getAnimationFileLocation(AquaticChestbursterEntity animatable) {
		return EntityAnimations.AQUATIC_CHESTBURSTER;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setLivingAnimations(AquaticChestbursterEntity entity, Integer uniqueID,
			AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("neck");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
	}
}
