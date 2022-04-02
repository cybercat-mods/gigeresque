package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;

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

		boolean vehicleCondition = (entity.getVehicle() == null) || !(entity.getVehicle() instanceof AlienEntity);

		if ((entity instanceof PlayerEntity)
				&& (((PlayerEntity) entity).isCreative() || ((PlayerEntity) entity).isSpectator()))
			return false;

		if ((entity instanceof PlayerEntity)
				&& !(((PlayerEntity) entity).isCreative() || ((PlayerEntity) entity).isSpectator()))
			return true;

		return entity.isAlive() && entity instanceof LivingEntity && entity.getPassengerList().isEmpty()
				&& ((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing()
				&& ((LivingEntity) entity).getGroup() != EntityGroup.UNDEAD && vehicleCondition;
	}

	public static boolean isEggmorphable(Entity entity) {
		if (entity == null)
			return false;
		boolean playerCondition = !(entity instanceof PlayerEntity)
				|| !((PlayerEntity) entity).isCreative() && !entity.isSpectator();

		if (ConfigAccessor.isTargetBlacklisted(ClassicAlienEntity.class, entity))
			return false;
		if (ConfigAccessor.isTargetWhitelisted(ClassicAlienEntity.class, entity))
			return true;

		boolean weakCondition = !(entity instanceof LivingEntity)
				|| (((LivingEntity) entity).getHealth() / ((LivingEntity) entity).getMaxHealth() < 0.25f)
				|| ((LivingEntity) entity).getHealth() <= 4f;

		boolean threatCondition = !(entity instanceof LivingEntity)
				|| ((LivingEntity) entity).getActiveItem().getItem() instanceof SwordItem
				|| ((LivingEntity) entity).getActiveItem().getItem() instanceof RangedWeaponItem;

		return entity.isAlive() && entity instanceof LivingEntity && !(entity instanceof AlienEntity)
				&& ((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing()
				&& ((LivingEntity) entity).getGroup() != EntityGroup.UNDEAD && playerCondition && weakCondition
				&& !threatCondition;
	}

	public static boolean isFacehuggerAttached(Entity entity) {
		return entity != null && entity.getPassengerList().stream().anyMatch(it -> it instanceof FacehuggerEntity);
	}
}
