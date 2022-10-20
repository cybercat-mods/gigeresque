package mods.cybercat.gigeresque.common.item.group;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GigItemGroups {
	private GigItemGroups() {
	}

	public static final CreativeModeTab GENERAL = FabricItemGroupBuilder.build(new ResourceLocation(Gigeresque.MOD_ID, "general"),
			() -> new ItemStack(GIgBlocks.NEST_RESIN_WEB));
}
