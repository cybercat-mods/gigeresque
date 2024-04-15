package mods.cybercat.gigeresque.common.entity.impl.aqua;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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

public class AquaticAlienEntity extends AdultAlienEntity implements SmartBrainOwner<AquaticAlienEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final GroundPathNavigation landNavigation = new GroundPathNavigation(this, level());
    private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level());
    private final MoveControl landMoveControl = new MoveControl(this);
    private final LookControl landLookControl = new LookControl(this);
    private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f, false);
    private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);

    public AquaticAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
        super(type, world);
        setMaxUpStep(1.0f);

        navigation = swimNavigation;
        moveControl = swimMoveControl;
        lookControl = swimLookControl;
        setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.aquaticXenoHealth).add(Attributes.ARMOR, Gigeresque.config.aquaticXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 9.0).add(Attributes.KNOCKBACK_RESISTANCE, 9.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.2500000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.aquaticXenoAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.85);
    }

    @Override
    public float getGrowthMultiplier() {
        return Gigeresque.config.aquaticAlienGrowthMultiplier;
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : landMoveControl;
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimLookControl : landLookControl;

        if (this.tickCount % 10 == 0) this.refreshDimensions();

        if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            moveRelative(getSpeed(), movementInput);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 0.25 : 0.65));
            if (getTarget() == null) {
                setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else super.travel(movementInput);
    }

    @Override
    public @NotNull PathNavigation createNavigation(@NotNull Level world) {
        return swimNavigation;
    }

    @Override
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluid) {
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return this.wasEyeInWater ? EntityDimensions.scalable(3.0f, 1.0f) : super.getDimensions(pose);
    }

    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt) {
        if (spawnReason != MobSpawnType.NATURAL) setGrowth(getMaxGrowth());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
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
    public List<ExtendedSensor<AquaticAlienEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(), new NearbyLivingEntitySensor<AquaticAlienEntity>().setRadius(30).setPredicate(
                GigEntityUtils::entityTest), new NearbyBlocksSensor<AquaticAlienEntity>().setRadius(7), new NearbyRepellentsSensor<AquaticAlienEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new NearbyLightsBlocksSensor<AquaticAlienEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(), new UnreachableTargetSensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<AquaticAlienEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(0.5F), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<AquaticAlienEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new KillLightsTask<>(), new FirstApplicableBehaviour<AquaticAlienEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(!this.wasTouchingWater ? 0.75F : 1.5f).stopIf(entity -> this.isPassedOut()), new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<AquaticAlienEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)), new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> !this.wasTouchingWater ? 0.95F : 1.5F), new AlienMeleeAttack<>(10));
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom().nextInt(0, 10) > 7) {
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
            livingEntity.hurt(damageSources().mobAttack(this), this.getRandom().nextInt(4) > 2 ? Gigeresque.config.aquaticXenoTailAttackDamage : 0.0f);
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper) creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        return this.getBbWidth() * ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.0f : 3.0f) * (this.getBbWidth() * ((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.0f : 3.0f)) + livingEntity.getBbWidth();
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
        double d = this.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        return d <= this.getMeleeAttackRangeSqr(livingEntity);
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
                    var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                    if (this.isUnderWater() && this.wasTouchingWater) {
                        if (this.isAggressive() && event.isMoving() && !isDead && !this.isPassedOut())
                            return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                        else if (!this.isAggressive() && event.isMoving() && !isDead && !this.isPassedOut())
                            return event.setAndContinue(GigAnimationsDefault.SWIM);
                        else return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
                    } else {
                        if (this.isAggressive() && event.isMoving() && !isDead && !this.isPassedOut())
                            return event.setAndContinue(GigAnimationsDefault.CRAWL_RUSH);
                        else if (!this.isAggressive() && event.isMoving() && !isDead && !this.isPassedOut())
                            return event.setAndContinue(GigAnimationsDefault.CRAWL);
                        else if (isSearching() && !this.isAggressive() && !isDead && !this.isPassedOut())
                            return event.setAndContinue(GigAnimationsDefault.AMBIENT);
                        else if (!this.isPassedOut()) return event.setAndContinue(GigAnimationsDefault.IDLE_LAND2);
                        return PlayState.CONTINUE;
                    }
                }).setSoundKeyframeHandler(event -> {
                            if (this.level().isClientSide) {
                                if (event.getKeyframeData().getSound().matches("stepSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.AQUA_LANDMOVE, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.AQUA_LANDCLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("idleSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                            }
                        }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                        .triggerableAnim("idle", this.isUnderWater() && this.wasTouchingWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND2)) // idle
                .add(new AnimationController<>(this, "attackController", 1, event -> PlayState.STOP).setSoundKeyframeHandler(event -> {
                                    if (this.level().isClientSide) {
                                        if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                                            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                                        if (event.getKeyframeData().getSound().matches("tailSoundkey"))
                                            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                                    }
                                }).triggerableAnim("kidnap", RawAnimation.begin().thenPlayXTimes("kidnap", 4)) // trigger kidnap hands
                                .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                                .triggerableAnim("alert", GigAnimationsDefault.AMBIENT) // reset hands
                                .triggerableAnim("idle", RawAnimation.begin().then("idle_land", LoopType.PLAY_ONCE)) // reset hands
                                .triggerableAnim("passout", GigAnimationsDefault.STATIS_ENTER) // pass out
                                .triggerableAnim("passoutloop", GigAnimationsDefault.STATIS_LOOP) // pass out
                                .triggerableAnim("wakeup", GigAnimationsDefault.STATIS_LEAVE.then(this.isInWater() ? "idle_water" : "idle_land", LoopType.PLAY_ONCE)) // wake up
                                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                                .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                                .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                                .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                                .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                ).add(new AnimationController<>(this, "hissController", 0, event -> {
                    var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                    if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead)
                        return event.setAndContinue(GigAnimationsDefault.HISS);

                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("hissSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS, SoundSource.HOSTILE, 1.0F, 1.0F, true);
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
