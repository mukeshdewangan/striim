package com.striim.expensemanager.date_util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeConverter {

    public static LocalDateTime convertToDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(dateStr + " 00:00",formatter);
        return dateTime;
    }
}
