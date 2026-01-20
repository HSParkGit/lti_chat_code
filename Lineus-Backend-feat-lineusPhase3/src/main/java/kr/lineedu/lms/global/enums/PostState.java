package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PostState {

    OPEN("공개", 0),
    CLOSED("비공개", 1),
    POPUP("팝업여부", 2);

    private String name;
    private Integer number;

    public static Integer toInteger(PostState postState) {
        return Arrays.stream(PostState.values())
            .filter(v -> Objects.equals(v.getName(), postState.getName()))
            .findFirst()
            .map(PostState::getNumber)
            .orElseGet(null);
    }

    public static PostState toEnum(Integer i) {
        return Arrays.stream(PostState.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }
}
