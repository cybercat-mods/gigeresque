package mods.cybercat.gigeresque.common.item.group;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.item.GigItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GigItemGroups {

	private GigItemGroups() {
	}

	public static final CreativeModeTab GENERAL = FabricItemGroupBuilder.build(Constants.modResource("items"),
			() -> new ItemStack(GigItems.ALIEN_SPAWN_EGG));
	
	public static final CreativeModeTab BLOCKS = FabricItemGroupBuilder.build(Constants.modResource("blocks"),
			() -> new ItemStack(GIgBlocks.NEST_RESIN_WEB));
}
