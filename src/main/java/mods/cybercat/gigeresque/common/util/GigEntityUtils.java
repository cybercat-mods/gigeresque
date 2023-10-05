package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
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
		return !(target instanceof AlienEntity || target instanceof AmbientCreature) && !target.getType().is(GigTags.FACEHUGGER_BLACKLIST) && ((Host) target).doesNotHaveParasite() && ((Eggmorphable) target).isNotEggmorphing() && target.getMobType() != MobType.UNDEAD && !GigEntityUtils.passengerCheck(target) && !GigEntityUtils.removeFaceHuggerTarget(target, self) && GigEntityUtils.isTargetHostable(target);
	}

	public static boolean entityTest(LivingEntity target, AlienEntity self) {
		return !((target instanceof AlienEntity || target.getType().is(GigTags.XENO_ATTACK_BLACKLIST)) || !target.hasLineOfSight(target) || GigEntityUtils.mainCheck(target) || self.isVehicle() && target.isAlive());
	}

	public static boolean removeTarget(LivingEntity target, AlienEntity self) {
		return ((target instanceof AlienEntity || target.getType().is(GigTags.XENO_ATTACK_BLACKLIST)) || GigEntityUtils.mainCheck(target) && !target.isAlive());
	}

	public static boolean removeFaceHuggerTarget(LivingEntity target, AlienEntity self) {
		return ((target instanceof AlienEntity || target.getType().is(GigTags.SMALL_XENO_ATTACK_BLACKLIST)) || GigEntityUtils.mainCheck(target) || !GigEntityUtils.isTargetHostable(target) && !target.isAlive());
	}

	public static boolean mainCheck(LivingEntity target) {
		return GigEntityUtils.passengerCheck(target) || GigEntityUtils.hostEggcheck(target) || GigEntityUtils.isFacehuggerAttached(target) || GigEntityUtils.feetcheck(target);
	}

	public static boolean passengerCheck(LivingEntity target) {
		return target.getVehicle() != null && target.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance);
	}

	public static boolean feetcheck(LivingEntity target) {
		return target.getFeetBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS;
	}

	public static boolean hostEggcheck(LivingEntity target) {
		return ((Host) target).isBleeding() || ((Host) target).hasParasite() || ((Eggmorphable) target).isEggmorphing();
	}
}
