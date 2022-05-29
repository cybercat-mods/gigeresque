package mods.cybercat.gigeresque.client.entity.model;

import java.util.List;

import mods.cybercat.gigeresque.client.entity.animation.EntityAnimations;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class RunnerbursterEntityModel extends AnimatedTickingGeoModel<RunnerbursterEntity> {
	@Override
	public Identifier getModelResource(RunnerbursterEntity object) {
		return EntityModels.RUNNERBURSTER;
	}

	@Override
	public Identifier getTextureResource(RunnerbursterEntity object) {
		return EntityTextures.RUNNERBURSTER;
	}

	@Override
	public Identifier getAnimationResource(RunnerbursterEntity animatable) {
		return EntityAnimations.RUNNERBURSTER;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setLivingAnimations(RunnerbursterEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		var neck = getAnimationProcessor().getBone("neck");
		List<EntityModelData> extraDataList = customPredicate.getExtraDataOfType(EntityModelData.class);
		if (extraDataList.isEmpty())
			return;
		var extraData = extraDataList.get(0);
		neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI) / 340f);
	}
}
