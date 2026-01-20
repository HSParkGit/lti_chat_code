package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum AssignmentState {

    COMPLETE("완료", 0),
    UNCOMPLETE("미완료", 1);

    private String name;
    private Integer number;

    public static Integer toInteger(AssignmentState type) {
        return Arrays.stream(AssignmentState.values())
            .filter(v -> Objects.equals(v.getName(), type.getName()))
            .findFirst()
            .map(AssignmentState::getNumber)
            .orElseGet(null);
    }

    public static AssignmentState toEnum(Integer i) {
        return Arrays.stream(AssignmentState.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }

}
