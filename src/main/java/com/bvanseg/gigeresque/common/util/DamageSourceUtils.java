package com.bvanseg.gigeresque.common.util;

import net.minecraft.entity.damage.DamageSource;

public class DamageSourceUtils {
	private DamageSourceUtils() {
	}

	public static boolean isDamageSourceNotPuncturing(DamageSource source) {
		return source.isExplosive() || source.isFire() || source.isMagic() || source.isFromFalling()
				|| source.isFallingBlock();
	}
}
