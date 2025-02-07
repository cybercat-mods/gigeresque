package mods.cybercat.gigeresque.common.entity.impl.classic;

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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillCropsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.AlienPanic;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EatFoodTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFightTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class ChestbursterEntity extends AlienEntity implements Growable, SmartBrainOwner<ChestbursterEntity> {

    public static final EntityDataAccessor<Boolean> BIRTHED = SynchedEntityData.defineId(
        ChestbursterEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    public static final EntityDataAccessor<Boolean> EAT = SynchedEntityData.defineId(
        ChestbursterEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    private static final EntityDataAccessor<Float> BLOOD = SynchedEntityData.defineId(
        ChestbursterEntity.class,
        EntityDataSerializers.FLOAT
    );

    private static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(
        ChestbursterEntity.class,
        EntityDataSerializers.FLOAT
    );

    public int bloodRendering = 0;

    public int eatingCounter = 0;

    protected String hostId = null;

    public ChestbursterEntity(EntityType<? extends ChestbursterEntity> type, Level world) {
        super(type, world);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.chestbursterHealth
            )
            .add(Attributes.ARMOR, 0.0f)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                0.0f
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
            .add(Attributes.FOLLOW_RANGE, 32.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                0.0f
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    public float getBlood() {
        return entityData.get(BLOOD);
    }

    public void setBlood(float growth) {
        entityData.set(BLOOD, growth);
    }

    @Override
    public int getAcidDiameter() {
        return 1;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public boolean isBirthed() {
        return this.entityData.get(BIRTHED);
    }

    public void setBirthStatus(boolean birth) {
        this.entityData.set(BIRTHED, birth);
    }

    public boolean isEating() {
        return this.entityData.get(EAT);
    }

    public void setEatingStatus(boolean birth) {
        this.entityData.set(EAT, birth);
    }

    @Override
    public float getGrowth() {
        return entityData.get(GROWTH);
    }

    @Override
    public void setGrowth(float growth) {
        entityData.set(GROWTH, growth);
    }

    @Override
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GROWTH, 0.0f);
        builder.define(BLOOD, 0.0f);
        builder.define(BIRTHED, false);
        builder.define(EAT, false);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return GigSounds.HUGGER_HURT.get();
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return GigSounds.HUGGER_DEATH.get();
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();

        if (!level().isClientSide && this.isAlive()) {
            setBlood(bloodRendering++);
            grow(this, 1 * getGrowthMultiplier());
        }
        this.handleEatingFunctions();
        if (this.isBirthed() && this.tickCount > 1200 && this.getGrowth() > 200)
            this.setBirthStatus(false);
        if (this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
        }
        if (this.getVehicle() instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendImpregate);
        }
        if (!this.isEating() && this.level().isClientSide())
            this.handleAnimations();
    }

    protected void handleEatingFunctions() {
        if (this.isEating())
            eatingCounter++;
        if (eatingCounter >= 20) {
            this.setEatingStatus(false);
            eatingCounter = 0;
        }
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
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRushSlither);
        }
    }

    protected void handleMovementAnimations() {
        if (this.isAggressive()) {
            this.handleAggroMovementAnimations();
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendSlither);
        }
    }

    protected void handleIdleAnimations() {
        GigCommonMethods.setAnimation(animationDispatcher::sendIdle);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("growth", getGrowth());
        if (hostId != null)
            nbt.putString("hostId", hostId);
        nbt.putBoolean("is_eating", isEating());
        nbt.putBoolean("is_birthed", isBirthed());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("growth"))
            setGrowth(nbt.getFloat("growth"));
        if (nbt.contains("hostId"))
            hostId = nbt.getString("hostId");
        if (nbt.contains("is_eating"))
            setEatingStatus(nbt.getBoolean("is_eating"));
        if (nbt.contains("is_birthed"))
            setBirthStatus(nbt.getBoolean("is_birthed"));
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
    public List<ExtendedSensor<ChestbursterEntity>> getSensors() {
        return ObjectArrayList.of(
            new NearbyPlayersSensor<>(),
            new NearbyLivingEntitySensor<ChestbursterEntity>().setRadius(15)
                .setPredicate(
                    GigEntityUtils::entityTest
                ),
            new NearbyBlocksSensor<ChestbursterEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.BURSTER_BLOCKS)
                ),
            new NearbyRepellentsSensor<ChestbursterEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            new NearbyLightsBlocksSensor<ChestbursterEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            new HurtBySensor<>(),
            new ItemEntitySensor<>(),
            new UnreachableTargetSensor<>(),
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Flee fight at half or less health
            new FleeFightTask<>(1.1F).startCondition(entity -> this.getHealth() <= (this.getMaxHealth() / 2))
                .stopIf(entity -> this.getHealth() > (this.getMaxHealth() / 2))
                .whenStarting(entity -> entity.setFleeingStatus(true))
                .whenStopping(entity -> entity.setFleeingStatus(false)),
            // Flee Fire
            new FleeFireTask<>(1.0F),
            new AlienPanic(1.0f),
            // Looks at target
            new LookAtTarget<>().stopIf(entity -> this.stasisManager.isStasis())
                .startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching()
                ),
            // Move to target
            new MoveToWalkTarget<>().startCondition(entity -> !this.stasisManager.isStasis())
                .stopIf(
                    entity -> this.stasisManager.isStasis()
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Build Nest
            new EatFoodTask<>(10),
            // Kill Lights
            new KillLightsTask<>(),
            new KillCropsTask<>(),
            // Do first
            new FirstApplicableBehaviour<RunnerAlienEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                )
                    .stopIf(
                        entity -> this.stasisManager.isStasis() || this.isExecuting()
                    ),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching()
                )
            ).stopIf(
                entity -> this.stasisManager.isStasis() || this.isExecuting()
            ),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater()
                    .setRadius(20)
                    .speedModifier(0.67f)
                    .startCondition(entity -> !this.moveAnalysis.isMoving()),
                // Idle
                new Idle<>().startCondition(entity -> !this.isAggressive())
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target))
        );
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.chestbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return Constants.TPD / 2.0f;
    }

    /**
     * TODO: replace runnerburster with rom Cocoon when not runner
     */
    @Override
    public LivingEntity growInto() {
        // LivingEntity entity;
        // if (Objects.equals(hostId, "runner")) entity = GigEntities.RUNNER_ALIEN.get().create(level());
        // else entity = GigEntities.ALIEN_COCOON.get().create(level());
        var entity = GigEntities.RUNNERBURSTER.get().create(level());
        if (entity != null) {
            entity.hostId = this.hostId;
            if (hasCustomName())
                entity.setCustomName(this.getCustomName());
        }
        return entity;
    }
}
