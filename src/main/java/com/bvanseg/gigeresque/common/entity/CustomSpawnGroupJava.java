package com.bvanseg.gigeresque.common.entity;

import net.minecraft.entity.SpawnGroup;

public class CustomSpawnGroupJava {
    static {
        SpawnGroup.values(); // Ensure class is loaded before the variant is accessed
    }

    public static SpawnGroup ALIEN;
}
