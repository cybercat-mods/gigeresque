package mods.cybercat.gigeresque.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class SurgeryKitItem extends Item {

    public SurgeryKitItem() {
        super(new Properties().durability(CommonMod.config.maxSurgeryKitUses));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
        @NotNull ItemStack itemStack,
        @NotNull Player player,
        LivingEntity livingEntity,
        @NotNull InteractionHand interactionHand
    ) {
        if (
            livingEntity.getPassengers().stream().noneMatch(FacehuggerEntity.class::isInstance) && livingEntity.hasEffect(
                GigStatusEffects.IMPREGNATION
            )
        ) {
            // Calculate kill chance based on durability
            var currentDurability = itemStack.getDamageValue();
            var maxDurability = itemStack.getMaxDamage();
            var killChance = calculateKillChance(currentDurability, maxDurability);
            tryRemoveParasite(itemStack, livingEntity);
            player.getCooldowns().addCooldown(this, CommonMod.config.surgeryKitCooldownTicks);
            itemStack.hurtAndBreak(1, player, livingEntity.getEquipmentSlotForItem(itemStack));
            if (player.hasEffect(GigStatusEffects.IMPREGNATION))
                player.removeEffect(GigStatusEffects.IMPREGNATION);
            if (currentDurability < maxDurability && livingEntity.getRandom().nextInt(0, 100) < killChance)
                livingEntity.hurt(GigDamageSources.of(livingEntity.level(), GigDamageSources.FAILED_SURGERY), Float.MAX_VALUE);
            if (player instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("surgery_kit"));
                if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone())
                    for (var s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria())
                        serverPlayer.getAdvancements().award(advancement, s);
            }
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player user, @NotNull InteractionHand hand) {
        if (user.getPassengers().stream().noneMatch(FacehuggerEntity.class::isInstance) && user.hasEffect(GigStatusEffects.IMPREGNATION)) {
            ItemStack itemStack = user.getItemInHand(hand);
            var currentDurability = itemStack.getDamageValue();
            var maxDurability = itemStack.getMaxDamage();
            var killChance = calculateKillChance(currentDurability, maxDurability);
            tryRemoveParasite(user.getItemInHand(hand), user);
            if (user.hasEffect(GigStatusEffects.IMPREGNATION))
                user.removeEffect(GigStatusEffects.IMPREGNATION);
            if (currentDurability < maxDurability && user.getRandom().nextInt(0, 100) < killChance)
                user.hurt(GigDamageSources.of(user.level(), GigDamageSources.FAILED_SURGERY), Float.MAX_VALUE);
            if (user instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("surgery_kit"));
                if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone())
                    for (var s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria())
                        serverPlayer.getAdvancements().award(advancement, s);
            }
        }
        return super.use(world, user, hand);
    }

    private double calculateKillChance(int currentDurability, int maxDurability) {
        int durabilityLost = maxDurability - currentDurability;
        return (durabilityLost / (double) maxDurability) * 0.40; // Scale chance from 0% to 40%
    }

    private void tryRemoveParasite(ItemStack stack, LivingEntity entity) {
        if (entity.hasEffect(GigStatusEffects.IMPREGNATION) || entity.hasEffect(GigStatusEffects.SPORE) && !entity.level().isClientSide) {
            entity.removeEffect(MobEffects.HUNGER);
            entity.removeEffect(MobEffects.WEAKNESS);
            entity.removeEffect(MobEffects.DIG_SLOWDOWN);
            entity.addEffect(new MobEffectInstance(GigStatusEffects.TRAUMA, 300, 0, false, false, true));
            var burster = GigEntityUtils.spawnBurster(entity);
            if (burster != null) {
                setBursterProperties(entity, burster);
                entity.level().addFreshEntity(burster);
                entity.level().playSound(entity, entity.blockPosition(), GigSounds.CHESTBURSTING.get(), SoundSource.NEUTRAL, 2.0f, 1.0f);
            }
            if (entity instanceof Player playerentity) {
                playerentity.getCooldowns().addCooldown(this, CommonMod.config.surgeryKitCooldownTicks);
                stack.hurtAndBreak(1, playerentity, playerentity.getEquipmentSlotForItem(stack));
            }
            entity.removeEffect(GigStatusEffects.IMPREGNATION);
        }
    }

    private static void setBursterProperties(LivingEntity entity, LivingEntity burster) {
        if (entity.hasCustomName())
            burster.setCustomName(entity.getCustomName());
        burster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), burster);
        burster.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
    }
}
