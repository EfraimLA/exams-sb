package org.acme.examssb.utils;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.TimeZone;

public class Utils {

    public static boolean isValidTimezone(String timezone) {
        return timezone == null || Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone);
    }

    public static <T extends Temporal> String formatDateTime(T date) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(date);
    }

}
