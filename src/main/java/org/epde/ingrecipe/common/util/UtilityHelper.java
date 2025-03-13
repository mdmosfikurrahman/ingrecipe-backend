package org.epde.ingrecipe.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UtilityHelper {

    private static final ZoneId BD_ZONE_ID = ZoneId.of("Asia/Dhaka");
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM, yyyy hh:mm:ss a");

    /**
     * Convert Date to LocalDateTime in BD time zone.
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(BD_ZONE_ID).toLocalDateTime();
    }

    /**
     * Convert LocalDateTime to Date in BD time zone.
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(BD_ZONE_ID).toInstant());
    }

    /**
     * Get current LocalDateTime in BD time zone.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(BD_ZONE_ID);
    }

    /**
     * Format LocalDateTime in BD time zone.
     */
    public static String formatDateToActualFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * Format Date in BD time zone.
     */
    public static String formatDateBDTime(Date date) {
        return formatDateToActualFormat(toLocalDateTime(date));
    }

    /**
     * Capitalize the first letter of a string and convert the rest to lowercase.
     */
    public static String stringCapitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
