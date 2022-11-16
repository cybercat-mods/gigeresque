package mods.cybercat.gigeresque.client.entity.model;

import java.util.List;

import com.mojang.math.Vector3f;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class AlienEntityModel extends AnimatedTickingGeoModel<ClassicAlienEntity> {

	@Override
	public ResourceLocation getModelResource(ClassicAlienEntity object) {
		return EntityModels.ALIEN;
	}

	@Override
	public ResourceLocation getTextureResource(ClassicAlienEntity object) {
		return object.isStatis() == true ? EntityTextures.ALIEN_STATIS : EntityTextures.ALIEN;
	}

	@Override
	public ResourceLocation getAnimationResource(ClassicAlienEntity animatable) {
		return EntityAnimations.ALIEN;
	}

	@Override
	public void setCustomAnimations(ClassicAlienEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		var body = getAnimationProcessor().getBone("body");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		body.setRotationY(Vector3f.YP.rotation((extraData.netHeadYaw + 3) * ((float) Math.PI / 180F)).j());
	}

}
