package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class DNAStatusEffect extends MobEffect {

    private BlockPos lightBlockPos = null;

    public DNAStatusEffect() {
        super(MobEffectCategory.HARMFUL, Color.DARK_GRAY.getColor());
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
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (!entity.getType().is(GigTags.DNAIMMUNE) && this == GigStatusEffects.DNA)
            entity.heal(0);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, @NotNull AttributeMap attributes, int amplifier) {
        var randomPhase = entity.getRandom().nextInt(0, 50);
        if (Constants.isCreeper.test(entity))
            return;
        if (!entity.getType().is(GigTags.DNAIMMUNE)) {
            if (randomPhase > 25) {
                if (
                    (Constants.notPlayer.test(entity) && !(GigEntityUtils.isTargetDNAImmune(
                        entity
                    ))) || Constants.isNotCreativeSpecPlayer.test(entity)
                ) {
                    GigEntityUtils.spawnMutant(entity);
                }
            } else {
                if (
                    (Constants.notPlayer.test(entity) && !(GigEntityUtils.isTargetDNAImmune(
                        entity
                    ))) || Constants.isNotCreativeSpecPlayer.test(entity)
                ) {
                    placeGoo(entity);
                }
            }
        }
        super.removeAttributeModifiers(entity, attributes, amplifier);
    }

    private void placeGoo(LivingEntity entity) {
        entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.DNA), Integer.MAX_VALUE);
        spawnGoo(entity);
    }

    private void spawnGoo(LivingEntity entity) {
        if (lightBlockPos == null) {
            lightBlockPos = findFreeSpace(entity.level(), entity.blockPosition());
            if (lightBlockPos == null)
                return;
            var areaEffectCloudEntity = new AreaEffectCloud(
                entity.level(),
                entity.getX(),
                entity.getY(),
                entity.getZ()
            );
            areaEffectCloudEntity.setRadius(2.0F);
            areaEffectCloudEntity.setDuration(300);
            areaEffectCloudEntity.setRadiusPerTick(0);
            areaEffectCloudEntity.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 600, 0));
            entity.level().addFreshEntity(areaEffectCloudEntity);
        } else
            lightBlockPos = null;
    }

    private BlockPos findFreeSpace(Level world, BlockPos blockPos) {
        if (blockPos == null)
            return null;

        var offsets = new int[2 + 1];
        for (var i = 2; i <= 2; i += 2) {
            offsets[i - 1] = i / 2;
            offsets[i] = -i / 2;
        }
        for (var x : offsets)
            for (var y : offsets)
                for (var z : offsets) {
                    var offsetPos = blockPos.offset(x, y, z);
                    var state = world.getBlockState(offsetPos);
                    if (state.isAir() || state.getBlock().equals(GigBlocks.NEST_RESIN_WEB_CROSS))
                        return offsetPos;
                }

        return null;
    }

}
