package kr.lineedu.lms.utils;

import org.slf4j.Logger;

public class MakeLogWithDetails {

    public static void warn(String message, String details, Logger logger) {

        logger.warn("{} Check details => {}", message, details);
    }

    public static void error(String message, String details, Logger logger) {
        logger.error("{} Check details => {}", message, details);
    }

    public static void info(String message, String details, Logger logger) {
        logger.info("{} Check details => {}", message, details);
    }
}
