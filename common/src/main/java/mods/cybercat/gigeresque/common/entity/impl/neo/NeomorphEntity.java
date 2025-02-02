package mods.cybercat.gigeresque.common.entity.impl.neo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
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
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.GigNav;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class NeomorphEntity extends AlienEntity implements SmartBrainOwner<NeomorphEntity> {

    public NeomorphEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.9F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.neomorphConfigs.neomorphXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.neomorphConfigs.neomorphXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                CommonMod.config.neomorphConfigs.neomorphXenoArmor
            )
            .add(
                Attributes.KNOCKBACK_RESISTANCE,
                0.0
            )
            .add(Attributes.FOLLOW_RANGE, 16.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.neomorphConfigs.neomorphAttackDamage + 5
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void tick() {
        super.tick();
        GigEntityUtils.breakBlocks(this);
        if (this.isPassedOut() && this.getNavigation() instanceof GigNav gigNav)
            gigNav.hardStop();
        moveAnalysis.update();

        if (this.level().isClientSide()) {
            this.handleAnimations();
        }
    }

    protected void handleAnimations() {
        if (this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
            return;
        }
        if (this.isHissing() && !this.isPassedOut()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendHiss);
        }
        if (this.moveAnalysis.isMoving()) {
            this.handleMovementAnimations();
        } else {
            this.handleIdleAnimations();
        }
    }

    protected void handleMovementAnimations() {
        if (this.isAggressive()) {
            this.handleAggroMovementAnimations();
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendWalk);
        }
    }

    protected void handleAggroMovementAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRun);
        }
    }

    protected void handleIdleAnimations() {
        if (this.isPassedOut()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendStasisLoop);
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdle);
        }
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 1)
            GigCommonMethods.generateSporeCloud(this, this.blockPosition(), 0, 0);
        super.tickDeath();
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
        return ObjectArrayList.of(
            new NearbyPlayersSensor<>(),
            new NearbyLivingEntitySensor<NeomorphEntity>().setRadius(30).setPredicate(GigEntityUtils::entityTest),
            new NearbyBlocksSensor<NeomorphEntity>().setRadius(7),
            new NearbyRepellentsSensor<NeomorphEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            new NearbyLightsBlocksSensor<NeomorphEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            new UnreachableTargetSensor<>(),
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<NeomorphEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.3F), new MoveToWalkTarget<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<NeomorphEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
            new FirstApplicableBehaviour<NeomorphEntity>(
                new TargetOrRetaliate<>(),
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                ),
                new SetRandomLookTarget<>()
            ),
            new OneRandomBehaviour<>(
                new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(0.55f),
                new Idle<>().startCondition(entity -> !this.isAggressive())
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<NeomorphEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.15F),
            new AlienMeleeAttack<>(10, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR).whenStopping(
                e -> this.addEffect(
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 100, false, false)
                )
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
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.neomorphConfigs.neomorphXenoTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper)
            creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

}
