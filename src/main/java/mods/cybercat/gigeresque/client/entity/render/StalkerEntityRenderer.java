package mods.cybercat.gigeresque.client.entity.render;

import mods.cybercat.gigeresque.client.entity.model.StalkerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import mod.azure.azurelib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class StalkerEntityRenderer extends GeoEntityRenderer<StalkerEntity> {
	public StalkerEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new StalkerEntityModel());
		this.shadowRadius = 1.0f;
	}

	@Override
	protected float getDeathMaxRotation(StalkerEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
