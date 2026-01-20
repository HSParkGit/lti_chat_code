package kr.lineedu.lms.utils;

import java.time.LocalDateTime;

public class DateTimeUtil {

    private static final int HOURS_TO_ADD = 9;

    public static LocalDateTime addHoursToLocalDateTime(LocalDateTime dateTime) {
        return dateTime.plusHours(HOURS_TO_ADD);
    }

    public static LocalDateTime minusHoursToLocalDateTime(LocalDateTime dateTime) {
        return dateTime.minusHours(HOURS_TO_ADD);
    }
}
