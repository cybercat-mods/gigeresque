package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;

public class EntityUtils {
	private EntityUtils() {
	}

	public static boolean isPotentialHost(Entity entity) {
		if (entity == null)
			return false;

		if (ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, entity))
			return false;
		if (ConfigAccessor.isTargetWhitelisted(FacehuggerEntity.class, entity))
			return true;

		var vehicleCondition = (entity.getVehicle() == null) || !(entity.getVehicle() instanceof AlienEntity);

		if ((entity instanceof Player) && (((Player) entity).isCreative() || ((Player) entity).isSpectator()))
			return false;

		if ((entity instanceof Player) && !(((Player) entity).isCreative() || ((Player) entity).isSpectator()))
			return true;

		return entity.isAlive() && entity instanceof LivingEntity && entity.getPassengers().isEmpty() && ((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing() && ((LivingEntity) entity).getMobType() != MobType.UNDEAD && vehicleCondition;
	}

	public static boolean isEggmorphable(Entity entity) {
		if (entity == null)
			return false;
		var playerCondition = !(entity instanceof Player) || !((Player) entity).isCreative() && !entity.isSpectator();

		if (ConfigAccessor.isTargetBlacklisted(ClassicAlienEntity.class, entity))
			return false;
		if (ConfigAccessor.isTargetWhitelisted(ClassicAlienEntity.class, entity))
			return true;

		var weakCondition = !(entity instanceof LivingEntity) || (((LivingEntity) entity).getHealth() / ((LivingEntity) entity).getMaxHealth() < 0.25f) || ((LivingEntity) entity).getHealth() <= 4f;

		var threatCondition = !(entity instanceof LivingEntity) || ((LivingEntity) entity).getUseItem().getItem() instanceof SwordItem || ((LivingEntity) entity).getUseItem().getItem() instanceof ProjectileWeaponItem;

		return entity.isAlive() && entity instanceof LivingEntity && !(entity instanceof AlienEntity) && ((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing() && ((LivingEntity) entity).getMobType() != MobType.UNDEAD && playerCondition && weakCondition && !threatCondition;
	}

	public static boolean isFacehuggerAttached(Entity entity) {
		return entity != null && entity.getPassengers().stream().anyMatch(it -> it instanceof FacehuggerEntity);
	}
}
