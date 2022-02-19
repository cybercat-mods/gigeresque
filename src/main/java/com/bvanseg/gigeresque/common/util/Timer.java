package com.bvanseg.gigeresque.common.util;

public class Timer {
    private Timer() {
    }

    static long measureNanoTime(TimedBlock block) {
        long start = System.nanoTime();
        block.run();
        return System.nanoTime() - start;
    }

    public interface TimedBlock {
        void run();
    }
}
