package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.level.block.entity.BlockEntity;

import mods.cybercat.gigeresque.Constants;

public class StorageDispatcher {

    private final AzCommand CLOSING_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "closing",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final AzCommand OPENING_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "opening",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private final BlockEntity blockEntity;

    public StorageDispatcher(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void sendOpen() {
        OPENING_COMMAND.sendForBlockEntity(blockEntity);
    }

    public void sendClose() {
        CLOSING_COMMAND.sendForBlockEntity(blockEntity);
    }

}
