package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;

public class GigEntityUtils {
	public GigEntityUtils() {
	}

	public static boolean isPotentialHost(Entity entity) {
		if (entity == null)
			return false;
		if (!(entity instanceof LivingEntity))
			return false;
		if (entity instanceof AlienEntity)
			return false;

		var vehicleCondition = (entity.getVehicle() == null) || !(entity.getVehicle() instanceof AlienEntity);

		var facehuggerblacklisted = entity.getType().is(GigTags.FACEHUGGER_BLACKLIST);

		if (entity instanceof Player playerEntity)
			if (playerEntity.isCreative() || playerEntity.isSpectator())
				return false;
			else
				return true;

		return entity.isAlive() && !facehuggerblacklisted && entity.getPassengers().isEmpty() && ((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing() && ((LivingEntity) entity).getMobType() != MobType.UNDEAD && vehicleCondition;
	}

	public static boolean isEggmorphable(Entity entity) {
		if (entity == null)
			return false;
		if (!(entity instanceof LivingEntity))
			return false;
		if (entity instanceof AlienEntity)
			return false;

		var playerCondition = !(entity instanceof Player) || !((Player) entity).isCreative() && !entity.isSpectator();

		var hostable = entity.getType().is(GigTags.CLASSIC_HOSTS);

		var weakCondition = (((LivingEntity) entity).getHealth() / ((LivingEntity) entity).getMaxHealth() < 0.25f) || ((LivingEntity) entity).getHealth() <= 4f;

		var threatCondition = ((LivingEntity) entity).getUseItem().getItem() instanceof SwordItem || ((LivingEntity) entity).getUseItem().getItem() instanceof ProjectileWeaponItem;

		return entity.isAlive() && hostable && ((Host) entity).doesNotHaveParasite() && ((Eggmorphable) entity).isNotEggmorphing() && ((LivingEntity) entity).getMobType() != MobType.UNDEAD && playerCondition && weakCondition && !threatCondition;
	}

	public static boolean isFacehuggerAttached(Entity entity) {
		return entity != null && entity.getPassengers().stream().anyMatch(it -> it instanceof FacehuggerEntity);
	}

	public static boolean isTargetHostable(Entity target) {
		return target.getType().is(GigTags.CLASSIC_HOSTS) || target.getType().is(GigTags.AQUATIC_HOSTS) || target.getType().is(GigTags.RUNNER_HOSTS);
	}

	public static boolean isTargetSmallMutantHost(Entity target) {
		return target.getType().is(GigTags.MUTANT_SMALL_HOSTS);
	}

	public static boolean isTargetLargeMutantHost(Entity target) {
		return target.getType().is(GigTags.MUTANT_LARGE_HOSTS);
	}

	public static boolean isTargetDNAImmune(Entity target) {
		return target.getType().is(GigTags.DNAIMMUNE);
	}

	public static boolean convertToSpitter(LivingEntity target) {
		return target.hasEffect(GigStatusEffects.DNA) && (((Host) target).hasParasite() && ((Host) target).getTicksUntilImpregnation() > Gigeresque.config.getImpregnationTickTimer() / 2);
	}
}
