package mods.cybercat.gigeresque.common.entity.impl.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;

public class AcidAmpouleProjectile extends AmpouleProjectile {

    public AcidAmpouleProjectile(EntityType<? extends AcidAmpouleProjectile> type, Level level) {
        super(type, level);
    }

    public AcidAmpouleProjectile(Level level, LivingEntity shooter, ItemStack ammo, ItemStack weapon) {
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
        GigCommonMethods.placePool(GigEntities.ACID.get(), level(), pos);
    }
}
