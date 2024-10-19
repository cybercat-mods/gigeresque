package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class EggMorphingStatusEffect extends MobEffect {

    public EggMorphingStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.BLACK.getColor());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributes, int amplifier) {
        if (Constants.isCreativeSpecPlayer.test(entity))
            return;
        if (!GigEntityUtils.isTargetHostable(entity))
            return;
        if (!entity.level().getBlockState(entity.blockPosition()).is(GigBlocks.NEST_RESIN_WEB_CROSS))
            return;
        var egg = new AlienEggEntity(Entities.EGG, entity.level());
        egg.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
        if (entity.level().getBlockState(entity.blockPosition()).is(GigTags.NEST_BLOCKS))
            entity.level().setBlockAndUpdate(entity.blockPosition(), Blocks.AIR.defaultBlockState());
        if (entity.level().getBlockState(entity.blockPosition().above()).is(GigTags.NEST_BLOCKS))
            entity.level().setBlockAndUpdate(entity.blockPosition().above(), Blocks.AIR.defaultBlockState());
        entity.level().addFreshEntity(egg);
        entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.EGGMORPHING), Float.MAX_VALUE);
    }
}
