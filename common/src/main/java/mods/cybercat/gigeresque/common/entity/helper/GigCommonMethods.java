package mods.cybercat.gigeresque.common.entity.helper;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;

public record GigCommonMethods() {

    public static void generateAcidPool(LivingEntity entity, BlockPos pos, int xOffset, int zOffset) {
        var acidEntity = GigEntities.ACID.get().create(entity.level());
        assert acidEntity != null;
        acidEntity.moveTo(pos.offset(xOffset, 0, zOffset), entity.getYRot(), entity.getXRot());
        entity.level().addFreshEntity(acidEntity);
    }

    public static void generateGooBlood(LivingEntity entity, BlockPos pos, int xOffset, int zOffset) {
        var acidEntity = GigEntities.GOO.get().create(entity.level());
        assert acidEntity != null;
        acidEntity.moveTo(pos.offset(xOffset, 0, zOffset), entity.getYRot(), entity.getXRot());
        entity.level().addFreshEntity(acidEntity);
    }

    public static void generateSporeCloud(LivingEntity entity, BlockPos pos, int xOffset, int zOffset) {
        var areaEffectCloudEntity = new AreaEffectCloud(entity.level(), pos.getX(), pos.getY() + 0.5,
                pos.getZ());
        areaEffectCloudEntity.setRadius(3.0F);
        areaEffectCloudEntity.setDuration(150);
        areaEffectCloudEntity.setRadiusPerTick(
                -areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.setParticle(ParticleTypes.ASH);
        if (!entity.hasEffect(GigStatusEffects.SPORE)) {
            areaEffectCloudEntity.addEffect(
                    new MobEffectInstance(GigStatusEffects.SPORE, CommonMod.config.sporeTickTimer, 0));
        }
        entity.level().addFreshEntity(areaEffectCloudEntity);
    }
}
