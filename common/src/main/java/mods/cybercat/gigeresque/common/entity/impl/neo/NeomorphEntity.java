package mods.cybercat.gigeresque.common.entity.impl.neo;

import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goals.RotateTowardsEntityGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.BreakBlocksGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.DelayedAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.LungeAtTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.DigToTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.DodgeProjectilesGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FindDarknessGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeExplodingCreeperGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFightGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.StrollAroundInWaterGoal;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class NeomorphEntity extends AlienEntity {

    public NeomorphEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world, Options.neomorph(3));
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.5F);
        this.animationSelector = GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.entityConfigs.neomorphConfigs.neomorphXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.entityConfigs.neomorphConfigs.neomorphXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                CommonMod.config.entityConfigs.neomorphConfigs.neomorphXenoArmor
            )
            .add(
                Attributes.KNOCKBACK_RESISTANCE,
                0.0
            )
            .add(Attributes.FOLLOW_RANGE, 32.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.entityConfigs.neomorphConfigs.neomorphAttackDamage + 5
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    protected @Nullable EntityDimensions swimmingDimensions(Pose pose) {
        return EntityDimensions.scalable(3, 1);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 1)
            GigCommonMethods.generateSporeCloud(this, this.blockPosition(), 0, 0, 3.0f);
        super.tickDeath();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FleeExplodingCreeperGoal(this));
        this.goalSelector.addGoal(0, new DodgeProjectilesGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new DelayedAttackGoal(this, 1.25F, 5));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.05F, 20 * 10, 16).setOnLungeCallback(this::runLungeAnimation));
        this.goalSelector.addGoal(4, new BreakBlocksGoal(this, GigTags.DESTRUCTIBLE_LIGHT, 1.5F));
        this.goalSelector.addGoal(5, new DigToTargetGoal(this, 32));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(7, new FindDarknessGoal(this)); // TODO: Find Darkness Goal
        this.goalSelector.addGoal(9, new RotateTowardsEntityGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new RotateTowardsEntityGoal(this, LivingEntity.class, 15.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AlienEntity.class).setAlertOthers());
        this.targetSelector.addGoal(
            2,
            new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                false,
                target -> this.getHealth() > (this.getMaxHealth() / 2) && GigEntityUtils.isValidTarget(target)
            )
        );
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
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.entityConfigs.neomorphConfigs.neomorphXenoTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

}
