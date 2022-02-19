package com.bvanseg.gigeresque.common.util;

import com.bvanseg.gigeresque.common.Gigeresque;

public class InitializationTimer {
    private InitializationTimer() {
    }

    public static void initializingBlock(String initializerName, InitializerBlock block) {
        Gigeresque.LOGGER.info("%s initializing.".formatted(initializerName));
        long timeTaken = Timer.measureNanoTime(block::initialize);
        Gigeresque.LOGGER.info("%s initialized in %sns (%sms).".formatted(initializerName, NumberFormatter.format(timeTaken), NumberFormatter.format(timeTaken / 1_000_000)));
    }

    public interface InitializerBlock {
        public void initialize();
    }
}
