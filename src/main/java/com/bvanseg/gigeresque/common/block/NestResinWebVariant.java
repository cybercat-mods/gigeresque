package com.bvanseg.gigeresque.common.block;

import net.minecraft.util.StringIdentifiable;

public enum NestResinWebVariant implements StringIdentifiable {
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
    public String asString() {
        return dirName;
    }
}
