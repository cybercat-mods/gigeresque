package mods.cybercat.gigeresque.common.block.material;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class Materials {
	public final static Material ACID = new MaterialBuilder(MaterialColor.TERRACOTTA_GREEN).allowsMovement().lightPassesThrough().notSolid().replaceable().destroyedByPiston().build();
	public final static Material NEST_RESIN = new MaterialBuilder(MaterialColor.COLOR_GRAY).burnable().allowsMovement().build();
	public final static Material NEST_RESIN_WEB = new MaterialBuilder(MaterialColor.COLOR_GRAY).burnable().allowsMovement().lightPassesThrough().build();
	public final static Material ORGANIC_ALIEN_BLOCK = new MaterialBuilder(MaterialColor.COLOR_GRAY).build();
	public final static Material ROUGH_ALIEN_BLOCK = new MaterialBuilder(MaterialColor.COLOR_GRAY).build();
	public final static Material SINOUS_ALIEN_BLOCK = new MaterialBuilder(MaterialColor.COLOR_GRAY).build();
}
