package com.bvanseg.gigeresque;

import net.minecraft.entity.SpawnGroup;

/**
 * @author Boston Vanseghi
 */
public class CustomSpawnGroup {
	static {
		SpawnGroup.values(); // Ensure class is loaded before the variant is accessed
	}

	public static SpawnGroup ALIEN;
}
