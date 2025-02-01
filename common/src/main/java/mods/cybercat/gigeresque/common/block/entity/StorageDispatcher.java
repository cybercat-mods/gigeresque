package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import mods.cybercat.gigeresque.Constants;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StorageDispatcher {

    private final AzCommand CLOSING_COMMAND = AzCommand.create(
            Constants.BASE_CONTROLLER,
            "closing",
            AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand CLOSED_COMMAND = AzCommand.create(
            Constants.BASE_CONTROLLER,
            "closed",
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private AzCommand CLOSING_COMBINED_COMMAND = AzCommand.compose(CLOSING_COMMAND, CLOSED_COMMAND);

    private final AzCommand OPENING_COMMAND = AzCommand.create(
            Constants.BASE_CONTROLLER,
            "opening",
            AzPlayBehaviors.PLAY_ONCE
    );

    private final AzCommand OPENDED_COMMAND = AzCommand.create(
            Constants.BASE_CONTROLLER,
            "opened",
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private AzCommand OPENING_COMBINED_COMMAND = AzCommand.compose(OPENING_COMMAND, OPENDED_COMMAND);

    private final BlockEntity blockEntity;

    public StorageDispatcher(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void sendOpen() {
        OPENING_COMBINED_COMMAND.sendForBlockEntity(blockEntity);
    }

    public void sendClose() {
        CLOSING_COMBINED_COMMAND.sendForBlockEntity(blockEntity);
    }
}
