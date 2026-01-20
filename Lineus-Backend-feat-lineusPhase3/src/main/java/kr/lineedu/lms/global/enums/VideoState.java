package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum VideoState {

	COMPLETE("완료", 0),
	INCOMPLETE("미완료", 1);

	private String name;
	private Integer number;

	//Enum 일치하는값
	public static Integer toInteger(VideoState videoState) {
		return Arrays.stream(VideoState.values())
			.filter(v -> Objects.equals(v.getName(), videoState.getName()))
			.findFirst()
			.map(VideoState::getNumber)
			.orElseGet(null);
	}

	public static VideoState toEnum(Integer i) {
		return Arrays.stream(VideoState.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}

}
