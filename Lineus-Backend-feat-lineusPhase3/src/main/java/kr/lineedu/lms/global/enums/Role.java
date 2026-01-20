package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Role {

    STUDENT("학생", 0),
    TEACHER("선생님", 1),
    ADMIN("관리자", 2);

    private String name;
    private Integer number;

    public static Integer toInteger(Role type) {
        return Arrays.stream(Role.values())
            .filter(v -> Objects.equals(v.getName(), type.getName()))
            .findFirst()
            .map(Role::getNumber)
            .orElseGet(null);
    }

    public static Role toEnum(Integer i) {
        return Arrays.stream(Role.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }

    public static Role fromString(String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
