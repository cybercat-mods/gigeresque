package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.common.item.group.GigItemGroups;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class GigSpawnEgg extends SpawnEggItem {

	public GigSpawnEgg(EntityType<? extends Mob> type, int primaryColor, int secondaryColor) {
		super(type, primaryColor, secondaryColor, new Item.Properties().stacksTo(1).tab(GigItemGroups.GENERAL));
	}

}
