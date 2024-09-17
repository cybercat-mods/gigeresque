package mods.cybercat.gigeresque.common.entity.impl.classic;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.navigation.SmoothGroundNavigation;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.GigNav;
import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillCropsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.AlienPanic;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EatFoodTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

public class ChestbursterEntity extends AlienEntity implements Growable, SmartBrainOwner<ChestbursterEntity> {

    public static final EntityDataAccessor<Boolean> BIRTHED = SynchedEntityData.defineId(ChestbursterEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> EAT = SynchedEntityData.defineId(ChestbursterEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> BLOOD = SynchedEntityData.defineId(ChestbursterEntity.class,
            EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(ChestbursterEntity.class,
            EntityDataSerializers.FLOAT);
    private final GigNav landNavigation = new GigNav(this, level());
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    public int bloodRendering = 0;
    public int eatingCounter = 0;
    protected String hostId = null;

    public ChestbursterEntity(EntityType<? extends ChestbursterEntity> type, Level world) {
        super(type, world);
        navigation = landNavigation;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                CommonMod.config.chestbursterHealth).add(Attributes.ARMOR, 2.0).add(Attributes.ARMOR_TOUGHNESS,
                0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(
                Attributes.MOVEMENT_SPEED, 0.3300000041723251).add(Attributes.ATTACK_DAMAGE, 5.0).add(
                Attributes.ATTACK_KNOCKBACK, 0.3);
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
    public SoundEvent getHurtSound(@NotNull DamageSource source) {
        return GigSounds.HUGGER_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return GigSounds.HUGGER_DEATH.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && this.isAlive()) {
            setBlood(bloodRendering++);
            grow(this, 1 * getGrowthMultiplier());
        }
        if (this.isEating()) eatingCounter++;
        if (eatingCounter >= 20) {
            this.setEatingStatus(false);
            eatingCounter = 0;
        }
        if (this.isBirthed() && this.tickCount > 1200 && this.getGrowth() > 200) this.setBirthStatus(false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("growth", getGrowth());
        if (hostId != null) nbt.putString("hostId", hostId);
        nbt.putBoolean("is_eating", isEating());
        nbt.putBoolean("is_birthed", isBirthed());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("growth")) setGrowth(nbt.getFloat("growth"));
        if (nbt.contains("hostId")) hostId = nbt.getString("hostId");
        if (nbt.contains("is_eating")) setEatingStatus(nbt.getBoolean("is_eating"));
        if (nbt.contains("is_birthed")) setBirthStatus(nbt.getBoolean("is_birthed"));
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
        return ObjectArrayList.of(new NearbyBlocksSensor<ChestbursterEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(BlockTags.CROPS)),
                new NearbyRepellentsSensor<ChestbursterEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new ItemEntitySensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new FleeFireTask<>(1.2F), new AlienPanic(2.0f), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new EatFoodTask<>(40), new KillCropsTask<>(),
                new FirstApplicableBehaviour<ChestbursterEntity>(new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.65f),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().stopIf(target -> !target.isAlive()),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.2F));
    }

    /*
     * GROWTH
     */

    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.chestbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return Constants.TPD / 2.0f;
    }

    @Override
    public LivingEntity growInto() {
        var entity = GigEntities.RUNNERBURSTER.get().create(level());
        entity.hostId = this.hostId;
        if (hasCustomName()) entity.setCustomName(this.getCustomName());
        return entity;
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !isDead && walkAnimation.speedOld > 0.15F)
                if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUSH_SLITHER);
                else return event.setAndContinue(GigAnimationsDefault.SLITHER);
            else if (this.tickCount < 60 && event.getAnimatable().isBirthed())
                return event.setAndContinue(GigAnimationsDefault.BIRTH);
            else return event.setAndContinue(GigAnimationsDefault.IDLE);
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("stepSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.BURSTER_CRAWL.get(),
                        SoundSource.HOSTILE, 0.25F, 1.0F, true);
        }));
        controllers.add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0,
                event -> PlayState.STOP).triggerableAnim(Constants.EAT, GigAnimationsDefault.CHOMP).triggerableAnim(
                "death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
