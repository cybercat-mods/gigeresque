package mods.cybercat.gigeresque.common.item.animator;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import mods.cybercat.gigeresque.Constants;

public class TrackerAnimationDispatcher {

    private final AzCommand START_OPEN_IDLE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "opening", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand OPEN_IDLE_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "open_idle", AzPlayBehaviors.LOOP);

    private final AzCommand OPENING_COMMAND = AzCommand.compose(START_OPEN_IDLE_COMMAND, OPEN_IDLE_COMMAND);

    private final AzCommand STARTING_CLOSING_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "closing", AzPlayBehaviors.PLAY_ONCE);

    private final AzCommand CLOSED_COMMAND = AzCommand.create(Constants.BASE_CONTROLLER, "closed", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    private final AzCommand CLOSING_COMMAND = AzCommand.compose(STARTING_CLOSING_COMMAND, CLOSED_COMMAND);

    public TrackerAnimationDispatcher() {}

    public void sendOpeningAnimation(Entity entity, ItemStack itemStack) {
        OPENING_COMMAND.sendForItem(entity, itemStack);
    }

    public void sendClosingAnimation(Entity entity, ItemStack itemStack) {
        CLOSING_COMMAND.sendForItem(entity, itemStack);
    }
}
