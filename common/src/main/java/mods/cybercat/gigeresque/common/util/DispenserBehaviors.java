package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.item.GigItems;

public class DispenserBehaviors {

    public static void initialize() {
        var defaultDispenseItemBehavior = new DefaultDispenseItemBehavior() {

            @Override
            public @NotNull ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                var direction = blockSource.state().getValue(DispenserBlock.FACING);
                var entitytype = ((SpawnEggItem) itemStack.getItem()).getType(itemStack);

                try {
                    entitytype.spawn(
                        blockSource.level(),
                        itemStack,
                        null,
                        blockSource.pos().relative(direction),
                        MobSpawnType.DISPENSER,
                        direction != Direction.UP,
                        false
                    );
                } catch (Exception exception) {
                    CommonMod.LOGGER.error("Error while dispensing spawn egg from dispenser at {}", blockSource.pos(), exception);
                    return ItemStack.EMPTY;
                }

                itemStack.shrink(1);
                blockSource.level().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.pos());
                return itemStack;
            }
        };
        DispenserBlock.registerBehavior(GigItems.AQUATIC_ALIEN_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.AQUATIC_CHESTBURSTER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.EGG_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.FACEHUGGER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.CHESTBURSTER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.ALIEN_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.BAPHOMORPH_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.HELL_BURSTER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.HELLMORPH_RUNNER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.SPITTER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.MUTANT_HAMMERPEDE_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.MUTANT_POPPER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.MUTANT_STALKER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.NEOBURSTER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.NEOMORPH_ADOLESCENT_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.NEOMORPH_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.RUNNER_ALIEN_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.RUNNERBURSTER_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.DRACONICTEMPLEBEAST_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.MOONLIGHTHORRORTEMPLEBEAST_SPAWN_EGG.get(), defaultDispenseItemBehavior);
        DispenserBlock.registerBehavior(GigItems.RAVENOUSTEMPLEBEAST_SPAWN_EGG.get(), defaultDispenseItemBehavior);
    }
}
