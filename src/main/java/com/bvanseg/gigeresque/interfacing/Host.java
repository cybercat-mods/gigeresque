package com.bvanseg.gigeresque.interfacing;

/**
 * @author Boston Vanseghi
 */
public interface Host {
    int getTicksUntilImpregnation();

    void setTicksUntilImpregnation(int ticksUntilImpregnation);

    default boolean hasParasite() {
        return getTicksUntilImpregnation() >= 0;
    }

    default boolean doesNotHaveParasite() {
        return !hasParasite();
    }

    default void removeParasite() {
        setTicksUntilImpregnation(-1);
    }
}
