package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PostType {

    NOTIFICATION("공지사항", 0),
    FAQ("faq", 1),
    DATAROOM("자료실", 2),
    MANUAL("메뉴얼", 3),
    NEWS("뉴스", 4);

    private String name;
    private Integer number;

    public static Integer toInteger(PostType attendanceState) {
        return Arrays.stream(PostType.values())
            .filter(v -> Objects.equals(v.getName(), attendanceState.getName()))
            .findFirst()
            .map(PostType::getNumber)
            .orElseGet(null);
    }

    public static PostType toEnum(Integer i) {
        return Arrays.stream(PostType.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }

}
