package mods.cybercat.gigeresque.common.entity.impl.classic;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.bettercrawling.entity.movement.ClimberLookController;
import mod.azure.bettercrawling.entity.movement.ClimberMoveController;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
import java.util.SplittableRandom;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyNestBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienHeadBiteTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.ClassicXenoMeleeAttackTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.BreakBlocksTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EnterStasisTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.HissingTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.SearchTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.EggmorpthTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FindDarknessTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.CrawlerAlien;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class ClassicAlienEntity extends CrawlerAlien implements SmartBrainOwner<ClassicAlienEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public ClassicAlienEntity(@NotNull EntityType<? extends CrawlerAlien> type, @NotNull Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                Gigeresque.config.classicXenoHealth
            )
            .add(Attributes.ARMOR, Gigeresque.config.classicXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                7.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
            .add(
                Attributes.FOLLOW_RANGE,
                32.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.13000000417232513)
            .add(
                Attributes.ATTACK_DAMAGE,
                Gigeresque.config.classicXenoAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0)
            .add(
                AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE,
                1.0
            );
    }

    @Override
    protected int getAcidDiameter() {
        return 3;
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        this.moveControl = this.isUnderWater()
            ? swimMoveControl
            : this.isCrawling()
                ? new ClimberMoveController<>(
                    this
                )
                : new MoveControl(this);
        this.lookControl = this.isUnderWater()
            ? landLookControl
            : this.isCrawling()
                ? new ClimberLookController<>(
                    this
                )
                : landLookControl;
        this.navigation.setCanFloat(true);
        if (this.tickCount % 10 == 0)
            this.refreshDimensions();

        super.travel(movementInput);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        if (this.wasEyeInWater)
            return EntityDimensions.scalable(3.0f, 1.0f);
        if (this.isTunnelCrawling() || this.isCrawling())
            return EntityDimensions.scalable(0.95f, 0.3f);
        return EntityDimensions.scalable(0.9f, 2.9f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isVehicle())
            this.setIsExecuting(false);
        if (this.isExecuting())
            this.setPassedOutStatus(false);
        this.setIsCrawling(this.getOrientation().pitch() > 10 || this.getOrientation().pitch() < -10);
    }

    @Override
    public float getGrowthMultiplier() {
        return Gigeresque.config.alienGrowthMultiplier;
    }

    @Override
    public SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor world,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnReason,
        SpawnGroupData entityData,
        CompoundTag entityNbt
    ) {
        if (spawnReason != MobSpawnType.NATURAL)
            setGrowth(getMaxGrowth());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
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
                this.getRandom().nextInt(4) > 2 ? Gigeresque.config.classicXenoTailAttackDamage : 0.0f
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
    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        return this.getBbWidth() * ((this.level()
            .getFluidState(this.blockPosition())
            .is(
                Fluids.WATER
            ) && this.level()
                .getFluidState(
                    this.blockPosition()
                )
                .getAmount() >= 8) ? 1.0f : 3.0f) * (this.getBbWidth() * ((this.level()
                    .getFluidState(
                        this.blockPosition()
                    )
                    .is(Fluids.WATER) && this.level()
                        .getFluidState(
                            this.blockPosition()
                        )
                        .getAmount() >= 8) ? 1.0f : 3.0f)) + livingEntity.getBbWidth();
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
        return this.distanceToSqr(
            livingEntity.getX(),
            livingEntity.getY(),
            livingEntity.getZ()
        ) <= this.getMeleeAttackRangeSqr(livingEntity);
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
    public List<ExtendedSensor<ClassicAlienEntity>> getSensors() {
        return ObjectArrayList.of(
            // Player Sensor
            new NearbyPlayersSensor<>(),
            // Living Sensor
            new NearbyLivingEntitySensor<ClassicAlienEntity>().setPredicate(GigEntityUtils::entityTest),
            // Block Sensor
            new NearbyBlocksSensor<ClassicAlienEntity>().setRadius(7),
            // Fire Sensor
            new NearbyRepellentsSensor<ClassicAlienEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS)
                ),
            // Lights Sensor
            new NearbyLightsBlocksSensor<ClassicAlienEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            // Nest Sensor
            new NearbyNestBlocksSensor<ClassicAlienEntity>().setRadius(30)
                .setPredicate(
                    (block, entity) -> block.is(GigBlocks.NEST_RESIN_WEB_CROSS)
                ),
            new UnreachableTargetSensor<>(),
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<ClassicAlienEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Flee Fire
            new FleeFireTask<ClassicAlienEntity>(3.5F).whenStarting(
                entity -> entity.setFleeingStatus(true)
            ).whenStarting(entity -> entity.setFleeingStatus(false)),
            // Take target to nest
            new EggmorpthTargetTask<>().stopIf(
                entity -> this.isFleeing() || this.isCrawling() || !this.isTunnelCrawling() || !this.isVehicle() || this
                    .getFirstPassenger() == null
            ),
            // Looks at target
            new LookAtTarget<>().stopIf(entity -> this.isPassedOut() || this.isExecuting())
                .startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching() || !this.isExecuting()
                ),
            // Hisses
            new HissingTask<>(800),
            // Searches
            new SearchTask<>(6000),
            // Headbite
            new AlienHeadBiteTask<>(this.isBiting() ? 44 : 760),
            // Move to target
            new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting())
                .stopIf(
                    entity -> this.isExecuting()
                )
        );
    }

    @Override
    public BrainActivityGroup<ClassicAlienEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Build Nest
            new BuildNestTask<>(90).startCondition(
                entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing() || !this.isCrawling() || !this.isTunnelCrawling()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())
                ),
            // Kill Lights
            new KillLightsTask<>().startCondition(
                entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())
                ),
            // Break blocks
            new BreakBlocksTask<>(90, true),
            // Do first
            new FirstApplicableBehaviour<ClassicAlienEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                )
                    .stopIf(
                        entity -> this.isPassedOut() || this.isExecuting()
                    ),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching()
                )
            ).stopIf(
                entity -> this.isPassedOut() || this.isExecuting()
            ),
            // Random
            new OneRandomBehaviour<>(
                // Find Darkness
                new FindDarknessTask<>().startCondition(
                    entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing() || !this.isCrawling() || !this
                        .isTunnelCrawling()
                ),
                // Randomly walk around
                new SetRandomWalkTarget<>().speedModifier(1.2f)
                    .startCondition(
                        entity -> !this.isPassedOut() || !this.isExecuting() || !this.isAggressive()
                    )
                    .stopIf(
                        entity -> this.isExecuting() || this.isPassedOut() || this.isAggressive()
                    ),
                // Searches
                new EnterStasisTask<>(6000),
                // Idle
                new Idle<>().startCondition(
                    entity -> (!this.isPassedOut() || !this.isAggressive() || this.isFleeing())
                )
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<ClassicAlienEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            // Invalidate Target
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            // Walk to Target
            new SetWalkTargetToAttackTarget<>().speedMod(
                (owner, target) -> Gigeresque.config.classicXenoAttackSpeed - 1.5f
            )
                .startCondition(
                    entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()
                )
                .stopIf(
                    entity -> this.isPassedOut() || (this.isFleeing() || !this.hasLineOfSight(entity))
                ),
            // Classic Xeno attacking
            new ClassicXenoMeleeAttackTask<>(5).startCondition(
                entity -> !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()
            )
                .stopIf(
                    entity -> (this.isFleeing())
                )
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
                this.getX() + (double) ((this.isExecuting() ? -2.4f : -1.85f) * f),
                this.getY() + (double) (this.isExecuting() ? (mob.getBbHeight() < 1.4 ? y2 : y) : (mob.getBbHeight() < 1.4 ? y3 : y1)),
                this.getZ() - (double) ((this.isExecuting() ? -2.4f : -1.85f) * g)
            );
            mob.yBodyRot = this.yBodyRot;
            mob.xxa = this.xxa;
            mob.zza = this.zza;
            mob.yya = this.yya;
            mob.setSpeed(0);
        }
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !this.isCrawling() && !isDead && (this.wasEyeInWater && !this.isExecuting() && !this.isVehicle())) {
                if (this.isAggressive() && !this.isVehicle())
                    return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                else
                    return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
            }
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
                this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND
            );
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide) {
                if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_FOOTSTEP,
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
                            GigSounds.ALIEN_HANDSTEP,
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
                            GigSounds.ALIEN_AMBIENT,
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
                            GigSounds.ALIEN_DEATH_THUD,
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
                            GigSounds.ALIEN_HEADBITE,
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
                            GigSounds.ALIEN_CRUNCH,
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
                    return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
                if (this.isPassedOut())
                    return event.setAndContinue(GigAnimationsDefault.STATIS_ENTER);
                if (this.isVehicle() && !this.isExecuting())
                    return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("kidnap"));
                return PlayState.STOP;
            }).triggerableAnim("kidnap", RawAnimation.begin().thenPlayXTimes("kidnap", 1)) // trigger kidnap hands
                .triggerableAnim(
                    "run",
                    RawAnimation.begin().then("run", LoopType.PLAY_ONCE)
                ) // trigger kidnap hands
                .triggerableAnim("reset", RawAnimation.begin().then("idle_land", LoopType.PLAY_ONCE)) // reset
                .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("alert", GigAnimationsDefault.AMBIENT) // reset hands
                .triggerableAnim("passout", GigAnimationsDefault.STATIS_ENTER) // pass out
                .triggerableAnim("passoutloop", GigAnimationsDefault.STATIS_LOOP) // pass out
                .triggerableAnim(
                    "wakeup",
                    GigAnimationsDefault.STATIS_LEAVE.then(
                        this.isInWater() ? "idle_water" : "idle_land",
                        LoopType.LOOP
                    )
                ) // wake up
                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
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
                                    GigSounds.ALIEN_CLAW,
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
                                    GigSounds.ALIEN_TAIL,
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
                                    GigSounds.ALIEN_CRUNCH,
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
                            GigSounds.ALIEN_HISS,
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
