package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.level.block.entity.BlockEntity;

import mods.cybercat.gigeresque.Constants;

public class SporeEggDispatcher {

    private static final AzCommand PETRIFIED_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "petrified",
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand IDLE_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "idle",
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand HATCHED_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "hatched_empty",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final BlockEntity sporeBlockEntity;

    public SporeEggDispatcher(BlockEntity sporeBlockEntity) {
        this.sporeBlockEntity = sporeBlockEntity;
    }

    public void sendPetrified() {
        PETRIFIED_COMMAND.sendForBlockEntity(sporeBlockEntity);
    }

    public void sendIdle() {
        IDLE_COMMAND.sendForBlockEntity(sporeBlockEntity);
    }

    public void sendHatched() {
        HATCHED_COMMAND.sendForBlockEntity(sporeBlockEntity);
    }
}
