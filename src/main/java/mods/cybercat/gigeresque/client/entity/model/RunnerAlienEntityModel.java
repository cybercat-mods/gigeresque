package mods.cybercat.gigeresque.client.entity.model;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationEvent;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityModel extends GeoModel<RunnerAlienEntity> {
	@Override
	public ResourceLocation getModelResource(RunnerAlienEntity object) {
		return EntityModels.RUNNER_ALIEN;
	}

	@Override
	public ResourceLocation getTextureResource(RunnerAlienEntity object) {
		return EntityTextures.RUNNER_ALIEN;
	}

	@Override
	public ResourceLocation getAnimationResource(RunnerAlienEntity animatable) {
		return EntityAnimations.RUNNER_ALIEN;
	}

	@Override
	public void setCustomAnimations(RunnerAlienEntity animatable, long instanceId,
			AnimationEvent<RunnerAlienEntity> animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		CoreGeoBone neck = getAnimationProcessor().getBone("head");
		EntityModelData entityData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);
		if (neck != null) {
			neck.setRotY((entityData.netHeadYaw() * (((float) Math.PI) / 340f)));
		}
	}
}
