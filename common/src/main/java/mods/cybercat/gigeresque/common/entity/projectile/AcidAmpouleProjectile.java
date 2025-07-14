package mods.cybercat.gigeresque.common.entity.projectile;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class AcidAmpouleProjectile extends AbstractArrow {

	public AcidAmpouleProjectile(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(EntityType.ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
	}

	public AcidAmpouleProjectile(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(EntityType.ARROW, owner, level, pickupItemStack, firedFromWeapon);
	}

    // TODO: Reimplement killing of projectile on hit
    @Override
	protected void onHitEntity(EntityHitResult result) {
        GigCommonMethods.generateAcidPoolAtPos(this.level(), result.getEntity().blockPosition());
        //this.kill();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) { 
        GigCommonMethods.generateAcidPoolAtPos(this.level(), this.blockPosition());
        this.kill();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return null;    
    }
}
