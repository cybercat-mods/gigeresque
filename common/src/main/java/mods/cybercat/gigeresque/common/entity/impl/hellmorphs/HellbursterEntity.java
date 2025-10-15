package mods.cybercat.gigeresque.common.entity.impl.hellmorphs;

import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

public class HellbursterEntity extends RunnerbursterEntity implements Growable {

    public HellbursterEntity(EntityType<? extends HellbursterEntity> type, Level level) {
        super(type, level);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 0.0F);
        this.animationSelector = GigMeleeAttackSelector.RBUSTER_ANIM_SELECTOR;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.hellbusterConfigs.hellbusterHealth
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
                CommonMod.config.hellbusterConfigs.hellbusterAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.hellbusterConfigs.hellbusterGrowthMultiplier;
    }

    @Override
    public LivingEntity growInto() {
        LivingEntity alien;
        if (this.getRandom().nextInt(0, 100) >= 51)
            alien = GigEntities.BAPHOMORPH.get().create(level());
        else
            alien = GigEntities.HELLMORPH_RUNNER.get().create(level());

        return alien;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
}
