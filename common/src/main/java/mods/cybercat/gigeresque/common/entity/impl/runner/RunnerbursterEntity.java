package mods.cybercat.gigeresque.common.entity.impl.runner;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeExplodingCreeperGoal;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.DelayedAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.EatFoodBlockGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.EatFoodItemGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFightGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.StrollAroundInWaterGoal;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class RunnerbursterEntity extends ChestbursterEntity implements Growable {

    public RunnerbursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.animationSelector = GigMeleeAttackSelector.RBUSTER_ANIM_SELECTOR;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.runnerbusterConfigs.runnerbusterHealth
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
                CommonMod.config.runnerbusterConfigs.runnerbusterAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.runnerbursterGrowthMultiplier;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getGrowth() < 2) {
            this.animationDispatcher.sendBirth();
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 10), this);
        }
    }

    /**
     * TODO: Remove classic alien when Rom stages ready ready
     */
    @Override
    public LivingEntity growInto() {
        LivingEntity alien;
        if (Objects.equals(hostId, "runner"))
            alien = GigEntities.RUNNER_ALIEN.get().create(level());
        else
            alien = GigEntities.ALIEN.get().create(level());
        // alien = GigEntities.ROM_ALIEN.get().create(level());

        return alien;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FleeExplodingCreeperGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(1, new DelayedAttackGoal(this, 1.1F, 5));
        this.goalSelector.addGoal(3, new EatFoodBlockGoal(this));
        this.goalSelector.addGoal(3, new EatFoodItemGoal(this, 0.9F, 5));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 15.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AlienEntity.class).setAlertOthers());
        this.targetSelector.addGoal(
            2,
            new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                false,
                target -> this.getHealth() > (this.getMaxHealth() / 2) && GigEntityUtils.removeTarget(target)
                    && target.getBbHeight() < 0.8
            )
        );
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        if (spawnType == MobSpawnType.SPAWN_EGG)
            setHostId("runner");
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}
