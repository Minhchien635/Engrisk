package com.engrisk.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {
    public static final DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    static {
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);
    }

    public static String format(float price) {
        return formatter.format(price);
    }
}
