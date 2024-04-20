package mods.cybercat.gigeresque.common.block.storage;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum StorageStates implements StringRepresentable {
    OPEN("open"), OPENED("opened"), CLOSE("close"), CLOSED("closed"), CLOSING("closing");

    private final String name;

    StorageStates(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}