package mods.cybercat.gigeresque.common.entity.impl.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;

public class GooAmpouleProjectile extends AmpouleProjectile {

    public GooAmpouleProjectile(EntityType<? extends GooAmpouleProjectile> type, Level level) {
        super(type, level);
    }

    public GooAmpouleProjectile(Level level, LivingEntity shooter, ItemStack ammo, ItemStack weapon) {
        super(GigEntities.AMPOULE_PROJECTILE.get(), level, shooter, ammo, weapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        generateVisuals(result.getEntity().blockPosition());
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        generateVisuals(this.blockPosition());
        this.discard();
    }

    private void generateVisuals(BlockPos pos) {
        GigCommonMethods.placePool(GigEntities.GOO.get(), level(), pos);

        var areaEffectCloudEntity = new AreaEffectCloud(this.level(), pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        areaEffectCloudEntity.setRadius(2.0F);
        areaEffectCloudEntity.setDuration(30);
        areaEffectCloudEntity.setRadiusPerTick(
            -areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration()
        );
        areaEffectCloudEntity.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 600, 0));
        this.level().addFreshEntity(areaEffectCloudEntity);
    }
}
