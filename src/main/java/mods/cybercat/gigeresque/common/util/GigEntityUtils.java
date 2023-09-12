package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.warden.Warden;

public record GigEntityUtils() {

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

	public static boolean faceHuggerTest(LivingEntity target, AlienEntity self) {
		return target instanceof LivingEntity && !(target instanceof AlienEntity || target instanceof Slime || target instanceof Strider) && !target.getType().is(GigTags.FACEHUGGER_BLACKLIST) && ((Host) target).doesNotHaveParasite() && ((Eggmorphable) target).isNotEggmorphing() && !(target instanceof AmbientCreature) && ((LivingEntity) target).getMobType() != MobType.UNDEAD && !(target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance))
				&& !GigEntityUtils.removeFaceHuggerTarget(target, self);
	}

	public static boolean entityTest(LivingEntity target, AlienEntity self) {
		return !((target instanceof AlienEntity || target instanceof Warden || target instanceof Slime || target instanceof ArmorStand || target instanceof Bat) || !target.hasLineOfSight(target) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) || ((Host) target).isBleeding() || ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing()
				|| self.isVehicle() || (GigEntityUtils.isFacehuggerAttached(target)) && target.isAlive());
	}

	public static boolean removeTarget(LivingEntity target, AlienEntity self) {
		return ((target instanceof AlienEntity || target instanceof Warden || target instanceof Slime || target instanceof ArmorStand || target instanceof Bat) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || ((Host) target).isBleeding() || ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing() || (GigEntityUtils.isFacehuggerAttached(target))
				|| (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) && !target.isAlive());
	}

	public static boolean removeFaceHuggerTarget(LivingEntity target, AlienEntity self) {
		return ((target instanceof AlienEntity || target instanceof Warden || target instanceof Slime || target instanceof ArmorStand || target instanceof Bat || target instanceof IronGolem) || (target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || ((Host) target).isBleeding() || ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing() || !GigEntityUtils.isTargetHostable(target)
				|| (GigEntityUtils.isFacehuggerAttached(target)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) && !target.isAlive());
	}
}
