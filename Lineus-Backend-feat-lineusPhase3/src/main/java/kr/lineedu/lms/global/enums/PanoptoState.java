package kr.lineedu.lms.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PanoptoState {

    ABSENCE("결석", 0),
    UNCHECKED("미반영", 1),
    LATE("지각", 2),
    ATTENDANCE("출석", 3),
    SYNC("연동중", 4);

    private String name;
    private Integer number;

}
