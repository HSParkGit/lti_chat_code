package kr.lineedu.lms.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OnlineAttendanceState {

	ABSENCE("결석", 0),
	LATE("지각", 1),
	ATTENDANCE("출석", 2),
	EXCUSED_ABSENCE("공결", 3),
	NO_PANOPTO_ITEM("파놉토 출결정보 없음", 4); // 차시에 파놉토가 없는경우

	//TODO 동영상이 출결반영 여부 x이면 여기 조건 하나 추가해야할듯..



    private String name;
	private Integer number;

	public static Integer toInteger(OnlineAttendanceState attendanceState) {
		return Arrays.stream(OnlineAttendanceState.values())
			.filter(v -> Objects.equals(v.getName(), attendanceState.getName()))
			.findFirst()
			.map(OnlineAttendanceState::getNumber)
			.orElseGet(null);
	}

	public static OnlineAttendanceState toEnum(Integer i) {
		return Arrays.stream(OnlineAttendanceState.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}

    public static OnlineAttendanceState toEnum(Character zoomAttendance) {
        return zoomAttendance == 'Y' ? OnlineAttendanceState.ATTENDANCE : OnlineAttendanceState.ABSENCE;
    }

}
