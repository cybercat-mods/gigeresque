package mods.cybercat.gigeresque.common.entity.impl.neo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import mod.azure.azurelib.common.api.common.ai.pathing.AzureNavigation;
import mod.azure.azurelib.common.internal.common.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import mod.azure.azurelib.common.internal.common.core.animation.Animation;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationController;
import mod.azure.azurelib.common.internal.common.core.animation.RawAnimation;
import mod.azure.azurelib.common.internal.common.core.object.PlayState;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.BreakBlocksTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NeomorphEntity extends AlienEntity implements SmartBrainOwner<NeomorphEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final AzureNavigation landNavigation = new AzureNavigation(this, level());
    public int breakingCounter = 0;

    public NeomorphEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        setMaxUpStep(1.5f);
        this.vibrationUser = new AzureVibrationUser(this, 1.9F);
        navigation = landNavigation;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                Gigeresque.config.neomorphXenoHealth).add(Attributes.ARMOR, Gigeresque.config.neomorphXenoArmor).add(
                Attributes.ARMOR_TOUGHNESS, Gigeresque.config.neomorphXenoArmor).add(Attributes.KNOCKBACK_RESISTANCE,
                0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(
                Attributes.ATTACK_DAMAGE, Gigeresque.config.neomorphAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : landMoveControl;
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimLookControl : landLookControl;

        if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            moveRelative(getSpeed(), movementInput);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.6));
            if (getTarget() == null) {
                setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            var velocityLength = this.getDeltaMovement().horizontalDistance();
            if (event.getAnimatable().getAttckingState() != 1 && velocityLength >= 0.000000001 && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isExecuting() && !isDead && !this.isPassedOut() && !this.swinging)
                if (!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                        this.blockPosition()).getAmount() >= 8) && !this.isExecuting()) {
                    if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
                        return event.setAndContinue(GigAnimationsDefault.RUN);
                    else if (!this.isExecuting() && walkAnimation.speedOld < 0.35F || (!(this.isCrawling() || this.isTunnelCrawling()) && !this.onGround()))
                        return event.setAndContinue(GigAnimationsDefault.WALK);
                } else if (this.wasEyeInWater && !this.isExecuting() && !this.isVehicle())
                    if (this.isAggressive() && !this.isVehicle())
                        return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                    else return event.setAndContinue(GigAnimationsDefault.SWIM);
            if (event.getAnimatable().getAttckingState() == 1) return PlayState.CONTINUE;
            else return event.setAndContinue(
                    (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                            this.blockPosition()).getAmount() >= 8) ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide) {
                if (event.getKeyframeData().getSound().matches("runstepSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP,
                            SoundSource.HOSTILE, 0.5F, 1.5F, true);
                if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP,
                            SoundSource.HOSTILE, 0.5F, 1.5F, true);
                if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD,
                            SoundSource.HOSTILE, 0.5F, 2.6F, true);
                if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW,
                            SoundSource.HOSTILE, 0.25F, 1.0F, true);
                if (event.getKeyframeData().getSound().matches("tailSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL,
                            SoundSource.HOSTILE, 0.25F, 1.0F, true);
            }
        })).add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 1,
                event -> PlayState.STOP).triggerableAnim("idle",
                        RawAnimation.begin().then("idle_land", Animation.LoopType.PLAY_ONCE)) // reset hands
                .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                .setSoundKeyframeHandler(event -> {
                    if (this.level().isClientSide) {
                        if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW,
                                    SoundSource.HOSTILE, 0.25F, 1.0F, true);
                        if (event.getKeyframeData().getSound().matches("tailSoundkey"))
                            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL,
                                    SoundSource.HOSTILE, 0.25F, 1.0F, true);
                    }
                })).add(new AnimationController<>(this, "hissController", 0, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead && !(this.level().getFluidState(
                    this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                    this.blockPosition()).getAmount() >= 8)) return event.setAndContinue(GigAnimationsDefault.HISS);
            return PlayState.STOP;
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("hissSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS,
                        SoundSource.HOSTILE, 1.0F, 1.0F, true);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
    public List<ExtendedSensor<NeomorphEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<NeomorphEntity>().setPredicate(GigEntityUtils::entityTest),
                new NearbyBlocksSensor<NeomorphEntity>().setRadius(7),
                new NearbyRepellentsSensor<NeomorphEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new NearbyLightsBlocksSensor<NeomorphEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new UnreachableTargetSensor<>(),
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<NeomorphEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.3F), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<NeomorphEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
                new BreakBlocksTask<>(90, true), new FirstApplicableBehaviour<NeomorphEntity>(new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.75f),
                        new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(
                                entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<NeomorphEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5F),
                new AlienMeleeAttack<>(12, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR).whenStopping(
                        e -> this.addEffect(
                                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 100, false, false))));
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        return this.getBbWidth() * 1.9f * (this.getBbWidth() * 1.9f) + livingEntity.getBbWidth();
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
        double d = this.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        return d <= this.getMeleeAttackRangeSqr(livingEntity);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom().nextInt(0,
                10) > 7) {
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
            livingEntity.hurt(damageSources().mobAttack(this),
                    this.getRandom().nextInt(4) > 2 ? Gigeresque.config.runnerXenoTailAttackDamage : 0.0f);
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper) creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

}
