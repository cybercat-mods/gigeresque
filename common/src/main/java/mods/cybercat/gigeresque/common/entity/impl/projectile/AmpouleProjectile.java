package mods.cybercat.gigeresque.common.entity.impl.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class AmpouleProjectile extends AbstractArrow {

    public AmpouleProjectile(EntityType<? extends AmpouleProjectile> type, Level level) {
        super(type, level);
    }

    public AmpouleProjectile(EntityType type, Level level, LivingEntity shooter, ItemStack copyWithCount, ItemStack weapon) {
        super(type, shooter, level, copyWithCount, weapon);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }
}
