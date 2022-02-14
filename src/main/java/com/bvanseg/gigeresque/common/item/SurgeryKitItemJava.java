package com.bvanseg.gigeresque.common.item;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.config.ConfigAccessorJava;
import com.bvanseg.gigeresque.common.entity.EntitiesJava;
import com.bvanseg.gigeresque.common.entity.EntityIdentifiersJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntityJava;
import com.bvanseg.gigeresque.common.status.effect.StatusEffectsJava;
import com.bvanseg.gigeresque.interfacing.Host;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SurgeryKitItemJava extends Item {
    public SurgeryKitItemJava(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        tryRemoveParasite(user.getStackInHand(hand), user, user, hand);
        return super.use(world, user, hand);
    }


    private void tryRemoveParasite(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        var host = (Host) entity;
        if (host.hasParasite()) {
            if (!entity.world.isClient) {
                if (host.getTicksUntilImpregnation() < ConstantsJava.TPS * 30) {
                    spawnParasite(entity);
                }

                host.removeParasite();
                stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
                entity.removeStatusEffect(StatusEffects.HUNGER);
                entity.removeStatusEffect(StatusEffects.MINING_FATIGUE);
                entity.addStatusEffect(
                        new StatusEffectInstance(
                                StatusEffectsJava.TRAUMA,
                                ConstantsJava.TPD
                        )
                );
                host.setBleeding(false);
            }
        }
    }

    private void spawnParasite(LivingEntity entity) {
        var identifier = Registry.ENTITY_TYPE.getId(entity.getType());
        var morphMappings = ConfigAccessorJava.getReversedMorphMappings();

        String runnerString = EntitiesJava.RUNNER_ALIEN.toString();
        ChestbursterEntityJava burster;

        String orDefault = morphMappings.getOrDefault(identifier.toString(), EntityIdentifiersJava.ALIEN.toString());
        if (runnerString.equals(orDefault)) {
            burster = new RunnerbursterEntityJava(EntitiesJava.RUNNERBURSTER, entity.world);
        } else if (EntitiesJava.AQUATIC_ALIEN.toString().equals(orDefault)) {
            burster = new AquaticChestbursterEntityJava(EntitiesJava.AQUATIC_CHESTBURSTER, entity.world);
        } else {
            burster = new ChestbursterEntityJava(EntitiesJava.CHESTBURSTER, entity.world);
        }

        burster.hostId = identifier.toString();
        burster.refreshPositionAndAngles(entity.getBlockPos(), entity.getYaw(), entity.getPitch());
        entity.world.spawnEntity(burster);
    }
}
