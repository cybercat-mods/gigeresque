package com.bvanseg.gigeresque.common.entity;

import net.minecraft.entity.SpawnGroup;

public class CustomSpawnGroup {
	static {
		SpawnGroup.values(); // Ensure class is loaded before the variant is accessed
	}

	public static SpawnGroup ALIEN;
}
