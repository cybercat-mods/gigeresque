package mods.cybercat.gigeresque.common.block;

import net.minecraft.block.Block;

public class GigBlock extends Block {

	public GigBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canMobSpawnInside() {
		return false;
	}

}
