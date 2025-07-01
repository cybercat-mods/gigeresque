package mods.cybercat.gigeresque.bvanseg;

import java.time.Duration;

public class TickUtil {

    public static long toTicks(Duration duration) {
        return (long) (duration.toMillis() / 50.0);
    }
}
