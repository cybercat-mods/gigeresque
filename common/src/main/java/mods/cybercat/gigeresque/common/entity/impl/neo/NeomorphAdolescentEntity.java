package mods.cybercat.gigeresque.common.entity.impl.neo;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class NeomorphAdolescentEntity extends AlienEntity {

    public NeomorphAdolescentEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 2.65F);
        this.animationSelector = GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.neomorphAdolescentConfigs.neomorph_adolescentXenoHealth
            )
            .add(Attributes.ARMOR, 0.0f)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                6.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 7.0)
            .add(
                Attributes.FOLLOW_RANGE,
                32.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.neomorphAdolescentConfigs.neomorph_adolescentAttackDamage + 5
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
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
            livingEntity.hurt(
                damageSources().mobAttack(this),
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.neomorphAdolescentConfigs.neomorph_adolescentXenoTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.chestbursterGrowthMultiplier;
    }

    @Override
    public LivingEntity growInto() {
        return GigEntities.NEOMORPH.get().create(level());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EatFoodItemGoal(this, 0.9F, 5));
        this.goalSelector.addGoal(1, new EatFoodBlockGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(1, new DelayedAttackGoal(this, 1.15F, 10));
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
            )
        );
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
    }

}
