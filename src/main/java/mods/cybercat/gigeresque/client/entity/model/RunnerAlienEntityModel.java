package mods.cybercat.gigeresque.client.entity.model;

import java.util.List;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class RunnerAlienEntityModel extends AnimatedTickingGeoModel<RunnerAlienEntity> {
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
	public void setCustomAnimations(RunnerAlienEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("neck");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI) / 340f);
	}
}
