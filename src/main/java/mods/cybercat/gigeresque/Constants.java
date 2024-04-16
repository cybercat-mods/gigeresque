package mods.cybercat.gigeresque;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.resources.ResourceLocation;

public record Constants() {

    public static final int TPS = 20; // Ticks per second
    public static final int TPM = TPS * 60; // Ticks per minute
    public static final int TPD = TPM * 20; // Ticks per day

    public static final String ATTACK_CONTROLLER = "attackController";
    public static final String LIVING_CONTROLLER = "livingController";
    public static final String LEFT_CLAW = "left_claw";
    public static final String RIGHT_CLAW = "right_claw";
    public static final String LEFT_TAIL = "left_tail";
    public static final String RIGHT_TAIL = "right_tail";
    public static final String RIGHT_CLAW_BASIC = "right_claw_basic";
    public static final String LEFT_TAIL_BASIC = "left_tail_basic";
    public static final String RIGHT_TAIL_BASIC = "right_tail_basic";
    public static final String LEFT_CLAW_BASIC = "left_claw_basic";
    public static final String EAT = "eat";
    public static final String ACID_SPIT = "acidspit";
    public static final String ATTACK_HEAVY = "attack_heavy";
    public static final String ATTACK = "attack";
    public static final String ATTACK_NORMAL = "attack_normal";

    public static ResourceLocation modResource(String name) {
        return new ResourceLocation(Gigeresque.MOD_ID, name);
    }
}
