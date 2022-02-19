package com.bvanseg.gigeresque.common.util;

import java.text.NumberFormat;

public final class NumberFormatter {
    private NumberFormatter() {
    }

    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static String format(long number) {
        return numberFormat.format(number);
    }
}
