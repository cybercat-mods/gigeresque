package mods.cybercat.gigeresque.client.entity.model;

import java.util.List;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class AlienEntityModel extends AnimatedTickingGeoModel<ClassicAlienEntity> {

	@Override
	public Identifier getModelLocation(ClassicAlienEntity object) {
		return EntityModels.ALIEN;
	}

	@Override
	public Identifier getTextureLocation(ClassicAlienEntity object) {
		return EntityTextures.ALIEN;
	}

	@Override
	public Identifier getAnimationFileLocation(ClassicAlienEntity animatable) {
		return EntityAnimations.ALIEN;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setLivingAnimations(ClassicAlienEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("neck");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * (((float) Math.PI) / 340f));
	}

}
