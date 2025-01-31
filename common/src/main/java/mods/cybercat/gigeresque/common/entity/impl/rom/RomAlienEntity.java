package mods.cybercat.gigeresque.common.entity.impl.rom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.UnreachableTargetSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.SplittableRandom;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigNav;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyNestBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienHeadBiteTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EnterStasisTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.HissingTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.SearchTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FindDarknessTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class RomAlienEntity extends AlienEntity implements SmartBrainOwner<RomAlienEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public RomAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
        super(type, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.5f);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    /*
     * TODO: replace classic configs with rom ones
     */
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.classicXenoConfigs.classicXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.classicXenoConfigs.classicXenoArmor)
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
                CommonMod.config.classicXenoConfigs.classicXenoAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if (this.wasEyeInWater)
            return EntityDimensions.scalable(3.0f, 1.0f);
        if (this.isTunnelCrawling() || this.isCrawling())
            return EntityDimensions.scalable(0.95f, 0.95f);
        return EntityDimensions.scalable(0.9f, 2.9f);
    }

    @Override
    public void tick() {
        super.tick();
        GigEntityUtils.breakBlocks(this);
        if (!this.isVehicle())
            this.setIsExecuting(false);
        if (this.isExecuting())
            this.setPassedOutStatus(false);
        if (this.isPassedOut() && this.getNavigation() instanceof GigNav gigNav)
            gigNav.hardStop();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onClimbable() {
        var blockPos = new BlockPos.MutableBlockPos(this.position().x, this.position().y + 2.0, this.position().z);
        if (this.level().getBlockState(blockPos).blocksMotion()) {
            this.inTwoBlockSpace = true;
        }
        if (!this.level().getBlockState(blockPos).blocksMotion()) {
            this.inTwoBlockSpace = false;
        }
        return this.inTwoBlockSpace && !this.isInWater();
    }

    /*
     * TODO: replace classic configs with rom ones
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.classicXenoConfigs.alienGrowthMultiplier;
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
                    ? CommonMod.config.classicXenoConfigs.classicXenoTailAttackDamage
                    : (float) CommonMod.config.classicXenoConfigs.classicXenoAttackDamage
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper)
            creeper.hurt(GigDamageSources.of(this.level(), GigDamageSources.XENO), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    @Override
    public boolean isPathFinding() {
        return false;
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
    public List<ExtendedSensor<RomAlienEntity>> getSensors() {
        return ObjectArrayList.of(
            // Player Sensor
            new NearbyPlayersSensor<>(),
            // Living Sensor
            new NearbyLivingEntitySensor<RomAlienEntity>().setRadius(32)
                .setPredicate(GigEntityUtils::entityTest),
            // Block Sensor
            new NearbyBlocksSensor<RomAlienEntity>().setRadius(7),
            // Fire Sensor
            new NearbyRepellentsSensor<RomAlienEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS)
                ),
            // Lights Sensor
            new NearbyLightsBlocksSensor<RomAlienEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            // Nest Sensor
            new NearbyNestBlocksSensor<RomAlienEntity>().setRadius(30)
                .setPredicate(
                    (block, entity) -> block.is(GigBlocks.NEST_RESIN_WEB_CROSS.get())
                ),
            new UnreachableTargetSensor<>(),
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<RomAlienEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Flee Fire
            new FleeFireTask<RomAlienEntity>(3.5F).whenStarting(
                entity -> entity.setFleeingStatus(true)
            )
                .whenStopping(entity -> entity.setFleeingStatus(false))
                .startCondition(romAlienEntity -> !romAlienEntity.isPassedOut())
                .stopIf(AlienEntity::isPassedOut),
            // Take target to nest
            // new EggmorpthTargetTask<>().startCondition(entity -> this.isVehicle() || !this.isPassedOut())
            // .stopIf(entity -> !this.isVehicle() || this.isPassedOut()),
            // Looks at target
            new LookAtTarget<>().stopIf(entity -> this.isPassedOut() || this.isExecuting() || this.isAggressive())
                .startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching() || !this.isExecuting()
                ),
            // Hisses
            new HissingTask<>(800).startCondition(entity -> !this.isAggressive())
                .stopIf(entity -> this.isPassedOut() || this.isExecuting() || this.isAggressive()),
            // Searches
            new SearchTask<>(6000).startCondition(entity -> !this.isAggressive())
                .stopIf(entity -> this.isPassedOut() || this.isExecuting() || this.isAggressive()),
            // Headbite
            new AlienHeadBiteTask<>(this.isBiting() ? 44 : 760),
            // Move to target
            new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting())
                .stopIf(entity -> this.isPassedOut() || this.isExecuting())
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<RomAlienEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Build Nest
            new BuildNestTask<>(90).startCondition(
                entity -> !this.isAggressive() || !this.isPassedOut() || !this.isExecuting() || !this.isFleeing() || !this.isCrawling()
                    || !this.isTunnelCrawling() || !this.isVehicle()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())
                ),
            // Kill Lights
            new KillLightsTask<>().startCondition(
                entity -> !this.isAggressive() || !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())
                ),
            // Find Darkness
            new FindDarknessTask<RomAlienEntity>().stopIf(Mob::isAggressive),
            // Do first
            new FirstApplicableBehaviour<RomAlienEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isVehicle() || this.isFleeing() || this.isPassedOut())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                ).stopIf(entity -> this.isPassedOut() || this.isExecuting()),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching()
                )
            ).stopIf(entity -> this.isPassedOut() || this.isExecuting() || this.isAggressive()),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater()
                    .setRadius(20)
                    .speedModifier(1.2f)
                    .startCondition(
                        entity -> !this.isPassedOut() || !this.isExecuting() || !this.isAggressive() || !this.isVehicle()
                    )
                    .stopIf(
                        entity -> this.isExecuting() || this.isPassedOut() || this.isAggressive() || this.isVehicle()
                    ),
                new EnterStasisTask<>(6000).startCondition(entity -> !this.isAggressive() || !this.isVehicle())
            ).stopIf(entity -> entity.getDeltaMovement().horizontalDistance() > 0 || this.isVehicle())
        );
    }

    @Override
    public BrainActivityGroup<RomAlienEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target) || this.isPassedOut()),
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5f).stopIf(entity -> this.isPassedOut() || this.isVehicle())
            // new ClassicXenoMeleeAttackTask<>(5).stopIf(entity -> this.isPassedOut() || this.isExecuting() ||
            // this.isVehicle())
        );
    }

    @Override
    public void positionRider(@NotNull Entity entity, @NotNull MoveFunction moveFunction) {
        if (entity instanceof LivingEntity mob) {
            var random = new SplittableRandom();
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, true, true));
            var f = Mth.sin(this.yBodyRot * ((float) Math.PI / 180));
            var g = Mth.cos(this.yBodyRot * ((float) Math.PI / 180));
            var y1 = random.nextFloat(0.14F, 0.15F);
            var y3 = random.nextFloat(0.44F, 0.45F);
            var y = random.nextFloat(0.74F, 0.75f);
            var y2 = random.nextFloat(1.14F, 1.15f);
            mob.setPos(
                this.getX() + ((this.isExecuting() ? -2.4f : -1.85f) * f),
                this.getY() + (this.isExecuting() ? (mob.getBbHeight() < 1.4 ? y2 : y) : (mob.getBbHeight() < 1.4 ? y3 : y1)),
                this.getZ() - ((this.isExecuting() ? -2.4f : -1.85f) * g)
            );
            mob.yBodyRot = this.yBodyRot;
            mob.setSpeed(0);
        }
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !this.isCrawling() && !isDead && this.isInWater() && !this.isExecuting() && !this.isVehicle())
                return event.setAndContinue(GigAnimationsDefault.SWIM);
            if (
                event.isMoving() && !this.isCrawling() && !isDead && !this.isInWater() && !this.isTunnelCrawling() && this.isAggressive()
                    && !this.isVehicle()
            )
                return event.setAndContinue(GigAnimationsDefault.RUN);
            if (
                event.isMoving() && !this.isCrawling() && !isDead && !this.isInWater() && !this.isTunnelCrawling() && !this.isAggressive()
                    && !this.isVehicle()
            )
                return event.setAndContinue(GigAnimationsDefault.WALK);
            if (event.isMoving() && !this.isCrawling() && !isDead && !this.isInWater() && !this.isTunnelCrawling() && this.isVehicle())
                return event.setAndContinue(GigAnimationsDefault.WALK_CARRYING);
            if (!this.isVehicle() && this.isCrawling() && !isDead && !this.isInWater())
                return event.setAndContinue(GigAnimationsDefault.CRAWL);
            if (!this.isVehicle() && this.isTunnelCrawling() && !isDead && !this.isInWater())
                return event.setAndContinue(GigAnimationsDefault.CRAWL);
            if (this.isNoAi() && !isDead)
                return event.setAndContinue(GigAnimationsDefault.STATIS_ENTER);
            if (this.isSearching() && !isDead)
                return event.setAndContinue(GigAnimationsDefault.AMBIENT);
            if (this.isVehicle() && this.isExecuting())
                return event.setAndContinue(GigAnimationsDefault.EXECUTION_GRAB);
            if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead)
                return event.setAndContinue(GigAnimationsDefault.HISS);
            return event.setAndContinue(
                this.isPassedOut()
                    ? GigAnimationsDefault.STATIS_LOOP
                    : this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND
            );
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide) {
                if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_FOOTSTEP.get(),
                            SoundSource.HOSTILE,
                            0.5F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("handstepSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_HANDSTEP.get(),
                            SoundSource.HOSTILE,
                            0.5F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("ambientSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_AMBIENT.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_DEATH_THUD.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("biteSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_HEADBITE.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("crunchSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_CRUNCH.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
            }
        })
            .triggerableAnim("carry", GigAnimationsDefault.EXECUTION_CARRY) // carry
            .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
            .triggerableAnim("grab", GigAnimationsDefault.EXECUTION_GRAB) // grab
            .triggerableAnim("crawl", GigAnimationsDefault.CRAWL) // grab
            .triggerableAnim("idle", GigAnimationsDefault.IDLE_LAND)) // idle
            .add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0, event -> {
                if (event.getAnimatable().isPassedOut())
                    return event.setAndContinue(GigAnimationsDefault.STATIS_LOOP);
                if (this.isPassedOut())
                    return event.setAndContinue(GigAnimationsDefault.STATIS_ENTER);
                if (this.isVehicle() && !this.isExecuting())
                    return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("kidnap"));
                return PlayState.STOP;
            }).triggerableAnim("kidnap", RawAnimation.begin().thenPlayXTimes("kidnap", 1)) // trigger kidnap hands
                .triggerableAnim(
                    "run",
                    RawAnimation.begin().then("run", Animation.LoopType.PLAY_ONCE)
                ) // trigger kidnap hands
                .triggerableAnim(
                    "reset",
                    RawAnimation.begin().then("idle_land", Animation.LoopType.PLAY_ONCE)
                ) // reset
                .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("alert", GigAnimationsDefault.AMBIENT) // reset hands
                .triggerableAnim("passout", GigAnimationsDefault.STATIS_ENTER) // pass out
                .triggerableAnim("passoutloop", GigAnimationsDefault.STATIS_LOOP) // pass out
                .triggerableAnim(
                    "wakeup",
                    GigAnimationsDefault.STATIS_LEAVE.then(
                        this.isInWater() ? "idle_water" : "idle_land",
                        Animation.LoopType.LOOP
                    )
                ) // wake up
                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                .triggerableAnim("execution", GigAnimationsDefault.EXECUTION) // headbite
                .triggerableAnim("swipe_left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                .triggerableAnim("left_claw_basic", GigAnimationsDefault.LEFT_CLAW_BASIC) // attack
                .triggerableAnim("right_claw_basic", GigAnimationsDefault.RIGHT_CLAW_BASIC) // attack
                .triggerableAnim("left_tail_basic", GigAnimationsDefault.LEFT_TAIL_BASIC) // attack
                .triggerableAnim("right_tail_basic", GigAnimationsDefault.RIGHT_TAIL_BASIC) // attack
                .triggerableAnim("grab", GigAnimationsDefault.EXECUTION_GRAB) // grab
                .setSoundKeyframeHandler(event -> {
                    if (this.level().isClientSide) {
                        if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                            this.level()
                                .playLocalSound(
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    GigSounds.ALIEN_CLAW.get(),
                                    SoundSource.HOSTILE,
                                    0.25F,
                                    1.0F,
                                    true
                                );
                        if (event.getKeyframeData().getSound().matches("tailSoundkey"))
                            this.level()
                                .playLocalSound(
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    GigSounds.ALIEN_TAIL.get(),
                                    SoundSource.HOSTILE,
                                    0.25F,
                                    1.0F,
                                    true
                                );
                        if (event.getKeyframeData().getSound().matches("crunchSoundkey"))
                            this.level()
                                .playLocalSound(
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    GigSounds.ALIEN_CRUNCH.get(),
                                    SoundSource.HOSTILE,
                                    1.0F,
                                    1.0F,
                                    true
                                );
                    }
                }))// newline
            .add(new AnimationController<>(this, "hissController", 0, event -> {
                var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead)
                    return event.setAndContinue(GigAnimationsDefault.HISS);
                return PlayState.STOP;
            }).setSoundKeyframeHandler(event -> {
                if (event.getKeyframeData().getSound().matches("hissSoundkey") && this.level().isClientSide)
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_HISS.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
            }).triggerableAnim("hiss", GigAnimationsDefault.HISS));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
