package mods.cybercat.gigeresque;

import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public record Constants() {

    public static final int TPS = 20; // Ticks per second
    public static final int TPM = TPS * 60; // Ticks per minute
    public static final int TPD = TPM * 20; // Ticks per day

    public static Predicate<Entity> notPlayer = entity -> !(entity instanceof Player);
    public static Predicate<Entity> isCreeper = Creeper.class::isInstance;
    public static Predicate<Entity> isNotCreativeSpecPlayer = entity -> (entity instanceof Player playerEntity && !(playerEntity.isCreative() || playerEntity.isSpectator()));
    public static Predicate<Entity> isCreativeSpecPlayer = entity -> (entity instanceof Player playerEntity && (playerEntity.isCreative() || playerEntity.isSpectator()));
    public static Predicate<Entity> hasEggEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(
            livingEntity) && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING);
    public static Predicate<Entity> hasDNAEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetGooable(
            livingEntity) && livingEntity.hasEffect(GigStatusEffects.DNA);
    public static Predicate<Entity> hasImpEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(
            livingEntity) && livingEntity.hasEffect(GigStatusEffects.IMPREGNATION);
    public static Predicate<Entity> shouldApplyImpEffects = entity -> entity instanceof LivingEntity livingEntity && hasImpEffect.test(
            livingEntity) && (livingEntity.getEffect(
            GigStatusEffects.IMPREGNATION).getDuration() < (0.2 * CommonMod.config.getImpregnationTickTimer()) && (livingEntity.tickCount % Constants.TPS == 0L));

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

    public static <T> T self(Object object) {
        return (T) object;
    }

    public static ResourceLocation modResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID, name);
    }

    public static BlockPos blockPos(double pX, double pY, double pZ) {
        return new BlockPos(Mth.floor(pX), Mth.floor(pY), Mth.floor(pZ));
    }

    public static BlockPos blockPos(Vec3 pVec3) {
        return blockPos(pVec3.x, pVec3.y, pVec3.z);
    }
}
