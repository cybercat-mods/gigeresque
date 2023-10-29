package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;

public record GigEntityUtils() {

    public static boolean isFacehuggerAttached(Entity entity) {
        return entity != null && entity.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance);
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

    public static void spawnMutant(LivingEntity entity) {
        var randomPhase = entity.getRandom().nextInt(0, 50);
        var randomPhase2 = entity.getRandom().nextInt(0, 2);
        Entity summon;
        if (GigEntityUtils.isTargetSmallMutantHost(entity)) {
            if (randomPhase2 == 1)
                summon = Entities.MUTANT_HAMMERPEDE.create(entity.level());
            else
                summon = Entities.MUTANT_POPPER.create(entity.level());
            summon.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
            spawnEffects(entity.level(), entity);
            entity.level().addFreshEntity(summon);
        } else if (GigEntityUtils.isTargetLargeMutantHost(entity)) {
            summon = Entities.MUTANT_STALKER.create(entity.level());
            summon.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
            spawnEffects(entity.level(), entity);
            entity.level().addFreshEntity(summon);
        }
        entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.DNA), Integer.MAX_VALUE);
        return;
    }

    private static void spawnEffects(Level world, LivingEntity entity) {
        if (!world.isClientSide())
            for (var i = 0; i < 2; i++)
                ((ServerLevel) world).sendParticles(ParticleTypes.POOF, entity.getX() + 0.5, entity.getY(), entity.getZ() + 0.5, 1, entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02, 0.15000000596046448);
    }
}
