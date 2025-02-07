package mods.cybercat.gigeresque.common.entity.helper;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.states.EggStates;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public record GigCommonMethods() {

    public static void setAnimation(Runnable animationAction) {
        animationAction.run();
    }

    public static void generateAcidPool(LivingEntity entity, BlockPos pos, int xOffset, int zOffset) {
        var acidEntity = GigEntities.ACID.get().create(entity.level());
        assert acidEntity != null;
        acidEntity.moveTo(pos.offset(xOffset, 0, zOffset), entity.getYRot(), entity.getXRot());
        entity.level().addFreshEntity(acidEntity);
    }

    public static void generateGooBlood(LivingEntity entity, BlockPos pos, int xOffset, int zOffset) {
        var acidEntity = GigEntities.GOO.get().create(entity.level());
        assert acidEntity != null;
        acidEntity.moveTo(pos.offset(xOffset, 0, zOffset), entity.getYRot(), entity.getXRot());
        entity.level().addFreshEntity(acidEntity);
    }

    public static void generateSporeCloud(LivingEntity entity, BlockPos pos, int xOffset, int zOffset) {
        var areaEffectCloudEntity = new AreaEffectCloud(
            entity.level(),
            pos.getX(),
            pos.getY() + 0.5,
            pos.getZ()
        );
        areaEffectCloudEntity.setRadius(3.0F);
        areaEffectCloudEntity.setDuration(150);
        areaEffectCloudEntity.setRadiusPerTick(
            -areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration()
        );
        areaEffectCloudEntity.setParticle(ParticleTypes.ASH);
        if (!entity.hasEffect(GigStatusEffects.SPORE)) {
            areaEffectCloudEntity.addEffect(
                new MobEffectInstance(GigStatusEffects.SPORE, CommonMod.config.sporeTickTimer, 0)
            );
        }
        entity.level().addFreshEntity(areaEffectCloudEntity);
    }

    public static void handleNestProgress(AlienEggEntity alienEggEntity) {
        if (alienEggEntity.getEggState() == EggStates.HATCHED.ordinal() && alienEggEntity.isAlive()) {
            alienEggEntity.setTicksUntilNest(alienEggEntity.ticksUntilNest++);
            if (alienEggEntity.getTicksUntilNest() >= 6000f) {
                if (alienEggEntity.level().isClientSide)
                    GigCommonMethods.spawnParticlesForNesting(alienEggEntity);
                alienEggEntity.level()
                    .setBlockAndUpdate(alienEggEntity.blockPosition(), GigBlocks.NEST_RESIN_WEB_CROSS.get().defaultBlockState());
                alienEggEntity.kill();
            }
        }
    }

    public static void spawnParticlesForNesting(AlienEggEntity alienEggEntity) {
        if (alienEggEntity.getTicksUntilNest() == 6000f) {
            for (int i = 0; i < 2; i++) {
                alienEggEntity.level()
                    .addAlwaysVisibleParticle(
                        GigParticles.GOO.get(),
                        alienEggEntity.getRandomX(1.0),
                        alienEggEntity.getRandomY(),
                        alienEggEntity.getRandomZ(1.0),
                        0.0,
                        0.0,
                        0.0
                    );
            }
        }
    }

    public static void handleHatchingProgress(AlienEggEntity alienEggEntity) {
        if (alienEggEntity.getEggState() == EggStates.HATCHING.ordinal()) {
            if (alienEggEntity.hatchProgress < AlienEggEntity.MAX_HATCH_PROGRESS) {
                alienEggEntity.hatchProgress++;
            }

            if (alienEggEntity.hatchProgress == 2L) {
                alienEggEntity.level()
                    .playSound(alienEggEntity, alienEggEntity.blockPosition(), GigSounds.EGG_OPEN.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
            }

            if (alienEggEntity.hatchProgress >= AlienEggEntity.MAX_HATCH_PROGRESS) {
                alienEggEntity.setEggState(EggStates.HATCHED.ordinal());
                alienEggEntity.ticksOpen++;
            }
        }

        if (alienEggEntity.getEggState() == EggStates.HATCHED.ordinal() && alienEggEntity.hasFacehugger()) {
            alienEggEntity.ticksOpen++;
        }
    }

    public static void handleFacehuggerSpawn(AlienEggEntity alienEggEntity) {
        if (alienEggEntity.ticksOpen >= 3L * Constants.TPS && alienEggEntity.hasFacehugger() && !alienEggEntity.isDeadOrDying()) {
            var facehugger = GigEntities.FACEHUGGER.get().create(alienEggEntity.level());
            if (facehugger != null) {
                facehugger.setPos(alienEggEntity.position().x, alienEggEntity.position().y + 1, alienEggEntity.position().z);
                facehugger.setDeltaMovement(
                    Mth.nextFloat(facehugger.getRandom(), -0.5f, 0.5f),
                    0.7,
                    Mth.nextFloat(facehugger.getRandom(), -0.5f, 0.5f)
                );
                alienEggEntity.level().addFreshEntity(facehugger);
            }
            alienEggEntity.setHasFacehugger(false);
        }
    }

    public static void handleAoEEntityHatchCheck(AlienEggEntity alienEggEntity) {
        // Perform hatching check once every second (20 ticks)
        if (alienEggEntity.hatchCheckTimer >= 20) {
            alienEggEntity.hatchCheckTimer = 0; // Reset the timer

            alienEggEntity.level()
                .getEntitiesOfClass(
                    LivingEntity.class,
                    alienEggEntity.getBoundingBox().inflate(CommonMod.config.eggConfigs.alieneggHatchRange)
                )
                .forEach(target -> {
                    if (target.isAlive() && GigEntityUtils.faceHuggerTest(target)) {
                        if (alienEggEntity.level().random.nextFloat() < 0.2f) { // 20% chance to hatch every second
                            if (!target.isSteppingCarefully() && Constants.isNotCreativeSpecPlayer.test(target)) {
                                alienEggEntity.setEggState(EggStates.HATCHING.ordinal());
                            }
                        }
                    }
                });

            alienEggEntity.level().getEntitiesOfClass(LivingEntity.class, alienEggEntity.getBoundingBox().inflate(3)).forEach(target -> {
                if (
                    target.isAlive() && GigEntityUtils.faceHuggerTest(target) && alienEggEntity.level().random.nextFloat() < 0.8f
                        && (target instanceof Player player && !(player.isCreative() || player.isSpectator())
                            || !(target instanceof Player))
                ) {
                    alienEggEntity.setEggState(EggStates.HATCHING.ordinal());
                }
            });
        }
    }

    public static void handleAoEBlockHatchCheck(AlienEggEntity alienEggEntity) {
        if (alienEggEntity.getLastHurtMob() == null)
            // Loop through nearby blocks in different directions (this logic remains the same)
            for (var testPos : BlockPos.betweenClosed(alienEggEntity.blockPosition().above(1), alienEggEntity.blockPosition().above(1))) {
                for (
                    var testPos1 : BlockPos.betweenClosed(alienEggEntity.blockPosition().below(1), alienEggEntity.blockPosition().below(1))
                ) {
                    for (
                        var testPos2 : BlockPos.betweenClosed(
                            alienEggEntity.blockPosition().east(1),
                            alienEggEntity.blockPosition().east(1)
                        )
                    ) {
                        for (
                            var testPos3 : BlockPos.betweenClosed(
                                alienEggEntity.blockPosition().west(1),
                                alienEggEntity.blockPosition().west(1)
                            )
                        ) {
                            for (
                                var testPos4 : BlockPos.betweenClosed(
                                    alienEggEntity.blockPosition().south(1),
                                    alienEggEntity.blockPosition().south(1)
                                )
                            ) {
                                for (
                                    var testPos5 : BlockPos.betweenClosed(
                                        alienEggEntity.blockPosition().north(1),
                                        alienEggEntity.blockPosition().north(1)
                                    )
                                ) {
                                    // Check if any nearby blocks are not air
                                    boolean isAnyBlockNotAir = !alienEggEntity.level().getBlockState(testPos).isAir() &&
                                        !alienEggEntity.level().getBlockState(testPos1).isAir() &&
                                        !alienEggEntity.level().getBlockState(testPos2).isAir() &&
                                        !alienEggEntity.level().getBlockState(testPos3).isAir() &&
                                        !alienEggEntity.level().getBlockState(testPos4).isAir() &&
                                        !alienEggEntity.level().getBlockState(testPos5).isAir();

                                    // Check if any nearby blocks are solid
                                    boolean isAnyBlockSolid = !alienEggEntity.level()
                                        .getBlockState(testPos)
                                        .isCollisionShapeFullBlock(alienEggEntity.level(), testPos) &&
                                        !alienEggEntity.level()
                                            .getBlockState(testPos1)
                                            .isCollisionShapeFullBlock(alienEggEntity.level(), testPos1) &&
                                        !alienEggEntity.level()
                                            .getBlockState(testPos2)
                                            .isCollisionShapeFullBlock(alienEggEntity.level(), testPos2) &&
                                        !alienEggEntity.level()
                                            .getBlockState(testPos3)
                                            .isCollisionShapeFullBlock(alienEggEntity.level(), testPos3) &&
                                        !alienEggEntity.level()
                                            .getBlockState(testPos4)
                                            .isCollisionShapeFullBlock(alienEggEntity.level(), testPos4) &&
                                        !alienEggEntity.level()
                                            .getBlockState(testPos5)
                                            .isCollisionShapeFullBlock(alienEggEntity.level(), testPos5);

                                    // Set isHatching to false if conditions are met
                                    if (isAnyBlockSolid || isAnyBlockNotAir) {
                                        alienEggEntity.setEggState(EggStates.IDLE.ordinal());
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    public static void handlePlayerInteraction(AlienEggEntity alienEggEntity) {
        if (alienEggEntity.getLastHurtMob() != null) {
            alienEggEntity.setEggState(EggStates.HATCHING.ordinal());
        }
    }

    public static void handleFloatingPhysics(Entity alienEgg) {
        alienEgg.xo = alienEgg.getX();
        alienEgg.yo = alienEgg.getY();
        alienEgg.zo = alienEgg.getZ();

        Vec3 motion = alienEgg.getDeltaMovement();
        double adjustedY = motion.y + (motion.y < 0.05999999865889549 ? 5.0E-4F : 0.0F);

        if (isFloatingInFluid(alienEgg, FluidTags.WATER) || isFloatingInFluid(alienEgg, FluidTags.LAVA)) {
            alienEgg.setDeltaMovement(motion.x * 0.99, adjustedY, motion.z * 0.99);
        } else {
            alienEgg.applyGravity();
        }
    }

    public static boolean isFloatingInFluid(Entity alienEgg, TagKey<Fluid> fluidTag) {
        return alienEgg.isInWater() && alienEgg.getFluidHeight(fluidTag) > 0.1;
    }

    public static void handleCollisionPhysics(Entity alienEgg) {
        if (alienEgg.level().isClientSide) {
            alienEgg.noPhysics = false;
        } else {
            alienEgg.noPhysics = !alienEgg.level().noCollision(alienEgg, alienEgg.getBoundingBox().deflate(1.0E-7));
            if (alienEgg.noPhysics) {
                moveToClosestSpace(alienEgg);
            }
        }
    }

    public static void moveToClosestSpace(Entity alienEgg) {
        alienEgg.moveTowardsClosestSpace(
            alienEgg.getX(),
            (alienEgg.getBoundingBox().minY + alienEgg.getBoundingBox().maxY) / 1.5,
            alienEgg.getZ()
        );
    }

    public static void handleMovement(Entity alienEgg) {
        if (shouldApplyMovement(alienEgg)) {
            alienEgg.move(MoverType.SELF, alienEgg.getDeltaMovement());

            float friction = getGroundFriction(alienEgg);
            alienEgg.setDeltaMovement(alienEgg.getDeltaMovement().multiply(friction, 0.78, friction));

            handleGroundImpact(alienEgg);
        }
    }

    public static boolean shouldApplyMovement(Entity alienEgg) {
        return !alienEgg.onGround()
            || alienEgg.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5
            || (alienEgg.tickCount + alienEgg.getId()) % 4 == 0;
    }

    public static float getGroundFriction(Entity alienEgg) {
        if (alienEgg.onGround()) {
            return alienEgg.level()
                .getBlockState(alienEgg.getBlockPosBelowThatAffectsMyMovement())
                .getBlock()
                .getFriction() * 0.98F;
        }
        return 0.98F;
    }

    public static void handleGroundImpact(Entity alienEgg) {
        if (alienEgg.onGround()) {
            Vec3 motion = alienEgg.getDeltaMovement();
            if (motion.y < 0.0) {
                alienEgg.setDeltaMovement(motion.multiply(1.0, -0.1, 1.0));
            }
        }
    }
}
