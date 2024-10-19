package mods.cybercat.gigeresque.common.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;

public class DamageSourceUtils {

    private DamageSourceUtils() {}

    public static boolean isDamageSourceNotPuncturing(DamageSource source, DamageSources sources) {
        return source == sources.onFire() || source == sources.magic() || source == sources.fall();
    }
}
