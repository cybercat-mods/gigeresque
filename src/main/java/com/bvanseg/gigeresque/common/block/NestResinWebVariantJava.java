package com.bvanseg.gigeresque.common.block;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum NestResinWebVariantJava implements StringIdentifiable {
    ONE("one"),
    TWO("two"),
    THREE("three"),
    FOUR("four"),
    FIVE("five"),
    SIX("six");

    public final String dirName;

    NestResinWebVariantJava(String dirName) {
        this.dirName = dirName;
    }

    @Override
    public String asString() {
        return dirName;
    }
}
