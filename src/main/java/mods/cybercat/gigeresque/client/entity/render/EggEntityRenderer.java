package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.EggEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import mod.azure.azurelib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class EggEntityRenderer extends GeoEntityRenderer<AlienEggEntity> {
	public EggEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new EggEntityModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	protected float getDeathMaxRotation(AlienEggEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
