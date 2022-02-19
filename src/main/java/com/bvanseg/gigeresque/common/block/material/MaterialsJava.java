package com.bvanseg.gigeresque.common.block.material;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;

public class MaterialsJava {
    public final static Material ACID = new MaterialBuilderJava(MapColor.TERRACOTTA_GREEN).allowsMovement().lightPassesThrough().notSolid().replaceable().destroyedByPiston().build();
    public final static Material NEST_RESIN = new MaterialBuilderJava(MapColor.GRAY).burnable().allowsMovement().build();
    public final static Material NEST_RESIN_WEB = new MaterialBuilderJava(MapColor.GRAY).burnable().allowsMovement().lightPassesThrough().build();
    public final static Material ORGANIC_ALIEN_BLOCK = new MaterialBuilderJava(MapColor.GRAY).build();
    public final static Material ROUGH_ALIEN_BLOCK = new MaterialBuilderJava(MapColor.GRAY).build();
    public final static Material SINOUS_ALIEN_BLOCK = new MaterialBuilderJava(MapColor.GRAY).build();
}
