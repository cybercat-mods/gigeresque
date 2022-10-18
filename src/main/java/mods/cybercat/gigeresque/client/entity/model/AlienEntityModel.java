package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class AlienEntityModel extends AnimatedTickingGeoModel<ClassicAlienEntity> {

	@Override
	public Identifier getModelResource(ClassicAlienEntity object) {
		return EntityModels.ALIEN;
	}

	@Override
	public Identifier getTextureResource(ClassicAlienEntity object) {
		return object.isStatis() == true ? EntityTextures.ALIEN_STATIS : EntityTextures.ALIEN;
	}

	@Override
	public Identifier getAnimationResource(ClassicAlienEntity animatable) {
		return EntityAnimations.ALIEN;
	}

	@Override
	public void setLivingAnimations(ClassicAlienEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone body = this.getAnimationProcessor().getBone("body");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (body != null) {
			body.setRotationY(
					Vec3f.POSITIVE_Y.getRadialQuaternion((extraData.netHeadYaw + 3) * ((float) Math.PI / 180F)).getY());
		}
	}

}
