package com.bvanseg.gigeresque.common.item

import com.bvanseg.gigeresque.Constants
import com.bvanseg.gigeresque.common.config.ConfigAccessor
import com.bvanseg.gigeresque.common.entity.Entities
import com.bvanseg.gigeresque.common.entity.EntityIdentifiers
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntity
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntity
import com.bvanseg.gigeresque.interfacing.Host
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

/**
 * @author Boston Vanseghi
 */
class SurgeryKitItem(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        tryRemoveParasite(user.getStackInHand(hand), user, user, hand)
        return super.use(world, user, hand)
    }

    override fun useOnEntity(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult {
        tryRemoveParasite(stack, user, entity, hand)
        return super.useOnEntity(stack, user, entity, hand)
    }

    private fun tryRemoveParasite(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand) {
        val host = entity as Host
        if (host.hasParasite()) {
            if (!entity.world.isClient) {
                if (host.ticksUntilImpregnation < Constants.TPS * 30) {
                    spawnParasite(entity)
                }

                host.removeParasite()
                stack.damage(1, user) { p -> p.sendToolBreakStatus(hand) }
                entity.removeStatusEffect(StatusEffects.HUNGER)
                entity.removeStatusEffect(StatusEffects.MINING_FATIGUE)
                entity.addStatusEffect(
                    StatusEffectInstance(
                        com.bvanseg.gigeresque.common.status.effect.StatusEffects.TRAUMA,
                        Constants.TPD
                    )
                )
                host.isBleeding = false
            }
        }
    }

    private fun spawnParasite(entity: LivingEntity) {
        val identifier = Registry.ENTITY_TYPE.getId(entity.type)
        val morphMappings = ConfigAccessor.reversedMorphMappings

        val burster = when (morphMappings.getOrDefault(identifier.toString(), EntityIdentifiers.ALIEN.toString())) {
            Entities.RUNNER_ALIEN.toString() -> RunnerbursterEntity(Entities.RUNNERBURSTER, entity.world)
            Entities.AQUATIC_ALIEN.toString() -> AquaticChestbursterEntity(Entities.AQUATIC_CHESTBURSTER, entity.world)
            else -> ChestbursterEntity(Entities.CHESTBURSTER, entity.world)
        }

        burster.hostId = identifier.toString()
        burster.refreshPositionAndAngles(entity.blockPos, entity.yaw, entity.pitch)
        entity.world.spawnEntity(burster)
    }
}