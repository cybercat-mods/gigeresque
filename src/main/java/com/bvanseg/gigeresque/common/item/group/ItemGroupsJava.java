package com.bvanseg.gigeresque.common.item.group;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.block.BlocksJava;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroupsJava {
    private ItemGroupsJava() {
    }

    public static final ItemGroup GENERAL = FabricItemGroupBuilder.build(new Identifier(GigeresqueJava.MOD_ID, "general"), () -> new ItemStack(BlocksJava.NEST_RESIN_WEB));
}
