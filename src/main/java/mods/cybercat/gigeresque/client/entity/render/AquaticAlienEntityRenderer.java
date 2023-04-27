package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.AquaticAlienEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import mod.azure.azurelib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AquaticAlienEntityRenderer extends GeoEntityRenderer<AquaticAlienEntity> {
	public AquaticAlienEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new AquaticAlienEntityModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	protected float getDeathMaxRotation(AquaticAlienEntity entityLivingBaseIn) {
		return 0;
	}
}
