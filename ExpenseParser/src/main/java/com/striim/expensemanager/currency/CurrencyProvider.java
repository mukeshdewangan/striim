package com.striim.expensemanager.currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// TODO: Implement a real currency converter using an API or database
// TODO: Add a method to get the currency value for a given date
// Currently it supports some commonly used currency pairs
public class CurrencyProvider {
    private static CurrencyProvider instance = new CurrencyProvider();

    public static CurrencyProvider getInstance() {
        return instance;
    }

    private CurrencyProvider () {}

    public double getCurrencyValue(CurrencyCode sourceCurrencyCode, CurrencyCode targetCurrencyCode, LocalDateTime dateTime) {
        // Dummy currency converter ignoring the datetime part
        if(sourceCurrencyCode == targetCurrencyCode) return 1.0;
        String pair = sourceCurrencyCode.toString() + "-" + targetCurrencyCode.toString();

        switch (pair) {
            case "USD-INR":
                return BigDecimal.valueOf(82.00).doubleValue();
            case "INR-USD":
                return BigDecimal.valueOf(0.012).doubleValue();
            case "EUR-USD":
                return BigDecimal.valueOf(1.07).doubleValue();
            case "USD-EUR":
                return BigDecimal.valueOf(0.93).doubleValue();
            case "INR-EUR":
                return BigDecimal.valueOf(0.011).doubleValue();
            case "EUR-INR":
                return BigDecimal.valueOf(89.00).doubleValue();
            default:
                throw new IllegalArgumentException("Unsupported currency pair: " + pair);
        }
    }
}
