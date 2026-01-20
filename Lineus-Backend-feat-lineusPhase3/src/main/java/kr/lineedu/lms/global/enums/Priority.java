package kr.lineedu.lms.global.enums;

import java.util.Arrays;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    public static Priority fromString(String value) {
        return Arrays.stream(Priority.values())
                .filter(p -> p.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid priority: " + value));
    }
}
