package mods.cybercat.gigeresque.common.block.petrifiedblocks.entity;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.level.block.entity.BlockEntity;

import mods.cybercat.gigeresque.Constants;

public class PetrifiedDispatcher {

    private static final AzCommand PETRIFIED_COMMAND = AzCommand.create(
        Constants.BASE_CONTROLLER,
        "petrified",
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand STASIS_COMMAND = AzCommand.create(
            Constants.BASE_CONTROLLER,
            "stasis_loop",
            AzPlayBehaviors.LOOP
    );

    private final BlockEntity blockEntity;

    public PetrifiedDispatcher(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void setPetrifiedCommand() {
        PETRIFIED_COMMAND.sendForBlockEntity(blockEntity);
    }

    public void setStasisCommand(){
        STASIS_COMMAND.sendForBlockEntity(blockEntity);
    }
}
