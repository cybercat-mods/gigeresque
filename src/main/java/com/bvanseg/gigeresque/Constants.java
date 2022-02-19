package com.bvanseg.gigeresque;

import com.bvanseg.gigeresque.common.Gigeresque;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Constants {
    private Constants(){}

    public static final int TPS = 20; // Ticks per second
    public static final int TPM = TPS * 60; // Ticks per minute
    public static final int TPD = TPM * 20; // Ticks per day

    public static final int ALIEN_SPAWN_CAP = 70;

    public static final int EGGMORPH_DURATION = TPM * 5;

    public static final DateTimeFormatter FILE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss", Locale.US);

    public static float getIsolationModeDamageBase() {
        return Gigeresque.config.features.isolationMode ? 10_000.0f : 1.0f;
    }
}
