package org.epde.ingrecipe.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SuppressWarnings("unused")
public class DateTimeUtil {

    private static final ZoneId BD_ZONE = ZoneId.of("Asia/Dhaka");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMMM, yyyy hh:mm:ss a");

    public static LocalDateTime convertToBDT(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(BD_ZONE).toLocalDateTime();
    }

    public static String formatToBDT(LocalDateTime dateTime) {
        return dateTime.atZone(BD_ZONE).format(FORMATTER);
    }

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(BD_ZONE).toLocalDateTime();
    }

    public static Date convertToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(BD_ZONE).toInstant());
    }

    public static LocalDateTime nowBDT() {
        return LocalDateTime.now(BD_ZONE);
    }
}
