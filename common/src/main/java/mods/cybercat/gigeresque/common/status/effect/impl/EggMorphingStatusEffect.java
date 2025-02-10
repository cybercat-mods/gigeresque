package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class EggMorphingStatusEffect extends MobEffect {

    public EggMorphingStatusEffect() {
        super(MobEffectCategory.NEUTRAL, Color.BLACK.getColor());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (GigEntityUtils.isTargetHostable(entity) && this == GigStatusEffects.EGGMORPHING)
            entity.heal(0);
        return super.applyEffectTick(entity, amplifier);
    }

    public static void effectRemoval(@NotNull LivingEntity entity, MobEffectInstance mobEffectInstance) {
        if (Constants.isCreativeSpecPlayer.test(entity))
            return;
        if (!GigEntityUtils.isTargetHostable(entity))
            return;
        if (entity.level().isClientSide || !(mobEffectInstance.getEffect().value() instanceof EggMorphingStatusEffect))
            return;
        if (!entity.level().getBlockState(entity.blockPosition()).is(GigBlocks.NEST_RESIN_WEB_CROSS.get()))
            return;
        var egg = new AlienEggEntity(GigEntities.EGG.get(), entity.level());
        egg.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
        if (entity.level().getBlockState(entity.blockPosition()).is(GigTags.NEST_BLOCKS))
            entity.level().setBlockAndUpdate(entity.blockPosition(), Blocks.AIR.defaultBlockState());
        if (entity.level().getBlockState(entity.blockPosition().above()).is(GigTags.NEST_BLOCKS))
            entity.level().setBlockAndUpdate(entity.blockPosition().above(), Blocks.AIR.defaultBlockState());
        entity.level().addFreshEntity(egg);
        entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.EGGMORPHING), Integer.MAX_VALUE);
    }
}
