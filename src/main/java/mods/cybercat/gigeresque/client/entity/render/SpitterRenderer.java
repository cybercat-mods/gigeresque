package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.SpitterModel;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class SpitterRenderer extends GeoEntityRenderer<SpitterEntity> {
	public SpitterRenderer(EntityRendererProvider.Context context) {
		super(context, new SpitterModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	protected float getDeathMaxRotation(SpitterEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
