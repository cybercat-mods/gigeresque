package mods.cybercat.gigeresque.interfacing;

/**
 * @author Boston Vanseghi
 */
public interface Eggmorphable {
	float getTicksUntilEggmorphed();

	void setTicksUntilEggmorphed(float ticksUntilEggmorphed);

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
