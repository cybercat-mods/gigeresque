package mods.cybercat.gigeresque.common.item;

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

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class SurgeryKitItem extends Item {

    public SurgeryKitItem() {
        super(new Item.Properties().durability(Gigeresque.config.maxSurgeryKitUses));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
        @NotNull ItemStack itemStack,
        @NotNull Player player,
        LivingEntity livingEntity,
        @NotNull InteractionHand interactionHand
    ) {
        if (livingEntity.getPassengers().stream().noneMatch(FacehuggerEntity.class::isInstance)) {
            tryRemoveParasite(itemStack, livingEntity);
            player.getCooldowns().addCooldown(this, Gigeresque.config.surgeryKitCooldownTicks);
            itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(interactionHand));
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player user, @NotNull InteractionHand hand) {
        if (user.getPassengers().stream().noneMatch(FacehuggerEntity.class::isInstance))
            tryRemoveParasite(user.getItemInHand(hand), user);
        return super.use(world, user, hand);
    }

    private void tryRemoveParasite(ItemStack stack, LivingEntity entity) {
        if (
            entity.hasEffect(GigStatusEffects.IMPREGNATION) || entity.hasEffect(
                GigStatusEffects.SPORE
            ) && !entity.level().isClientSide
        ) {
            entity.removeEffect(MobEffects.HUNGER);
            entity.removeEffect(MobEffects.WEAKNESS);
            entity.removeEffect(MobEffects.DIG_SLOWDOWN);
            entity.removeEffect(GigStatusEffects.IMPREGNATION);
            LivingEntity burster = createBurster(entity);
            if (burster != null) {
                setBursterProperties(entity, burster);
                entity.level().addFreshEntity(burster);
                entity.level()
                    .playSound(
                        entity,
                        entity.blockPosition(),
                        GigSounds.CHESTBURSTING,
                        SoundSource.NEUTRAL,
                        2.0f,
                        1.0f
                    );
            }
            if (entity instanceof Player playerentity) {
                playerentity.getCooldowns().addCooldown(this, Gigeresque.config.surgeryKitCooldownTicks);
                stack.hurtAndBreak(1, playerentity, p -> p.broadcastBreakEvent(playerentity.getUsedItemHand()));
            }
            entity.addEffect(new MobEffectInstance(GigStatusEffects.TRAUMA, Constants.TPD));
        }
    }

    private static LivingEntity createBurster(LivingEntity entity) {
        LivingEntity defaultBurster = Entities.CHESTBURSTER.create(entity.level());
        if (!entity.hasEffect(GigStatusEffects.SPORE) && !entity.hasEffect(GigStatusEffects.DNA)) {
            if (entity.getType().is(GigTags.RUNNER_HOSTS)) {
                RunnerbursterEntity runnerBurster = Entities.RUNNERBURSTER.create(entity.level());
                if (runnerBurster != null) {
                    runnerBurster.setHostId("runner");
                    return runnerBurster;
                }
            } else if (entity.getType().is(GigTags.AQUATIC_HOSTS)) {
                return Entities.AQUATIC_CHESTBURSTER.create(entity.level());
            }
        } else if (entity.getType().is(GigTags.NEOHOST) && entity.hasEffect(GigStatusEffects.SPORE)) {
            return Entities.NEOBURSTER.create(entity.level());
        } else if (entity.getType().is(GigTags.CLASSIC_HOSTS) && entity.hasEffect(GigStatusEffects.DNA)) {
            return Entities.SPITTER.create(entity.level());
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
