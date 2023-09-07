package mods.cybercat.gigeresque.client.entity.model;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class NeobursterModel extends DefaultedEntityGeoModel<NeobursterEntity> {

	public NeobursterModel() {
		super(Constants.modResource("neoburster/neoburster"), false);
	}

	@Override
	public RenderType getRenderType(NeobursterEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
	
	@Override
	public void setCustomAnimations(NeobursterEntity animatable, long instanceId, AnimationState<NeobursterEntity> animationState) {

		CoreGeoBone head = getAnimationProcessor().getBone("main");

		if (head != null) 
			head.setRotY(animationState.getData(DataTickets.ENTITY_MODEL_DATA).headPitch() * Mth.DEG_TO_RAD); 
		
		
		super.setCustomAnimations(animatable, instanceId, animationState);
	}

}
