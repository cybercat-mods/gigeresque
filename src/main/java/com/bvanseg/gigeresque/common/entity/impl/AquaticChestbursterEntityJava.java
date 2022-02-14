package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.common.entity.GrowableJava;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

public class AquaticChestbursterEntityJava extends ChestbursterEntityJava implements IAnimatable, GrowableJava {
    public AquaticChestbursterEntityJava(EntityType<? extends AquaticChestbursterEntityJava> type, World world) {
        super(type, world);
    }
}
