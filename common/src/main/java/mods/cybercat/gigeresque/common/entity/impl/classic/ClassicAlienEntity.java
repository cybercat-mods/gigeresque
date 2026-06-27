package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
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
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goals.RotateTowardsEntityGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.BreakBlocksGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.DelayedClassicAttackGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.HeadBiteGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.LungeAtTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.DigToTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.DodgeProjectilesGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FindDarknessGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeExplodingCreeperGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFightGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.PatrolForTargetsGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.StrollAroundInWaterGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.nest.BuildNestGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.nest.EggmorphGoal;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class ClassicAlienEntity extends AlienEntity {

    public ClassicAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
        super(type, world, Options.standardAlien(3));
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.5f);
        this.animationSelector = GigMeleeAttackSelector.CLASSIC_ANIM_SELECTOR;
        this.climbingManager.canClimb = true;
    }

    /*
     * TODO: Buff once Rom Alien is ready
     */
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.entityConfigs.classicXenoConfigs.classicXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.entityConfigs.classicXenoConfigs.classicXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                7.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
            .add(
                Attributes.FOLLOW_RANGE,
                32.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.entityConfigs.classicXenoConfigs.classicXenoAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    protected EntityDimensions swimmingDimensions(Pose pose) {
        return EntityDimensions.scalable(2, 1);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        crawlingManager.tick();

        if (!this.isVehicle()) {
            this.setIsExecuting(false);
        }

        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            if (
                target.getInBlockState().is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
                    || target.level()
                        .getBlockState(target.blockPosition())
                        .is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
            ) {
                this.setTarget(null);
                this.setAggressive(false);
            }
        }

        if (this.isVehicle() && this.getFirstPassenger() instanceof Mob passenger) {
            passenger.getNavigation().stop();
            passenger.setTarget(null);
            passenger.setNoAi(true);
        }
        if (this.isVehicle() && this.getFirstPassenger() instanceof Mob passenger) {
            passenger.getNavigation().stop();
            passenger.setTarget(null);
            passenger.setNoAi(true);
        }
        if (this.isVehicle()) {
            this.goalSelector.getAvailableGoals()
                .stream()
                .filter(g -> !(g.getGoal() instanceof EggmorphGoal))
                .forEach(g -> g.stop());
        }

        if (!this.isVehicle()) {
            this.getPassengers().forEach(p -> {
                if (p instanceof Mob mob) {
                    mob.setNoAi(false);
                }
            });
        }

        if (this.isVehicle() && !GigEntityUtils.isTargetHostable(this.getFirstPassenger())) {
            this.ejectPassengers();
        }

        if (
            this.level() instanceof ServerLevel serverLevel && this.isVehicle()
                && this.getInBlockState().is(GigTags.NEST_BLOCKS)
        ) {
            GigEntityUtils.placeInNest(serverLevel, this, this.getFirstPassenger());
        }
    }

    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.entityConfigs.classicXenoConfigs.alienGrowthMultiplier;
    }

    @Nullable
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
                GigDamageSources.of(this.level(), GigDamageSources.XENO),
                this.getRandom().nextInt(4) > 2
                    ? CommonMod.config.entityConfigs.classicXenoConfigs.classicXenoTailAttackDamage
                    : (float) CommonMod.config.entityConfigs.classicXenoConfigs.classicXenoAttackDamage
            );
        }
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EggmorphGoal(this));
        this.goalSelector.addGoal(0, new FleeExplodingCreeperGoal(this));
        this.goalSelector.addGoal(0, new DodgeProjectilesGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(1, new DelayedClassicAttackGoal(this, 1.25F, 5));
        this.goalSelector.addGoal(2, new HeadBiteGoal(this));
        this.goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.05F, 20 * 10, 16).setOnLungeCallback(this::runLungeAnimation));
        this.goalSelector.addGoal(4, new BreakBlocksGoal(this, GigTags.DESTRUCTIBLE_LIGHT, 1.5F));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(5, new DigToTargetGoal(this, 32));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(7, new BuildNestGoal(this));
        this.goalSelector.addGoal(7, new FindDarknessGoal(this)); // TODO: Find Darkness Goal
        this.goalSelector.addGoal(9, new RotateTowardsEntityGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(8, new PatrolForTargetsGoal(this, 0.7));
        this.goalSelector.addGoal(10, new RotateTowardsEntityGoal(this, LivingEntity.class, 15.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AlienEntity.class) {

            @Override
            public boolean canContinueToUse() {
                LivingEntity target = ClassicAlienEntity.this.getTarget();
                if (target != null) {
                    if (
                        target.getInBlockState().is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
                            || target.level()
                                .getBlockState(target.blockPosition())
                                .is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
                    ) {
                        ClassicAlienEntity.this.setTarget(null);
                        return false;
                    }
                }
                return super.canContinueToUse();
            }
        }.setAlertOthers());
        this.targetSelector.addGoal(
            2,
            new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                false,
                target -> this.getHealth() > (this.getMaxHealth() / 2) && !this.stasisManager.isStasis() && GigEntityUtils.isValidTarget(
                    target
                )
            )
        );
    }

    @Override
    public void positionRider(@NotNull Entity entity, @NotNull MoveFunction moveFunction) {
        if (entity instanceof LivingEntity mob) {
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 100, true, true));
            mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1, true, true));
            var f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180));
            var g = Mth.cos(this.yBodyRot * ((float) Math.PI / 180));
            var y1 = 0.14F;
            var y3 = 0.44F;
            var y = 0.74F;
            var y2 = 1.14F;
            mob.setPos(
                this.getX() + ((this.isExecuting() ? -2.4f : -1.85f) * f),
                this.getY() + (this.isExecuting() ? (mob.getBbHeight() < 1.4 ? y2 : y) : (mob.getBbHeight() < 1.4 ? y3 : y1)),
                this.getZ() - ((this.isExecuting() ? -2.4f : -1.85f) * g)
            );
            mob.yBodyRot = this.yBodyRot;

            if (mob instanceof Mob mobEntity) {
                mobEntity.getNavigation().stop();
                mobEntity.setTarget(null);
            }
        }
    }

}
