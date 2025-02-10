package mods.cybercat.gigeresque;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public record Constants() {

    public static int particleCount = 0;

    public static final int TPS = 20; // Ticks per second

    public static final int TPM = TPS * 60; // Ticks per minute

    public static final int TPD = TPM * 20; // Ticks per day

    public static Predicate<Entity> notPlayer = entity -> !(entity instanceof Player);

    public static Predicate<Entity> isCreeper = Creeper.class::isInstance;

    public static Predicate<Entity> isNotCreativeSpecPlayer = entity -> (entity instanceof Player playerEntity && !(playerEntity
        .isCreative() || playerEntity.isSpectator()));

    public static Predicate<Entity> isCreativeSpecPlayer = entity -> (entity instanceof Player playerEntity && (playerEntity.isCreative()
        || playerEntity.isSpectator()));

    public static Predicate<Entity> hasEggEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(
        livingEntity
    ) && livingEntity.hasEffect(GigStatusEffects.EGGMORPHING);

    public static Predicate<Entity> hasCureEffects = entity -> entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(
        GigStatusEffects.DNA
    )
        && livingEntity.hasEffect(MobEffects.ABSORPTION) && livingEntity.hasEffect(MobEffects.WITHER) && livingEntity.hasEffect(
            MobEffects.REGENERATION
        );

    public static Predicate<Entity> hasDNAEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetGooable(
        livingEntity
    ) && livingEntity.hasEffect(GigStatusEffects.DNA);

    public static Predicate<Entity> hasImpEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(
        livingEntity
    ) && livingEntity.hasEffect(GigStatusEffects.IMPREGNATION);

    public static Predicate<Entity> hasSporeEffect = entity -> entity instanceof LivingEntity livingEntity && GigEntityUtils.convertToNeo(
        livingEntity
    ) && livingEntity.hasEffect(GigStatusEffects.SPORE);

    public static Predicate<Entity> shouldApplyImpEffects = entity -> entity instanceof LivingEntity livingEntity && hasImpEffect.test(
        livingEntity
    ) && (livingEntity.getEffect(
        GigStatusEffects.IMPREGNATION
    ).getDuration() < (0.2 * CommonMod.config.getImpregnationTickTimer()) && (livingEntity.tickCount % Constants.TPS == 0L));

    public static final String ATTACK_CONTROLLER = "attack_controller";

    public static final String LIVING_CONTROLLER = "livingController";

    public static final String BASE_CONTROLLER = "base_controller";

    public static final String HOSTILE_CONTROLLER = "hostile_controller";

    public static final String HISS_CONTROLLER = "hiss_controller";

    public static <T> T self(Object object) {
        return (T) object;
    }

    public static ResourceLocation modResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID, name);
    }
}
