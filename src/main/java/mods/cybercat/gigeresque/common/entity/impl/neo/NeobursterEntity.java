package mods.cybercat.gigeresque.common.entity.impl.neo;


import mod.azure.azurelib.common.internal.common.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import mod.azure.azurelib.common.internal.common.core.animation.Animation;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationController;
import mod.azure.azurelib.common.internal.common.core.animation.RawAnimation;
import mod.azure.azurelib.common.internal.common.core.object.PlayState;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillCropsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EatFoodTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;

public class NeobursterEntity extends RunnerbursterEntity {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public NeobursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                Gigeresque.config.neobursterXenoHealth).add(Attributes.ARMOR, 0.0f).add(Attributes.ARMOR_TOUGHNESS,
                6.0).add(Attributes.KNOCKBACK_RESISTANCE, 7.0).add(Attributes.FOLLOW_RANGE, 32.0).add(
                Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE,
                Gigeresque.config.neobursterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(
                AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return Gigeresque.config.chestbursterGrowthMultiplier;
    }

    @Override
    public LivingEntity growInto() {
        return Entities.NEOMORPH_ADOLESCENT.create(level());
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new EatFoodTask<>(40), new KillCropsTask<>(),
                new FirstApplicableBehaviour<ChestbursterEntity>(new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<ChestbursterEntity>(new SetRandomWalkTarget<>().speedModifier(0.45f),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(300, 600))));
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().stopIf(target -> !target.isAlive()),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 2.3F), new AnimatableMeleeAttack<>(20));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            var velocityLength = this.getDeltaMovement().horizontalDistance();
            if (velocityLength >= 0.000000001 && !isDead)
                if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUN);
                else return event.setAndContinue(GigAnimationsDefault.WALK);
            else if (this.isBirthed()) return event.setAndContinue(GigAnimationsDefault.BIRTH);
            else return event.setAndContinue(GigAnimationsDefault.IDLE);
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide) {
                if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD,
                            SoundSource.HOSTILE, 0.5F, 2.6F, true);
                if (event.getKeyframeData().getSound().matches("stepSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP,
                            SoundSource.HOSTILE, 0.3F, 1.5F, true);
            }
        }));
        controllers.add(
                new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0,
                        event -> PlayState.STOP).triggerableAnim("eat",
                        GigAnimationsDefault.CHOMP).triggerableAnim("death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
