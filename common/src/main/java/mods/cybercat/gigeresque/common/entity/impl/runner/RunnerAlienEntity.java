package mods.cybercat.gigeresque.common.entity.impl.runner;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.UnreachableTargetSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EnterStasisTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.*;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class RunnerAlienEntity extends AlienEntity implements SmartBrainOwner<RunnerAlienEntity> {

    public RunnerAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
        super(type, world);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.5f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.classicXenoConfigs.classicXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.runnerConfigs.runnerXenoHealth)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                CommonMod.config.runnerConfigs.runnerXenoArmor
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
            .add(Attributes.FOLLOW_RANGE, 32.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.runnerConfigs.runnerXenoAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    @Override
    @NotNull
    public EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if (this.wasEyeInWater)
            return EntityDimensions.scalable(3.0f, 1.0f);
        return EntityDimensions.scalable(0.9f, crawlingManager.isCrawling() ? 0.4f : 1.75f);
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
            if (target instanceof Player playerEntity) {
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
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.runnerConfigs.runnerXenoTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper)
            creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.runnerbusterConfigs.runnerAlienGrowthMultiplier;
    }

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
    public void tick() {
        super.tick();
        this.moveAnalysis.update();
        GigEntityUtils.breakBlocks(this);

        if (this.level().isClientSide) {
            this.handleAnimations();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onClimbable() {
        var blockPos = new BlockPos.MutableBlockPos(this.position().x, this.position().y + 2.0, this.position().z);
        if (this.level().getBlockState(blockPos).blocksMotion()) {
            this.inTwoBlockSpace = true;
        }
        if (!this.level().getBlockState(blockPos).blocksMotion()) {
            this.inTwoBlockSpace = false;
        }
        return this.inTwoBlockSpace;
    }

    @Override
    public boolean isPathFinding() {
        return false;
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
    public List<ExtendedSensor<RunnerAlienEntity>> getSensors() {
        return ObjectArrayList.of(
            new NearbyPlayersSensor<>(),
            new NearbyLivingEntitySensor<RunnerAlienEntity>().setRadius(30)
                .setPredicate(
                    GigEntityUtils::entityTest
                ),
            new NearbyBlocksSensor<RunnerAlienEntity>().setRadius(7),
            new NearbyRepellentsSensor<RunnerAlienEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            new NearbyLightsBlocksSensor<RunnerAlienEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            new HurtBySensor<>(),
            new UnreachableTargetSensor<>(),
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<RunnerAlienEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Flee fight at half or less health
            new FleeFightTask<>(1.0F).startCondition(entity -> this.getHealth() <= (this.getMaxHealth() / 2))
                .stopIf(entity -> this.getHealth() > (this.getMaxHealth() / 2)),
            // Flee Fire
            new FleeFireTask<>(3.5F),
            // Looks at target
            new LookAtTarget<>().stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting() || this.isAggressive())
                .startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching() || !this.isExecuting()
                ),
            // Move to target
            new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting())
                .stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting())
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<RunnerAlienEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Build Nest
            new BuildNestTask<>(90).startCondition(
                entity -> !this.isAggressive() || !this.stasisManager.isStasis() || !this.isExecuting() || !this.isFleeing()
                    || !crawlingManager.isCrawling()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.stasisManager.isStasis() || this.isFleeing())
                ),
            // Kill Lights
            new KillLightsTask<>().startCondition(
                entity -> !this.isAggressive() || !this.stasisManager.isStasis() || !this.isExecuting() || !this.isFleeing()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.stasisManager.isStasis() || this.isFleeing())
                ),
            // Find Darkness
            new FindDarknessTask<>(),
            // Do first
            new FirstApplicableBehaviour<RunnerAlienEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isFleeing() || this.stasisManager.isStasis())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                ).stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting()),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching()
                )
            ).stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting() || this.isAggressive()),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater()
                    .setRadius(20)
                    .speedModifier(0.7f)
                    .startCondition(
                        entity -> !this.stasisManager.isStasis() || !this.isExecuting() || !this.isAggressive() && !this.moveAnalysis
                            .isMoving()
                    )
                    .stopIf(
                        entity -> this.isExecuting() || this.stasisManager.isStasis() || this.isAggressive() || this.isVehicle()
                    ),
                // Idle
                new EnterStasisTask<>(6000)
            ).stopIf(entity -> entity.getDeltaMovement().horizontalDistance() > 0)
        );
    }

    @Override
    public BrainActivityGroup<RunnerAlienEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            new RunToAttackTargetTask<>().speedMod((owner, target) -> 1.25f)
                .closeEnoughDist((mob, livingEntity) -> 0)
                .stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting()),
            new JumpToTargetTask<>(20).stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting()),
            new AlienMeleeAttack<>(12, GigMeleeAttackSelector.STANDARD_ANIM_SELECTOR).stopIf(
                entity -> this.stasisManager.isStasis() || this.isExecuting()
            )
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
        if (this.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendCrawl);
        } else if (this.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendCrawl);
        } else if (this.isInWater()) {
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
        if (this.stasisManager.isStasis()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendStatisEnter);
        } else if (this.crawlingManager.isCrawling()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendCrawl);
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdleLand);
        }
    }
}
