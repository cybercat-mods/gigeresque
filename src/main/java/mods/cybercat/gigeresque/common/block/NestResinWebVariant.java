package mods.cybercat.gigeresque.common.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum NestResinWebVariant implements StringRepresentable {

    ONE("one"),
    TWO("two"),
    THREE("three"),
    FOUR("four"),
    FIVE("five"),
    SIX("six");

    public final String dirName;

    NestResinWebVariant(String dirName) {
        this.dirName = dirName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return dirName;
    }
}
