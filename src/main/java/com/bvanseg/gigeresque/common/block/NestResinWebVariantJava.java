package com.bvanseg.gigeresque.common.block;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum NestResinWebVariantJava implements StringIdentifiable {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX;

    @Override
    public String asString() {
        return name().toLowerCase(Locale.US);
    }
}
