package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum LanguageType {

    KOREAN("자료실", 0),
    ENGLISH("영어", 1),
    CHINESE("중국어", 2);


    private String name;
    private Integer number;

    public static Integer toInteger(LanguageType languageType) {
        return Arrays.stream(LanguageType.values())
            .filter(v -> Objects.equals(v.getName(), languageType.getName()))
            .findFirst()
            .map(LanguageType::getNumber)
            .orElseGet(null);
    }

    public static LanguageType toEnum(Integer i) {
        return Arrays.stream(LanguageType.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }

}
