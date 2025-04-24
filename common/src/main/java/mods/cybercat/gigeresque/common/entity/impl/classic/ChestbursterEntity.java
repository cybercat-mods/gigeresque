package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.*;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.*;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class ChestbursterEntity extends AlienEntity {

    protected String hostId = null;

    protected int delayBeforeEating = 0;

    protected boolean triggeredAttackAnimation = false;

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

        if (this.isBirthed() && this.tickCount > 1200 && this.getGrowth() > 200)
            this.setBirthStatus(false);
        if (this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
        }
        if (this.getVehicle() instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendImpregate);
        }

        if (!this.level().isClientSide) {
            var radius = this.getBoundingBox().inflate(1.25D);
            this.level()
                .getEntitiesOfClass(ItemEntity.class, radius)
                .stream()
                .filter(itemEntity -> itemEntity.getItem().is(GigTags.BURSTER_FOODS))
                .findFirst()
                .ifPresent(this::checkAndPerformEating);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (hostId != null)
            nbt.putString("hostId", hostId);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("growth"))
            setGrowth(nbt.getFloat("growth"));
        if (nbt.contains("hostId"))
            hostId = nbt.getString("hostId");
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this));
        this.goalSelector.addGoal(3, new EatFoodBlockGoal(this));
        this.goalSelector.addGoal(3, new EatFoodItemGoal(this, 0.9F, 5));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 15.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AlienEntity.class).setAlertOthers());
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.chestbursterGrowthMultiplier;
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

    protected void checkAndPerformEating(ItemEntity target) {
        if (target == null)
            return;
        if (this.isBirthed())
            return;

        if (isWithinEatingRange(target)) {
            if (this.delayBeforeEating > 0) {
                this.delayBeforeEating--;
                this.lookAt(target, 10.0F, 10.0F);

                if (this.delayBeforeEating == 5 && !this.triggeredAttackAnimation) {
                    this.animationDispatcher.sendChomp();
                    this.triggeredAttackAnimation = true;
                }
            } else {
                if (target.getItem().is(GigTags.POTIONS)) {
                    this.playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
                } else {
                    this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                }
                this.swing(InteractionHand.MAIN_HAND);
                float growthValue;
                if (target.getItem().has(DataComponents.FOOD)) {
                    var foodComponent = target.getItem().get(DataComponents.FOOD);
                    growthValue = foodComponent.nutrition() * 20.0F;
                    target.getItem().finishUsingItem(this.level(), this);
                } else {
                    growthValue = 20.0F;
                    if (target.getItem().is(GigTags.POTIONS)) {
                        target.getItem().finishUsingItem(this.level(), this);
                        target.getItem().consume(1, this);
                    } else {
                        target.getItem().consume(1, this);
                    }
                }
                this.setGrowth(this.getGrowth() + growthValue);
                this.triggeredAttackAnimation = false;
                this.delayBeforeEating = 20;
            }
        } else {
            delayBeforeEating--;
            this.triggeredAttackAnimation = false;
        }
    }

    public boolean isWithinEatingRange(@NotNull ItemEntity entity) {
        for (
            var testPos : BlockPos.betweenClosed(
                this.blockPosition()
                    .relative(this.getDirection(), 1)
                    .above(-1)
                    .relative(this.getDirection().getClockWise(), -1),
                this.blockPosition().relative(this.getDirection(), 3).above(1).relative(this.getDirection().getClockWise(), 1)
            )
        ) {
            if (entity.blockPosition().equals(testPos)) {
                return true;
            }
        }
        return this.getBoundingBox().intersects(entity.getBoundingBox().inflate(1.5F));
    }
}
