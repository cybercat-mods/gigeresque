package mods.cybercat.gigeresque.common.entity.impl.neo;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.level.Level;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillCropsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EatFoodTask;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class NeobursterEntity extends RunnerbursterEntity {

    public NeobursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.neobursterConfigs.neobursterXenoHealth
            )
            .add(Attributes.ARMOR, 0.0f)
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
                CommonMod.config.neobursterConfigs.neobursterAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.chestbursterGrowthMultiplier;
    }

    @Override
    public LivingEntity growInto() {
        return GigEntities.NEOMORPH_ADOLESCENT.get().create(level());
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Build Nest
            new EatFoodTask<>(40),
            // Kill Lights
            new KillLightsTask<>(),
            new KillCropsTask<>(),
            // Do first
            new FirstApplicableBehaviour<RunnerAlienEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                )
                    .stopIf(
                        entity -> this.stasisManager.isStasis() || this.isExecuting()
                    ),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching()
                )
            ).stopIf(
                entity -> this.stasisManager.isStasis() || this.isExecuting()
            ),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater()
                    .setRadius(20)
                    .speedModifier(0.7f)
                    .startCondition(
                        entity -> !this.stasisManager.isStasis() || !this.isExecuting() || !this.isAggressive()
                    )
                    .stopIf(
                        entity -> this.isExecuting() || this.stasisManager.isStasis() || this.isAggressive() || this.isVehicle()
                    ),
                // Idle
                new Idle<>().startCondition(entity -> !this.isAggressive())
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.4f)
                .stopIf(entity -> this.stasisManager.isStasis() || this.isVehicle()),
            new AlienMeleeAttack<>(5, GigMeleeAttackSelector.NBUSTER_ANIM_SELECTOR)
        );
    }

}
