package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.common.fluid.GigFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GigBucket extends BucketItem {

	public GigBucket() {
		super(GigFluids.BLACK_FLUID_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
	}

}
