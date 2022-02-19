package com.bvanseg.gigeresque.common.block.material;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;

public class Materials {
    public final static Material ACID = new MaterialBuilder(MapColor.TERRACOTTA_GREEN).allowsMovement().lightPassesThrough().notSolid().replaceable().destroyedByPiston().build();
    public final static Material NEST_RESIN = new MaterialBuilder(MapColor.GRAY).burnable().allowsMovement().build();
    public final static Material NEST_RESIN_WEB = new MaterialBuilder(MapColor.GRAY).burnable().allowsMovement().lightPassesThrough().build();
    public final static Material ORGANIC_ALIEN_BLOCK = new MaterialBuilder(MapColor.GRAY).build();
    public final static Material ROUGH_ALIEN_BLOCK = new MaterialBuilder(MapColor.GRAY).build();
    public final static Material SINOUS_ALIEN_BLOCK = new MaterialBuilder(MapColor.GRAY).build();
}
