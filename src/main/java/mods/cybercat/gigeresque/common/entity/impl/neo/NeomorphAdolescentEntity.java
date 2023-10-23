package mods.cybercat.gigeresque.common.entity.impl.neo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azuredoom.bettercrawling.common.ClimberLookController;
import mod.azuredoom.bettercrawling.common.ClimberMoveController;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.CrawlerAdultAlien;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.List;

public class NeomorphAdolescentEntity extends CrawlerAdultAlien implements GeoEntity, SmartBrainOwner<NeomorphAdolescentEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public NeomorphAdolescentEntity(EntityType<? extends CrawlerAdultAlien> entityType, Level world) {
        super(entityType, world);
        this.setMaxUpStep(0.1f);
        this.vibrationUser = new AzureVibrationUser(this, (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? 1.3F : 2.65F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.neomorph_adolescentXenoHealth).add(Attributes.ARMOR, 0.0f).add(Attributes.ARMOR_TOUGHNESS, 6.0).add(Attributes.KNOCKBACK_RESISTANCE, 7.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.13000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.neomorph_adolescentAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
    }

    @Override
    public void travel(Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : new ClimberMoveController<>(this);
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) ? swimLookControl : new ClimberLookController<>(this);

        if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            moveRelative(getSpeed(), movementInput);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.6));
            if (getTarget() == null) {
                setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else
            super.travel(movementInput);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof LivingEntity livingEntity && !this.level().isClientSide)
            if (this.getRandom().nextInt(0, 10) > 7) {
                livingEntity.hurt(damageSources().mobAttack(this), this.getRandom().nextInt(4) > 2 ? Gigeresque.config.runnerXenoTailAttackDamage : 0.0f);
                this.heal(1.0833f);
                return super.doHurtTarget(target);
            }
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return Gigeresque.config.chestbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return Constants.TPD / 2.0f;
    }

    @Override
    public LivingEntity growInto() {
        return Entities.NEOMORPH.create(level());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt) {
        if (spawnReason != MobSpawnType.NATURAL)
            setGrowth(0);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

    @Override
    public List<ExtendedSensor<NeomorphAdolescentEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(), new NearbyLivingEntitySensor<NeomorphAdolescentEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self)), new NearbyBlocksSensor<NeomorphAdolescentEntity>().setRadius(7), new NearbyRepellentsSensor<NeomorphAdolescentEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new NearbyLightsBlocksSensor<NeomorphAdolescentEntity>().setRadius(7).setPredicate((block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(), new UnreachableTargetSensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<NeomorphAdolescentEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(3.5F), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<NeomorphAdolescentEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle())), new FirstApplicableBehaviour<NeomorphAdolescentEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())), new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(1.05f), new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<NeomorphAdolescentEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this)), new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 2.5f), new AlienMeleeAttack(10));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "livingController", 0, event -> {
                    var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                    var velocityLength = this.getDeltaMovement().horizontalDistance();
                    if (velocityLength >= 0.000000001 && !this.isCrawling() && this.isExecuting() == false && !isDead && this.isPassedOut() == false && !this.swinging)
                        if (!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) && this.isExecuting() == false) {
                            if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
                                return event.setAndContinue(GigAnimationsDefault.RUN);
                            else if (this.isExecuting() == false && walkAnimation.speedOld < 0.35F || (!this.isCrawling() && !this.onGround()))
                                return event.setAndContinue(GigAnimationsDefault.WALK);
                        } else if (this.wasEyeInWater && this.isExecuting() == false && !this.isVehicle())
                            if (this.isAggressive() && !this.isVehicle())
                                return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                            else
                                return event.setAndContinue(GigAnimationsDefault.SWIM);
                    return event.setAndContinue((this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8) ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
                }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                        .triggerableAnim("idle", GigAnimationsDefault.IDLE_LAND) // idle
                        .setSoundKeyframeHandler(event -> {
                            if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                                if (this.level().isClientSide)
                                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD, SoundSource.HOSTILE, 0.5F, 2.6F, true);
                            if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
                                if (this.level().isClientSide)
                                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP, SoundSource.HOSTILE, 0.5F, 1.5F, true);
//			if (event.getKeyframeData().getSound().matches("idleSoundkey"))
//				if (this.level().isClientSide)
//					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.5F, true);
                        })).add(new AnimationController<>(this, "attackController", 1, event -> {
                    return PlayState.STOP;
                }).triggerableAnim("alert", RawAnimation.begin().then("hiss", LoopType.PLAY_ONCE)) // reset hands
                        .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                        .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                        .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                        .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                        .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                        .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                        .setSoundKeyframeHandler(event -> {
                            if (event.getKeyframeData().getSound().matches("clawSoundkey"))
                                if (this.level().isClientSide)
                                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                            if (event.getKeyframeData().getSound().matches("tailSoundkey"))
                                if (this.level().isClientSide)
                                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
                        }))
                .add(new AnimationController<>(this, "hissController", 0, event -> {
                    var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                    if (this.entityData.get(IS_HISSING) == true && !this.isVehicle() && this.isExecuting() == false && !isDead && !(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8))
                        return event.setAndContinue(GigAnimationsDefault.HISS);
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("hissSoundkey"))
                        if (this.level().isClientSide)
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS, SoundSource.HOSTILE, 1.0F, 1.0F, true);
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
