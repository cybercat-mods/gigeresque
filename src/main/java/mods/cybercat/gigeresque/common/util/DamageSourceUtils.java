package mods.cybercat.gigeresque.common.util;

import net.minecraft.world.damagesource.DamageSource;

public class DamageSourceUtils {
	private DamageSourceUtils() {
	}

	public static boolean isDamageSourceNotPuncturing(DamageSource source) {
		return source.isExplosion() || source.isFire() || source.isMagic() || source.isFall() || source.isDamageHelmet();
	}
}
