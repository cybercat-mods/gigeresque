package mods.cybercat.gigeresque.client;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.render.helper.EntityHeadOffsetData;

public class FabricHeadOffsetReloadListener extends EntityHeadOffsetData.ReloadListener implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return Constants.modResource("gigeresque_head_offsets");
    }
}
