package com.bvanseg.gigeresque.interfacing;

/**
 * @author Boston Vanseghi
 */
public interface Eggmorphable {
    int getTicksUntilEggmorphed();

    void setTicksUntilEggmorphed(int ticksUntilEggmorphed);

    default boolean isEggmorphing() {
        return getTicksUntilEggmorphed() >= 0;
    }

    default boolean isNotEggmorphing() {
        return !isEggmorphing();
    }

    default void resetEggmorphing() {
        setTicksUntilEggmorphed(-1);
    }
}
