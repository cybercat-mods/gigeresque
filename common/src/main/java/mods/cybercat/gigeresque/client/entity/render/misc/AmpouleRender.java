package mods.cybercat.gigeresque.client.entity.render.misc;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.projectile.AmpouleProjectile;
import mods.cybercat.gigeresque.common.entity.impl.projectile.GooAmpouleProjectile;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AmpouleRender extends ArrowRenderer<AmpouleProjectile> {

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/misc/ampoule_projectile.png");

    public AmpouleRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AmpouleProjectile entity) {
        return TEXTURE;
    }
}
