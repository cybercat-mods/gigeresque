package mods.cybercat.gigeresque.common.entity.impl.templebeast;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.BreakBlocksGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.DelayedAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.*;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class MoonlightHorrorTempleBeastEntity extends AlienEntity {

    public MoonlightHorrorTempleBeastEntity(EntityType<? extends AlienEntity> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.animationSelector = GigMeleeAttackSelector.STANDARD_ANIM_SELECTOR;
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.moonlightHorrorTempleBeastConfigs.moonlightHorrorTempleBeastXenoHealth
            )
            .add(
                Attributes.ARMOR,
                CommonMod.config.moonlightHorrorTempleBeastConfigs.moonlightHorrorTempleBeastXenoArmor
            )
            .add(Attributes.ARMOR_TOUGHNESS, 0.0)
            .add(
                Attributes.KNOCKBACK_RESISTANCE,
                0.0
            )
            .add(Attributes.FOLLOW_RANGE, 16.0)
            .add(
                Attributes.MOVEMENT_SPEED,
                0.3300000041723251
            )
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.moonlightHorrorTempleBeastConfigs.moonlightHorrorTempleBeastAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FleeExplodingCreeperGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.8));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(1, new DelayedAttackGoal(this, 1.1F, 5));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(7, new FindDarknessGoal(this)); // TODO: Find Darkness Goal
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 15.0F));
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
