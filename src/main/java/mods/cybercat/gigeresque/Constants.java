package mods.cybercat.gigeresque;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import mods.cybercat.gigeresque.common.config.GigeresqueConfig;

public class Constants {
	private Constants() {
	}

	public static final int TPS = 20; // Ticks per second
	public static final int TPM = TPS * 60; // Ticks per minute
	public static final int TPD = TPM * 20; // Ticks per day

	public static final int EGGMORPH_DURATION = TPM * 5;

	public static final DateTimeFormatter FILE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss",
			Locale.US);

	public static float getIsolationModeDamageBase() {
		return GigeresqueConfig.isolationMode ? 10_000.0f : 1.0f;
	}
}
