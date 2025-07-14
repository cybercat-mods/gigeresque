package mods.cybercat.gigeresque.common.item;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.entity.projectile.AcidAmpouleProjectile;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AcidAmpoule extends ArrowItem {

	// TODO: Make it so the arrow can not be used inside of a normal bow
    public AcidAmpoule(Properties properties) {
        super(properties);
    }

	@Override
	public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
		return new AcidAmpouleProjectile(level, shooter, ammo.copyWithCount(1), weapon);
	}

	@Override
	public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
		AcidAmpouleProjectile projecticle = new AcidAmpouleProjectile(level, pos.x(), pos.y(), pos.z(), null, null);
		return projecticle;
	}
}