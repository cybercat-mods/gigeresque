package mods.cybercat.gigeresque.common.block;

import net.minecraft.world.level.block.Block;

public class GigBlock extends Block {

	public GigBlock(Properties settings) {
		super(settings);
	}

	@Override
	public boolean isPossibleToRespawnInThis() {
		return false;
	}

}
