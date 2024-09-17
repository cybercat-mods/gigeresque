package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
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

public class SurgeryKitItem extends Item {

    public SurgeryKitItem() {
        super(new Properties().durability(CommonMod.config.maxSurgeryKitUses));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack itemStack, @NotNull Player player, LivingEntity livingEntity, @NotNull InteractionHand interactionHand) {
        if (livingEntity.getPassengers().stream().noneMatch(FacehuggerEntity.class::isInstance) && livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)) {
            tryRemoveParasite(itemStack, livingEntity);
            player.getCooldowns().addCooldown(this, CommonMod.config.surgeryKitCooldownTicks);
            itemStack.hurtAndBreak(1, player, livingEntity.getEquipmentSlotForItem(itemStack));
            livingEntity.getActiveEffects().clear();
            if (player instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("surgery_kit"));
                if (advancement != null) {
                    serverPlayer.getAdvancements().award(advancement, "criteria_key");
                }
            }
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player user, @NotNull InteractionHand hand) {
        if (user.getPassengers().stream().noneMatch(FacehuggerEntity.class::isInstance) && user.hasEffect(GigStatusEffects.IMPREGNATION)){
            tryRemoveParasite(user.getItemInHand(hand), user);
            user.getActiveEffects().clear();
            if (user instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("surgery_kit"));
                if (advancement != null) {
                    serverPlayer.getAdvancements().award(advancement, "criteria_key");
                }
            }
        }
        return super.use(world, user, hand);
    }

    private void tryRemoveParasite(ItemStack stack, LivingEntity entity) {
        if (entity.hasEffect(GigStatusEffects.IMPREGNATION) || entity.hasEffect(GigStatusEffects.SPORE) && !entity.level().isClientSide) {
            entity.removeEffect(MobEffects.HUNGER);
            entity.removeEffect(MobEffects.WEAKNESS);
            entity.removeEffect(MobEffects.DIG_SLOWDOWN);
            entity.addEffect(new MobEffectInstance(GigStatusEffects.TRAUMA, 500));
            LivingEntity burster = createBurster(entity);
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

    private static LivingEntity createBurster(LivingEntity entity) {
        LivingEntity defaultBurster = GigEntities.CHESTBURSTER.get().create(entity.level());
        if (!entity.hasEffect(GigStatusEffects.SPORE) && !entity.hasEffect(GigStatusEffects.DNA)) {
            if (entity.getType().is(GigTags.RUNNER_HOSTS)) {
                RunnerbursterEntity runnerBurster = GigEntities.RUNNERBURSTER.get().create(entity.level());
                if (runnerBurster != null) {
                    runnerBurster.setHostId("runner");
                    return runnerBurster;
                }
            } else if (entity.getType().is(GigTags.AQUATIC_HOSTS)) {
                return GigEntities.AQUATIC_CHESTBURSTER.get().create(entity.level());
            }
        } else if (entity.getType().is(GigTags.NEOHOST) && entity.hasEffect(GigStatusEffects.SPORE)) {
            return GigEntities.NEOBURSTER.get().create(entity.level());
        } else if (entity.getType().is(GigTags.CLASSIC_HOSTS) && entity.hasEffect(GigStatusEffects.DNA)) {
            return GigEntities.SPITTER.get().create(entity.level());
        }
        return defaultBurster;
    }

    private static void setBursterProperties(LivingEntity entity, LivingEntity burster) {
        if (entity.hasCustomName()) {
            burster.setCustomName(entity.getCustomName());
        }
        burster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), burster);
        burster.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
    }
}
