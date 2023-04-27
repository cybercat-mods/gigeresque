package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.NeomorphAdolescentModel;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class NeomorphAdolescentRenderer extends GeoEntityRenderer<NeomorphAdolescentEntity> {
	public NeomorphAdolescentRenderer(EntityRendererProvider.Context context) {
		super(context, new NeomorphAdolescentModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	protected float getDeathMaxRotation(NeomorphAdolescentEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
