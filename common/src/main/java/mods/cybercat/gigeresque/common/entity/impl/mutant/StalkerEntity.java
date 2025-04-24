package mods.cybercat.gigeresque.common.entity.impl.mutant;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.BreakBlocksGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.DelayedAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.LungeAtTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.*;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class StalkerEntity extends AlienEntity {

    public StalkerEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.9F);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.animationSelector = GigMeleeAttackSelector.STALKER_ANIM_SELECTOR;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.stalkerConfigs.stalkerXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.stalkerConfigs.stalkerXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                0.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
            .add(
                Attributes.FOLLOW_RANGE,
                16.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.stalkerConfigs.stalkerAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DodgeProjectilesGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new DelayedAttackGoal(this, 1.1F, 5));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.05F, 20 * 10, 16).setOnLungeCallback(this::runLungeAnimation));
        this.goalSelector.addGoal(5, new DigToTargetGoal(this, 32));
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

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (!this.level().isClientSide && this.random.nextInt(5) == 0) {
            BlockPos.betweenClosedStream(this.getBoundingBox().inflate(10)).forEach(pos -> {
                var blockState = this.level().getBlockState(pos);
                if (blockState.is(GigTags.SPORE_REPLACE)) {
                    this.level().setBlockAndUpdate(pos, GigBlocks.SPORE_BLOCK.get().defaultBlockState());
                }
            });
        }
        if (!this.level().isClientSide && this.random.nextInt(10) == 0) {
            BlockPos.betweenClosedStream(this.getBoundingBox().inflate(10)).forEach(pos -> {
                var blockState = this.level().getBlockState(pos);
                if (blockState.is(Blocks.WATER)) {
                    this.level().setBlockAndUpdate(pos, GigBlocks.BLACK_FLUID.get().defaultBlockState());
                }
            });
        }
        super.die(source);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            var attacker = source.getEntity();
            if (source.getEntity() != null && attacker instanceof LivingEntity living)
                this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, living);
        }

        if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources()))
            return super.hurt(source, amount);

        if (!this.level().isClientSide && source != this.damageSources().genericKill()) {
            if (getAcidDiameter() == 1)
                GigCommonMethods.generateGooBlood(this, this.blockPosition(), 0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (int i = 0; i < getAcidDiameter(); i++) {
                    int x = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    int z = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    if (source != damageSources().genericKill() || source != damageSources().generic()) {
                        GigCommonMethods.generateGooBlood(this, this.blockPosition(), x, z);
                    }
                }
            }
        }
        return super.hurt(source, amount);
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
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.stalkerConfigs.stalkerTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
        }
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

}
