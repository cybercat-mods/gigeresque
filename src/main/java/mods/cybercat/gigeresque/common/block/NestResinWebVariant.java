package mods.cybercat.gigeresque.common.block;

import net.minecraft.util.StringRepresentable;

public enum NestResinWebVariant implements StringRepresentable {
    ONE("one"), TWO("two"), THREE("three"), FOUR("four"), FIVE("five"), SIX("six");

    public final String dirName;

    NestResinWebVariant(String dirName) {
        this.dirName = dirName;
    }

    @Override
    public String getSerializedName() {
        return dirName;
    }
}
