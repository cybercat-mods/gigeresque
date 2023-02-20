package mods.cybercat.gigeresque.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.TreasureMapForEmeralds;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

public class GigVillagerTrades {

	public static void addTrades() {
		List<VillagerTrades.ItemListing> gig_trades = new ArrayList<>(
				Arrays.asList(VillagerTrades.TRADES.get(VillagerProfession.CARTOGRAPHER).get(4)));
		gig_trades.add(new TreasureMapForEmeralds(13, GigTags.GIG_EXPLORER_MAPS, "filled_map.gig_dungeon",
				MapDecoration.Type.RED_MARKER, 12, 5));
		VillagerTrades.ItemListing[] resultlvl1 = new VillagerTrades.ItemListing[] {};
		VillagerTrades.TRADES.get(VillagerProfession.CARTOGRAPHER).put(4, gig_trades.toArray(resultlvl1));
	}
}
