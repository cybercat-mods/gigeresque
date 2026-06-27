package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;

public record GigEntityUtils() {

    public static final EasyRandom RANDOM = new EasyRandom(RandomSource.createThreadSafe());

    public static final Predicate<Entity> PEACEFUL_CHECK = entity -> {
        // Only applies in Peaceful
        if (entity.level().getDifficulty() != Difficulty.PEACEFUL)
            return false;

        // Peaceful mode enabled
        if (CommonMod.config.generalConfigs.peacefulModeIgnorePlayersOnly) {
            // ignore only players in peaceful
            return entity instanceof Player;
        } else {
            // ignore everything in peaceful
            return CommonMod.config.generalConfigs.enablePeacefulModeTargetDisable;
        }
    };

    public static final Predicate<Entity> PEACEFUL_CHECK_ONLY_PLAYERS = entity -> {
        // Feature toggle must be enabled
        if (!CommonMod.config.generalConfigs.peacefulModeIgnorePlayersOnly)
            return false;

        // Not Peaceful, allow normal targeting
        if (entity.level().getDifficulty() != Difficulty.PEACEFUL)
            return false;

        // Ignore only players in peaceful
        return entity instanceof Player;
    };

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

    public static boolean convertToNeo(LivingEntity target) {
        return target.getType().is(GigTags.NEOHOST) && target.hasEffect(GigStatusEffects.SPORE);
    }

    public static boolean faceHuggerTest(LivingEntity target) {
        if (target.getType().is(GigTags.GIG_ALIENS)) {
            return false;
        }

        if (PEACEFUL_CHECK_ONLY_PLAYERS.test(target)) {
            return false;
        }

        if (target instanceof AmbientCreature) {
            return false;
        }

        if (GigEntityUtils.passengerCheck(target)) {
            return false;
        }

        if (target.hasEffect(GigStatusEffects.IMPREGNATION)) {
            return false;
        }

        if (target.hasEffect(GigStatusEffects.EGGMORPHING)) {
            return false;
        }

        if (GigEntityUtils.isFacehuggerAttached(target)) {
            return false;
        }

        if (target.getType().is(GigTags.FACEHUGGER_BLACKLIST)) {
            return false;
        }

        return GigEntityUtils.isTargetHostable(target);
    }

    public static boolean isCommonValidTarget(LivingEntity target) {
        return !(target.getType().is(GigTags.GIG_ALIENS)
            || PEACEFUL_CHECK.test(target)
            || target.getType().is(GigTags.XENO_ATTACK_BLACKLIST)
            || GigEntityUtils.passengerCheck(target)
            || target.hasEffect(GigStatusEffects.IMPREGNATION)
            || target.hasEffect(GigStatusEffects.EGGMORPHING)
            || GigEntityUtils.isFacehuggerAttached(target)
            || !target.isAlive()
            || target.isInvulnerable()
            || !target.attackable()
            || !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)
            || target.getInBlockState().is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
            || target.level().getBlockState(target.blockPosition()).is(GigBlocks.NEST_RESIN_WEB_CROSS.get()));
    }

    public static boolean isValidAquaTarget(LivingEntity target) {
        return isCommonValidTarget(target);
    }

    public static boolean isValidTarget(LivingEntity target) {
        return isCommonValidTarget(target)
            && !(target instanceof WaterAnimal)
            && !(target instanceof Guardian);
    }

    public static boolean removeFaceHuggerTarget(LivingEntity target) {
        if (target.getType().is(GigTags.GIG_ALIENS)) {
            return false;
        }

        if (GigEntityUtils.passengerCheck(target)) {
            return false;
        }

        if (target.hasEffect(GigStatusEffects.IMPREGNATION)) {
            return false;
        }

        if (target.hasEffect(GigStatusEffects.EGGMORPHING)) {
            return false;
        }

        if (GigEntityUtils.isFacehuggerAttached(target)) {
            return false;
        }

        if (target.isBaby()) {
            return false;
        }

        if (target.getType().is(GigTags.FACEHUGGER_BLACKLIST)) {
            return false;
        }

        return GigEntityUtils.isTargetHostable(target) && target.isAlive();
    }

    public static boolean passengerCheck(LivingEntity target) {
        return target.getVehicle() != null && target.getVehicle()
            .getSelfAndPassengers()
            .anyMatch(
                AlienEntity.class::isInstance
            );
    }

    public static void spawnMutant(LivingEntity entity) {
        var randomPhase2 = entity.getRandom().nextInt(0, 2);
        LivingEntity summon;
        if (GigEntityUtils.isTargetSmallMutantHost(entity)) {
            if (randomPhase2 == 1)
                summon = GigEntities.MUTANT_HAMMERPEDE.get().create(entity.level());
            else
                summon = GigEntities.MUTANT_POPPER.get().create(entity.level());
            if (summon != null)
                GigEntityUtils.moveToAndSpawn(entity, summon);
        } else if (GigEntityUtils.isTargetLargeMutantHost(entity)) {
            summon = GigEntities.MUTANT_STALKER.get().create(entity.level());
            if (summon != null)
                GigEntityUtils.moveToAndSpawn(entity, summon);
        }
    }

    public static LivingEntity spawnBurster(LivingEntity entity) {
        var defaultBurster = GigEntities.CHESTBURSTER.get().create(entity.level());
        if (!entity.hasEffect(GigStatusEffects.SPORE) && !entity.hasEffect(GigStatusEffects.DNA)) {
            if (entity.getType().is(GigTags.RUNNER_HOSTS)) {
                var runnerBurster = GigEntities.RUNNERBURSTER.get().create(entity.level());
                if (runnerBurster != null) {
                    runnerBurster.setHostId("runner");
                    return runnerBurster;
                }
            } else if (entity.getType().is(GigTags.AQUATIC_HOSTS))
                return GigEntities.AQUATIC_CHESTBURSTER.get().create(entity.level());
        }
        if (GigEntityUtils.convertToNeo(entity))
            return GigEntities.NEOBURSTER.get().create(entity.level());
        if (entity.hasEffect(GigStatusEffects.DNA))
            return GigEntities.SPITTER.get().create(entity.level());
        if (entity.getType().is(GigTags.HWG_ENTITIES))
            return GigEntities.HELL_BURSTER.get().create(entity.level());
        return defaultBurster;
    }

    private static void moveToAndSpawn(@NotNull LivingEntity entity, LivingEntity summon) {
        if (entity instanceof LivingEntity livingEntity) {
            for (var effect : livingEntity.getActiveEffects()) {
                summon.addEffect(new MobEffectInstance(effect));
            }
        }
        summon.setPos(entity.getX(), entity.getY(), entity.getZ());
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

    public static final BiPredicate<AlienEntity, LivingEntity> TARGET_PREDICATE = (xenomorph, potentialTarget) -> {
        if (xenomorph == null || potentialTarget == null)
            return false;

        if (xenomorph.level() != potentialTarget.level())
            return false;

        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(potentialTarget))
            return false;

        if (potentialTarget.hasEffect(GigStatusEffects.IMPREGNATION))
            return false;

        if (xenomorph.isVehicle())
            return false;

        if (xenomorph.isAlliedTo(potentialTarget))
            return false;

        if (!potentialTarget.getType().is(GigTags.ALL_HOSTS))
            return false;

        if (potentialTarget.getType().is(EntityTypeTags.UNDEAD))
            return false;

        if (potentialTarget.getInBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS)
            return false;

        if (
            potentialTarget.getType() == EntityType.ARMOR_STAND
                || potentialTarget.getType() == EntityType.WARDEN
                || potentialTarget instanceof Bat
        )
            return false;

        if (GigEntityUtils.isFacehuggerAttached(potentialTarget))
            return false;

        if (potentialTarget.isInvulnerable() || potentialTarget.isDeadOrDying())
            return false;

        if (!xenomorph.level().getWorldBorder().isWithinBounds(potentialTarget.getBoundingBox()))
            return false;

        if (
            potentialTarget.getVehicle() != null
                && potentialTarget.getVehicle()
                    .getSelfAndPassengers()
                    .anyMatch(AlienEntity.class::isInstance)
        )
            return false;

        if (potentialTarget.getType().is(GigTags.GIG_ALIENS))
            return false;

        if (xenomorph.isAggressive())
            return false;

        return xenomorph.level()
            .getBlockState(xenomorph.blockPosition().below())
            .isCollisionShapeFullBlock(
                xenomorph.level(),
                xenomorph.blockPosition().below()
            );
    };

    public static void placeInNest(@NotNull ServerLevel level, AlienEntity entity, Entity passenger) {
        var test = GigEntityUtils.getRandomPositionWithinRange(entity.blockPosition(), 3, 1, 3);
        for (BlockPos testPos : BlockPos.betweenClosed(test, test.above(2))) {
            if (
                level.getBlockState(test).isAir() && level.getBlockState(
                    test.below()
                ).isSolid() && level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(test)
                ).stream().noneMatch(Objects::isNull) && passenger != null
            ) {
                passenger.setPos(Vec3.atBottomCenterOf(testPos));
                passenger.removeVehicle();
                passenger.ejectPassengers();
                entity.animationDispatcher.sendLeftClaw();
                level.setBlockAndUpdate(testPos, GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
                level.setBlockAndUpdate(testPos.above(), GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
                entity.ejectPassengers();
                entity.removeVehicle();
                entity.setTarget(null);
                entity.setAggressive(false);
                entity.setIsExecuting(false);
                entity.savedNestWebCross = null;
                entity.setTarget(null);
                entity.setLastHurtByMob(null);
                entity.setLastHurtByPlayer(null);
                if (passenger instanceof Mob mob) {
                    mob.setNoAi(false);
                }
            }
        }
    }

    public static boolean inResinEnoughToBeEggmorphed(@NotNull Entity entity) {
        var stateAtEntityPos = entity.level().getBlockState(entity.blockPosition());
        return stateAtEntityPos.is(GigBlocks.NEST_RESIN_WEB_CROSS.get());
    }

    public static BlockPos getRandomPositionWithinRange(BlockPos centerPos, int xRadius, int yRadius, int zRadius) {
        return RANDOM.getRandomPositionWithinRange(centerPos, xRadius, yRadius, zRadius);
    }

}
