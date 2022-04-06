package mods.cybercat.gigeresque.common.item.group;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GigItemGroups {
	private GigItemGroups() {
	}

	public static final ItemGroup GENERAL = FabricItemGroupBuilder.build(new Identifier(Gigeresque.MOD_ID, "general"),
			() -> new ItemStack(GIgBlocks.NEST_RESIN_WEB));
}
