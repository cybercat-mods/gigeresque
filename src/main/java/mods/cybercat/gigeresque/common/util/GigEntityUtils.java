package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;

public record GigEntityUtils() {

    public static boolean isFacehuggerAttached(Entity entity) {
        return (entity != null && entity.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance));
    }

    public static boolean isTargetHostable(Entity target) {
        return target.getType().is(GigTags.CLASSIC_HOSTS) || target.getType()
            .is(
                GigTags.AQUATIC_HOSTS
            ) || target.getType().is(GigTags.RUNNER_HOSTS);
    }

    public static boolean isTargetGooable(Entity target) {
        return isTargetSmallMutantHost(target) || isTargetLargeMutantHost(target);
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
        return target.hasEffect(GigStatusEffects.DNA) && target.hasEffect(GigStatusEffects.IMPREGNATION);
    }

    public static boolean faceHuggerTest(LivingEntity target) {
        return !(target.getType().is(GigTags.GIG_ALIENS) || target instanceof AmbientCreature) && !target.getType()
            .is(
                GigTags.FACEHUGGER_BLACKLIST
            ) && !target.hasEffect(GigStatusEffects.IMPREGNATION) && !target.hasEffect(
                GigStatusEffects.EGGMORPHING
            ) && target.getMobType() != MobType.UNDEAD && !GigEntityUtils.passengerCheck(
                target
            ) && !GigEntityUtils.removeFaceHuggerTarget(target) && GigEntityUtils.isTargetHostable(target);
    }

    public static boolean entityTest(LivingEntity target, AlienEntity self) {
        return !((target.getType().is(GigTags.GIG_ALIENS) || target.getType()
            .is(
                GigTags.XENO_ATTACK_BLACKLIST
            )) || !target.hasLineOfSight(target) || GigEntityUtils.mainCheck(
                target
            ) || self.isVehicle() && target.isAlive());
    }

    public static boolean removeTarget(LivingEntity target) {
        return (((target.getType().is(GigTags.GIG_ALIENS) || target.getType()
            .is(
                GigTags.XENO_ATTACK_BLACKLIST
            )) || GigEntityUtils.passengerCheck(target) || GigEntityUtils.hostEggCheck(
                target
            ) || GigEntityUtils.isFacehuggerAttached(target) || GigEntityUtils.feetCheck(
                target
            ) && !target.isAlive()) || target.hasEffect(GigStatusEffects.IMPREGNATION));
    }

    public static boolean removeFaceHuggerTarget(LivingEntity target) {
        return ((target.getType().is(GigTags.GIG_ALIENS) || target.getType()
            .is(
                GigTags.SMALL_XENO_ATTACK_BLACKLIST
            )) || GigEntityUtils.mainCheck(target) || GigEntityUtils.mainCheck2(
                target
            ) || !GigEntityUtils.isTargetHostable(target) && !target.isAlive());
    }

    public static boolean mainCheck(LivingEntity target) {
        return GigEntityUtils.passengerCheck(target) || GigEntityUtils.feetCheck(target);
    }

    public static boolean mainCheck2(LivingEntity target) {
        return GigEntityUtils.hostEggCheck(target) || GigEntityUtils.isFacehuggerAttached(target);
    }

    public static boolean passengerCheck(LivingEntity target) {
        return target.getVehicle() != null && target.getVehicle()
            .getSelfAndPassengers()
            .anyMatch(
                AlienEntity.class::isInstance
            );
    }

    public static boolean feetCheck(LivingEntity target) {
        return target.getFeetBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS;
    }

    public static boolean hostEggCheck(LivingEntity target) {
        return target.hasEffect(GigStatusEffects.IMPREGNATION) || target.hasEffect(GigStatusEffects.EGGMORPHING);
    }

    public static void spawnMutant(LivingEntity entity) {
        var randomPhase2 = entity.getRandom().nextInt(0, 2);
        if (GigEntityUtils.isTargetSmallMutantHost(entity)) {
            Entity summon;
            if (randomPhase2 == 1) {
                summon = Entities.MUTANT_HAMMERPEDE.create(entity.level());
            } else {
                summon = Entities.MUTANT_POPPER.create(entity.level());
            }
            assert summon != null;
            GigEntityUtils.moveToAndSpawn(entity, summon);
        } else if (GigEntityUtils.isTargetLargeMutantHost(entity)) {
            Entity summon = Entities.MUTANT_STALKER.create(entity.level());
            assert summon != null;
            GigEntityUtils.moveToAndSpawn(entity, summon);
        }
        entity.hurt(GigDamageSources.of(entity.level(), GigDamageSources.DNA), Integer.MAX_VALUE);
    }

    private static void moveToAndSpawn(@NotNull LivingEntity entity, Entity summon) {
        summon.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
        spawnEffects(entity.level(), entity);
        entity.level().addFreshEntity(summon);
    }

    private static void spawnEffects(Level world, LivingEntity entity) {
        if (!world.isClientSide())
            for (var i = 0; i < 2; i++)
                ((ServerLevel) world).sendParticles(
                    ParticleTypes.POOF,
                    entity.getX() + 0.5,
                    entity.getY(),
                    entity.getZ() + 0.5,
                    1,
                    entity.getRandom().nextGaussian() * 0.02,
                    entity.getRandom().nextGaussian() * 0.02,
                    entity.getRandom().nextGaussian() * 0.02,
                    0.15000000596046448
                );
    }
}
