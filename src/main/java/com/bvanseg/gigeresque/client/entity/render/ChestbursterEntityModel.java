package com.bvanseg.gigeresque.client.entity.render;

import java.util.List;

import com.bvanseg.gigeresque.client.entity.animation.EntityAnimations;
import com.bvanseg.gigeresque.client.entity.model.EntityModels;
import com.bvanseg.gigeresque.client.entity.texture.EntityTextures;
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class ChestbursterEntityModel extends AnimatedGeoModel<ChestbursterEntity> {
	@Override
	public Identifier getModelLocation(ChestbursterEntity object) {
		return EntityModels.CHESTBURSTER;
	}

	@Override
	public Identifier getTextureLocation(ChestbursterEntity object) {
		return EntityTextures.CHESTBURSTER;
	}

	@Override
	public Identifier getAnimationFileLocation(ChestbursterEntity animatable) {
		return EntityAnimations.CHESTBURSTER;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setLivingAnimations(ChestbursterEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("neck");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
	}
}
