package mods.cybercat.gigeresque.client.entity.model;

import java.util.List;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class AquaticChestbursterEntityModel extends AnimatedTickingGeoModel<AquaticChestbursterEntity> {
	@Override
	public ResourceLocation getModelResource(AquaticChestbursterEntity object) {
		return EntityModels.AQUATIC_CHESTBURSTER;
	}

	@Override
	public ResourceLocation getTextureResource(AquaticChestbursterEntity object) {
		return EntityTextures.AQUATIC_CHESTBURSTER;
	}

	@Override
	public ResourceLocation getAnimationResource(AquaticChestbursterEntity animatable) {
		return EntityAnimations.AQUATIC_CHESTBURSTER;
	}

	@Override
	public void setCustomAnimations(AquaticChestbursterEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("head");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
	}
}
