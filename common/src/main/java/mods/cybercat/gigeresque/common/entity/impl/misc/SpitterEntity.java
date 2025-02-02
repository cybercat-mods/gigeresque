package mods.cybercat.gigeresque.common.entity.impl.misc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.StrafeTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienProjectileAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class SpitterEntity extends AlienEntity implements SmartBrainOwner<SpitterEntity> {

    public SpitterEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.3F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.spitterConfigs.spitterXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.spitterConfigs.spitterXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                0.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
            .add(
                Attributes.FOLLOW_RANGE,
                16.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.spitterConfigs.spitterAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    protected Brain.@NotNull Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

    @Override
    public List<ExtendedSensor<SpitterEntity>> getSensors() {
        return ObjectArrayList.of(
            // Player Sensor
            new NearbyPlayersSensor<>(),
            // Living Sensor
            new NearbyLivingEntitySensor<SpitterEntity>().setRadius(32)
                .setPredicate(GigEntityUtils::entityTest),
            // Block Sensor
            new NearbyBlocksSensor<SpitterEntity>().setRadius(7),
            // Fire Sensor
            new NearbyRepellentsSensor<SpitterEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            // Lights Sensor
            new NearbyLightsBlocksSensor<SpitterEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            // Nest Sensor
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Looks at target
            new LookAtTarget<>(),
            // Flee Fire
            new FleeFireTask<>(1.3F),
            new StrafeTarget<SpitterEntity>().stopStrafingWhen(
                spitterEntity -> spitterEntity.getTarget() != null && spitterEntity.isWithinMeleeAttackRange(spitterEntity.getTarget())
            ),
            new MoveToWalkTarget<>()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<SpitterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Kill Lights
            new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
            // Do first
            new FirstApplicableBehaviour<SpitterEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                )
                    .stopIf(
                        entity -> this.isPassedOut() || this.isExecuting()
                    ),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching()
                )
            ).stopIf(
                entity -> this.isPassedOut() || this.isExecuting()
            ),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(1.15f),
                // Idle
                new Idle<>().startCondition(entity -> !this.isAggressive())
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            // Invalidate Target
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            // Walk to Target
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5F),
            // Xeno Acid Spit
            new AlienProjectileAttack<>(18, GigMeleeAttackSelector.SPITTER_RANGE_SELECTOR),
            // Xeno attacking
            new AlienMeleeAttack<>(5, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        if (this.level().isClientSide())
            this.handleAnimations();
        GigEntityUtils.breakBlocks(this);
        if (!this.isInWater())
            this.setIsCrawling(
                this.horizontalCollision || !this.level().getBlockState(this.blockPosition().below()).isSolid()
            );
    }

    protected void handleAnimations() {
        if (this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
            return;
        }
        if (this.moveAnalysis.isMoving()) {
            this.handleMovementAnimations();
        } else {
            this.handleIdleAnimations();
        }
    }

    protected void handleAggroMovementAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRun);
        }
    }

    protected void handleMovementAnimations() {
        if (this.isAggressive()) {
            this.handleAggroMovementAnimations();
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendWalk);
        }
    }

    protected void handleIdleAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdle);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        if (spawnType != MobSpawnType.NATURAL)
            setGrowth(getMaxGrowth());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if (this.wasEyeInWater)
            return EntityDimensions.scalable(3.0f, 1.0f);
        if (this.isTunnelCrawling())
            return EntityDimensions.scalable(0.9f, 0.9f);
        return EntityDimensions.scalable(0.9f, 1.9f);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (
            target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom()
                .nextInt(
                    0,
                    10
                ) > 7
        ) {
            if (livingEntity instanceof Player playerEntity) {
                playerEntity.drop(playerEntity.getInventory().getSelected(), false);
                playerEntity.getInventory().setItem(playerEntity.getInventory().selected, ItemStack.EMPTY);
            }
            if (livingEntity instanceof Mob mobEntity) {
                mobEntity.getMainHandItem();
                this.drop(mobEntity, mobEntity.getMainHandItem());
                mobEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
            }
            livingEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
            livingEntity.hurt(
                damageSources().mobAttack(this),
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.spitterConfigs.spitterXenoTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper)
            creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    public void shootAcid(LivingEntity target, LivingEntity attacker) {
        if (attacker.hasLineOfSight(target)) {
            var acidProjectile = GigEntities.ACID_PROJECTILE.get().create(this.level());
            if (acidProjectile != null) {
                // Position the projectile in front of the attacker
                final var attackDirection = attacker.getViewVector(1.0F); // Get view vector
                acidProjectile.setPos(
                    attacker.getX() + attackDirection.x * 2,
                    attacker.getY(0.5), // Adjust vertical position
                    attacker.getZ() + attackDirection.z * 2
                );

                // Calculate the direction vector toward the target (from attacker to target)
                double dx = target.getX() - acidProjectile.getX();
                double dy = target.getY(0.5) - acidProjectile.getY(); // Aim for the center of the target
                double dz = target.getZ() - acidProjectile.getZ();

                // Set the projectile's velocity towards the target
                float velocity = 1.0F; // Initial speed
                float inaccuracy = 0.1F; // Lower value = better aim
                acidProjectile.shoot(dx, dy, dz, velocity, inaccuracy);

                // Spawn the projectile into the world
                attacker.level().addFreshEntity(acidProjectile);
            }
        }
    }

}
