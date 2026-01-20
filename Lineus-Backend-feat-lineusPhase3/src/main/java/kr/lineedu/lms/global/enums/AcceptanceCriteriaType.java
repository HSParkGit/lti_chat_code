package kr.lineedu.lms.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 온라인 출석 인정기준
 * 현재는 하나
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum AcceptanceCriteriaType {

	OVER_ACCEPTANCE_PROGRESS_RATE("동영상 시청 - 인정 진도율 이상", 0);

	private String name;
	private Integer number;

	//Enum 일치하는값
	public static Integer toInteger(AcceptanceCriteriaType type) {
		return Arrays.stream(AcceptanceCriteriaType.values())
			.filter(v -> Objects.equals(v.getName(), type.getName()))
			.findFirst()
			.map(AcceptanceCriteriaType::getNumber)
			.orElseGet(null);
	}

	public static AcceptanceCriteriaType toEnum(Integer i) {
		return Arrays.stream(AcceptanceCriteriaType.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}

}
