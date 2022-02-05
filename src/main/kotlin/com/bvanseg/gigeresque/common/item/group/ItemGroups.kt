package com.bvanseg.gigeresque.common.item.group

import com.bvanseg.gigeresque.common.Gigeresque
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier

/**
 * @author Boston Vanseghi
 */
object ItemGroups {
    val GENERAL = FabricItemGroupBuilder.build(
        Identifier(Gigeresque.MOD_ID, "general")
    ) { ItemStack(Items.BOWL) }
}