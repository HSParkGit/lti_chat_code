package kr.lineedu.lms.utils;

public class StringConverterUtil {
    // Convert snake_case to Title Case
    public static String snakeCaseToTitleCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }
        String[] parts = snakeCase.split("_");
        StringBuilder titleCase = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                titleCase.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return titleCase.toString().trim();
    }

    // Convert Title Case to snake_case
    public static String titleCaseToSnakeCase(String titleCase) {
        if (titleCase == null || titleCase.isEmpty()) {
            return titleCase;
        }
        return titleCase.replaceAll("\\s+", "_").toLowerCase();
    }
}
