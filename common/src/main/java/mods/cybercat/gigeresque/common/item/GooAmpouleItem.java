package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.common.entity.impl.projectile.GooAmpouleProjectile;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GooAmpouleItem extends ArrowItem {
  public GooAmpouleItem(Properties properties) {
    super(properties);
  }

  @Override
  public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
    return new GooAmpouleProjectile(level, shooter, ammo.copyWithCount(1), weapon);
  }

  @Override
  public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
    return null;
  }
}
