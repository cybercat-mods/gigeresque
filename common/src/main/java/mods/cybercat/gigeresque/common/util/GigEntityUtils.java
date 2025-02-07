package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
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

    public static boolean convertToNeo(LivingEntity target) {
        return target.getType().is(GigTags.NEOHOST) && target.hasEffect(GigStatusEffects.SPORE);
    }

    public static boolean faceHuggerTest(LivingEntity target) {
        return !(target.getType().is(GigTags.GIG_ALIENS) || target instanceof AmbientCreature) && !target.getType()
            .is(
                GigTags.FACEHUGGER_BLACKLIST
            ) && !target.hasEffect(GigStatusEffects.IMPREGNATION) && !target.hasEffect(
                GigStatusEffects.EGGMORPHING
            ) && !GigEntityUtils.passengerCheck(
                target
            ) && !GigEntityUtils.removeFaceHuggerTarget(target) && GigEntityUtils.isTargetHostable(target);
    }

    public static boolean entityTest(LivingEntity target, LivingEntity self) {
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
            ) || !GigEntityUtils.isTargetHostable(target) || !target.isAlive());
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
        return target.getInBlockState().getBlock() == GigBlocks.NEST_RESIN_WEB_CROSS;
    }

    public static boolean hostEggCheck(LivingEntity target) {
        return target.hasEffect(GigStatusEffects.IMPREGNATION) || target.hasEffect(GigStatusEffects.EGGMORPHING);
    }

    public static void spawnMutant(LivingEntity entity) {
        var randomPhase2 = entity.getRandom().nextInt(0, 2);
        Entity summon;
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
        } else if (GigEntityUtils.convertToNeo(entity))
            return GigEntities.NEOBURSTER.get().create(entity.level());
        else if (GigEntityUtils.convertToSpitter(entity))
            return GigEntities.SPITTER.get().create(entity.level());
        else if (entity.getType().is(GigTags.HWG_ENTITIES))
            return GigEntities.HELL_BURSTER.get().create(entity.level());
        return defaultBurster;
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

    public static void breakBlocks(AlienEntity alienEntity) {
        if (alienEntity.crawlingManager.isCrawling())
            return;
        if (alienEntity.isDeadOrDying())
            return;
        if (alienEntity.stasisManager.isStasis())
            return;
        if (alienEntity.isInWater())
            return;
        if (alienEntity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            if (!alienEntity.level().isClientSide)
                alienEntity.breakingCounter++;
            if (alienEntity.breakingCounter > 10) {
                for (
                    var testPos : BlockPos.betweenClosed(
                        alienEntity.blockPosition().relative(alienEntity.getDirection()),
                        alienEntity.blockPosition().relative(alienEntity.getDirection()).above(2)
                    )
                ) {
                    var state = alienEntity.level().getBlockState(testPos);
                    if (state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(BlockTags.FLOWERS))
                        continue;
                    if (state.is(GigTags.WEAK_BLOCKS) && !state.isAir()) {
                        if (!alienEntity.isVehicle())
                            alienEntity.animationDispatcher.sendLeftClaw();
                        if (alienEntity.isVehicle())
                            alienEntity.animationDispatcher.sendLeftTailBasic();
                        if (!alienEntity.level().isClientSide)
                            alienEntity.level().destroyBlock(testPos, true, null, 512);
                        alienEntity.breakingCounter = -90;
                        if (alienEntity.level().isClientSide()) {
                            for (var i = 2; i < 10; i++) {
                                alienEntity.level()
                                    .addAlwaysVisibleParticle(
                                        GigParticles.ACID.get(),
                                        alienEntity.getX() + ((alienEntity.getRandom().nextDouble() / 2.0) - 0.5) * (alienEntity.getRandom()
                                            .nextBoolean() ? -1 : 1),
                                        alienEntity.getEyeY() - ((alienEntity.getEyeY() - alienEntity.blockPosition().getY()) / 2.0),
                                        alienEntity.getZ() + ((alienEntity.getRandom().nextDouble() / 2.0) - 0.5) * (alienEntity.getRandom()
                                            .nextBoolean() ? -1 : 1),
                                        0.0,
                                        -0.15,
                                        0.0
                                    );
                            }
                            alienEntity.level()
                                .playLocalSound(
                                    testPos.getX(),
                                    testPos.getY(),
                                    testPos.getZ(),
                                    SoundEvents.LAVA_EXTINGUISH,
                                    SoundSource.BLOCKS,
                                    0.2f + alienEntity.getRandom().nextFloat() * 0.2f,
                                    0.9f + alienEntity.getRandom().nextFloat() * 0.15f,
                                    false
                                );
                        }
                    } else if (
                        !state.is(GigTags.ACID_RESISTANT) && !state.isAir() && (alienEntity.getHealth() >= (alienEntity.getMaxHealth()
                            * 0.50))
                    ) {
                        if (!alienEntity.level().isClientSide) {
                            var acid = GigEntities.ACID.get().create(alienEntity.level());
                            if (acid != null) {
                                acid.setPos(testPos.above().getX(), testPos.above().getY(), testPos.above().getZ());
                                alienEntity.level().addFreshEntity(acid);
                            }
                        }
                        alienEntity.hurt(GigDamageSources.of(alienEntity.level(), GigDamageSources.ACID), 5);
                        alienEntity.breakingCounter = -90;
                    }
                }
            }
            if (alienEntity.breakingCounter >= 25)
                alienEntity.breakingCounter = 0;
        }
    }

    public static void handleLivingEntityInteractions(AlienEntity self, Entity target, LivingEntity livingEntity) {
        if (target instanceof Player playerEntity) {
            handlePlayerInteraction(playerEntity);
        } else if (livingEntity instanceof Mob mobEntity) {
            handleMobInteraction(self, mobEntity);
        }

        livingEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
        float damage = self.getRandom().nextInt(4) > 2
            ? CommonMod.config.classicXenoConfigs.classicXenoTailAttackDamage
            : (float) CommonMod.config.classicXenoConfigs.classicXenoAttackDamage;
        livingEntity.hurt(GigDamageSources.of(self.level(), GigDamageSources.XENO), damage);

        self.heal(1.0833f);
    }

    public static void handlePlayerInteraction(Player playerEntity) {
        playerEntity.drop(playerEntity.getInventory().getSelected(), false);
        playerEntity.getInventory().setItem(playerEntity.getInventory().selected, ItemStack.EMPTY);
    }

    public static void handleMobInteraction(AlienEntity self, Mob mobEntity) {
        self.drop(mobEntity, mobEntity.getMainHandItem());
        mobEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
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

}
