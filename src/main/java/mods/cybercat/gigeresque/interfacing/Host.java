package mods.cybercat.gigeresque.interfacing;

/**
 * @author Boston Vanseghi
 */
public interface Host {
    float getTicksUntilImpregnation();

    void setTicksUntilImpregnation(float ticksUntilImpregnation);

    default boolean hasParasite() {
        return getTicksUntilImpregnation() >= 0;
    }

    default boolean doesNotHaveParasite() {
        return !hasParasite();
    }

    default void removeParasite() {
        setTicksUntilImpregnation(-1.0f);
    }

    boolean isBleeding();

    void setBleeding(boolean isBleeding);
}
