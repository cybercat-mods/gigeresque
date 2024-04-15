package mods.cybercat.gigeresque.common.entity.impl.extra;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.bettercrawling.entity.movement.ClimberLookController;
import mod.azure.bettercrawling.entity.movement.ClimberMoveController;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienProjectileAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.CrawlerAlien;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpitterEntity extends CrawlerAlien implements GeoEntity, SmartBrainOwner<SpitterEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    public int breakingCounter = 0;

    public SpitterEntity(EntityType<? extends CrawlerAlien> entityType, Level world) {
        super(entityType, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.3F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                Gigeresque.config.spitterXenoHealth).add(Attributes.ARMOR, Gigeresque.config.spitterXenoArmor).add(
                Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE,
                16.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE,
                Gigeresque.config.spitterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
                    var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                    if (event.isMoving() && !this.isCrawling() && !isDead) {
                        if (this.onGround() && !this.wasEyeInWater) {
                            if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
                                return event.setAndContinue(GigAnimationsDefault.RUN);
                            else return event.setAndContinue(GigAnimationsDefault.WALK);
                        } else if (this.wasEyeInWater && !this.isVehicle()) if (this.isAggressive() && !this.isVehicle()) {
                            return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                        } else {
                            return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
                        }
                    } else if (this.isCrawling() && !this.isVehicle() && !this.isInWater())
                        return event.setAndContinue(GigAnimationsDefault.CRAWL);
                    return event.setAndContinue(
                            this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE);
                }).setSoundKeyframeHandler(event -> {
                            if (this.level().isClientSide) {
                                if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP,
                                            SoundSource.HOSTILE, 0.5F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("handstepSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP,
                                            SoundSource.HOSTILE, 0.5F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("ambientSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT,
                                            SoundSource.HOSTILE, 1.0F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD,
                                            SoundSource.HOSTILE, 1.0F, 1.0F, true);
                            }
                        }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                        .triggerableAnim("idle", GigAnimationsDefault.IDLE)) // idle
                .add(new AnimationController<>(this, "attackController", 0, event -> {
                    if (event.getAnimatable().isPassedOut())
                        return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
                    return PlayState.STOP;
                }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                        .triggerableAnim("acidspit", GigAnimationsDefault.SPIT) // spit
                        .triggerableAnim("acidspray", GigAnimationsDefault.SPRAY) // spray
                        .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                        .triggerableAnim("swipe_left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                        .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                        .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                        .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                        .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                        .setSoundKeyframeHandler(event -> {
                            if (this.level().isClientSide) {
                                if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                                            GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("tailSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                                            GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                                if (event.getKeyframeData().getSound().matches("crunchSoundkey"))
                                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                                            GigSounds.ALIEN_CRUNCH, SoundSource.HOSTILE, 1.0F, 1.0F, true);
                            }
                        })).add(new AnimationController<>(this, "hissController", 0, event -> {
                    var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                    if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead)
                        return event.setAndContinue(GigAnimationsDefault.HISS);
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
    public List<ExtendedSensor<SpitterEntity>> getSensors() {
        return ObjectArrayList.of(
                // Player Sensor
                new NearbyPlayersSensor<>(),
                // Living Sensor
                new NearbyLivingEntitySensor<SpitterEntity>().setPredicate(GigEntityUtils::entityTest),
                // Block Sensor
                new NearbyBlocksSensor<SpitterEntity>().setRadius(7),
                // Fire Sensor
                new NearbyRepellentsSensor<SpitterEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                // Lights Sensor
                new NearbyLightsBlocksSensor<SpitterEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)),
                // Nest Sensor
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                // Looks at target
                new LookAtTarget<>(),
                // Flee Fire
                new FleeFireTask<>(1.3F), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                // Kill Lights
                new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
                // Do first
                new FirstApplicableBehaviour<SpitterEntity>(
                        // Targeting
                        new TargetOrRetaliate<>().stopIf(
                                target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
                        // Look at players
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())).stopIf(
                                entity -> this.isPassedOut() || this.isExecuting()),
                        // Look around randomly
                        new SetRandomLookTarget<>().startCondition(
                                entity -> !this.isPassedOut() || !this.isSearching())).stopIf(
                        entity -> this.isPassedOut() || this.isExecuting()),
                // Random
                new OneRandomBehaviour<>(
                        // Randomly walk around
                        new SetRandomWalkTarget<>().speedModifier(1.15f),
                        // Idle
                        new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(
                                entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                // Invalidate Target
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
                // Walk to Target
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5F),
                // Xeno Acid Spit
                new AlienProjectileAttack<>(18),
                // Xeno attacking
                new AlienMeleeAttack<>(5));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isInWater())
            this.setIsCrawling(
                    this.horizontalCollision || !this.level().getBlockState(this.blockPosition().below()).isSolid());

        if (level().getBlockState(this.blockPosition()).is(GigBlocks.ACID_BLOCK))
            this.level().removeBlock(this.blockPosition(), false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt) {
        if (spawnReason != MobSpawnType.NATURAL) setGrowth(getMaxGrowth());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        return this.getBbWidth() * 3.0f * (this.getBbWidth() * 3.0f) + livingEntity.getBbWidth();
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
        double d = this.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        return d <= this.getMeleeAttackRangeSqr(livingEntity);
    }

    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        if (this.wasEyeInWater) return EntityDimensions.scalable(3.0f, 1.0f);
        if (this.isCrawling()) return EntityDimensions.scalable(0.9f, 0.9f);
        return EntityDimensions.scalable(0.9f, 1.9f);
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : new ClimberMoveController<>(this);
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimLookControl : new ClimberLookController<>(this);

        if (this.tickCount % 10 == 0) this.refreshDimensions();

        if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            moveRelative(getSpeed(), movementInput);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.5));
            if (getTarget() == null) setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
        } else super.travel(movementInput);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom().nextInt(0,
                10) > 7) {
            if (livingEntity instanceof Player playerEntity) {
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
                    this.getRandom().nextInt(4) > 2 ? Gigeresque.config.stalkerTailAttackDamage : 0.0f);
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper) creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    public void shootAcid(LivingEntity target, LivingEntity attacker) {
        if (attacker.hasLineOfSight(target)) {
            BehaviorUtils.lookAtEntity(this, target);
            // Calculate the line of sight between the entities
            var attackerPos = attacker.position();
            var targetPos = target.position();
            var direction = targetPos.subtract(attackerPos).normalize();

            // Spawn acid particles along the line of sight and play the sound
            for (var i = 1; i < Mth.floor(direction.length()) + 7; ++i) {
                var vec34 = this.position().add(0.0, 1.3f, 0.0).add(direction.scale(i));
                if (this.level() instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(Particles.ACID, vec34.x, vec34.y, vec34.z, 1, 0, 0, 0, 0);
            }
            this.playSound(SoundEvents.LAVA_EXTINGUISH, 3.0f, 1.0f);

            // Damage the target entity/shield if used
            if (!target.getUseItem().is(Items.SHIELD))
                target.hurt(GigDamageSources.of(this.level(), GigDamageSources.ACID), 2.0f);
            if (target.getUseItem().is(Items.SHIELD) && target instanceof Player player)
                target.getUseItem().hurtAndBreak(10, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

}
