package mods.cybercat.gigeresque.common.entity.impl.runner;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goals.RotateTowardsEntityGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.BreakBlocksGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.DelayedAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.LungeAtTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.*;
import mods.cybercat.gigeresque.common.entity.ai.goals.nest.BuildNestGoal;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class RunnerAlienEntity extends AlienEntity {

    public RunnerAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
        super(type, world);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.5f);
        this.animationSelector = GigMeleeAttackSelector.STANDARD_ANIM_SELECTOR;
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
    }

    @Override
    public boolean isPathFinding() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FleeExplodingCreeperGoal(this));
        this.goalSelector.addGoal(0, new DodgeProjectilesGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new DelayedAttackGoal(this, 1.15F, 5));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.05F, 20 * 10, 16)); // TODO: Leaping Aniamtion
        this.goalSelector.addGoal(5, new DigToTargetGoal(this, 32));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(7, new BuildNestGoal(this));
        this.goalSelector.addGoal(7, new FindDarknessGoal(this)); // TODO: Find Darkness Goal
        this.goalSelector.addGoal(9, new RotateTowardsEntityGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new RotateTowardsEntityGoal(this, LivingEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new BreakBlocksGoal(this, GigTags.DESTRUCTIBLE_LIGHT, 1.5F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AlienEntity.class).setAlertOthers());
        this.targetSelector.addGoal(
            2,
            new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                false,
                target -> this.getHealth() > (this.getMaxHealth() / 2) && GigEntityUtils.removeTarget(target)
            )
        );
    }
}
