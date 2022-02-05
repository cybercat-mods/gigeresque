package com.bvanseg.gigeresque.common.util;

import com.bvanseg.gigeresque.common.GigeresqueJava;

public class InitializationTimer {
    private InitializationTimer() {
    }

    public static void initializingBlock(String initializerName, InitializerBlock block) {
        GigeresqueJava.LOGGER.info("%s initializing.".formatted(initializerName));
        long timeTaken = Timer.measureNanoTime(block::initialize);
        GigeresqueJava.LOGGER.info("%s initialized in %sns (%sms).".formatted(initializerName, NumberFormatter.format(timeTaken), NumberFormatter.format(timeTaken / 1_000_000)));
    }

    public interface InitializerBlock {
        public void initialize();
    }
}
