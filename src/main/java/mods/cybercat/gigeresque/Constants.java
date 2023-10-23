package mods.cybercat.gigeresque;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.resources.ResourceLocation;

public record Constants() {

    public static final int TPS = 20; // Ticks per second
    public static final int TPM = TPS * 60; // Ticks per minute
    public static final int TPD = TPM * 20; // Ticks per day

    public static final ResourceLocation modResource(String name) {
        return new ResourceLocation(Gigeresque.MOD_ID, name);
    }
}
