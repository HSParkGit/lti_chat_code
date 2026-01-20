package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum State {
    ACTIVE("활성", 0),
    INACTIVATED("비활성", 1),
    DELETED("삭제", 2);

    private String name;
    private Integer number;

    public static Integer toInteger(State type) {
        return Arrays.stream(State.values())
            .filter(v -> Objects.equals(v.getName(), type.getName()))
            .findFirst()
            .map(State::getNumber)
            .orElseGet(null);
    }

    public static State toEnum(Integer i) {
        return Arrays.stream(State.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }

}
