package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum AttendanceState {

	ABSENCE("결석", 0),
	EXCUSED_ABSENCE("공결", 1),
	LATE("지각", 2),
	ATTENDANCE("출석", 3);

	//TODO 동영상이 출결반영 여부 x이면 여기 조건 하나 추가해야할듯..



    private String name;
	private Integer number;

	public static Integer toInteger(AttendanceState attendanceState) {
		return Arrays.stream(AttendanceState.values())
			.filter(v -> Objects.equals(v.getName(), attendanceState.getName()))
			.findFirst()
			.map(AttendanceState::getNumber)
			.orElseGet(null);
	}

	public static AttendanceState toEnum(Integer i) {
		return Arrays.stream(AttendanceState.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}

    public static AttendanceState toEnum(Character zoomAttendance) {
        return zoomAttendance == 'Y' ? AttendanceState.ATTENDANCE : AttendanceState.ABSENCE;
    }

}
