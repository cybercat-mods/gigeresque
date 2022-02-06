package com.bvanseg.gigeresque.common.block.material;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;

public class MaterialBuilderJava {
    private PistonBehavior pistonBehavior;
    private boolean blocksMovement;
    private boolean burnable = false;
    private boolean liquid = false;
    private boolean replaceable = false;
    private boolean solid;
    private MapColor color;
    private boolean blocksLight;

    MaterialBuilderJava(MapColor color) {
        pistonBehavior = PistonBehavior.NORMAL;
        blocksMovement = true;
        solid = true;
        blocksLight = true;
        this.color = color;
    }

    public MaterialBuilderJava liquid() {
        liquid = true;
        return this;
    }

    public MaterialBuilderJava notSolid() {
        solid = false;
        return this;
    }

    public MaterialBuilderJava allowsMovement() {
        blocksMovement = false;
        return this;
    }

    public MaterialBuilderJava lightPassesThrough() {
        blocksLight = false;
        return this;
    }

    public MaterialBuilderJava burnable() {
        burnable = true;
        return this;
    }

    public MaterialBuilderJava replaceable() {
        replaceable = true;
        return this;
    }

    public MaterialBuilderJava destroyedByPiston() {
        pistonBehavior = PistonBehavior.DESTROY;
        return this;
    }

    public MaterialBuilderJava blocksPiston() {
        pistonBehavior = PistonBehavior.BLOCK;
        return this;
    }

    public Material build() {
        return new Material(color, liquid, solid, blocksMovement, blocksLight, burnable, replaceable, pistonBehavior);
    }
}
