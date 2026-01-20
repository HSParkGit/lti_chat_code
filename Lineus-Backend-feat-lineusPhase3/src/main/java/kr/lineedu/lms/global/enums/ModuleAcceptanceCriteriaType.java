package kr.lineedu.lms.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 주차 출석 인정 기준
 * -현재는 하나
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ModuleAcceptanceCriteriaType {

	FINISH_ALL_CONTENT_VIDEO("주차내 모든 동영상 상태 완료", 0),
	ALL_ATTENDANCE("모두 출석", 1),
	AT_LEAST_ONE_ATTENDANCE("최소 하나 출석", 2);

	private String name;
	private Integer number;

	//Enum 일치하는값
	public static Integer toInteger(ModuleAcceptanceCriteriaType type) {
		return Arrays.stream(ModuleAcceptanceCriteriaType.values())
			.filter(v -> Objects.equals(v.getName(), type.getName()))
			.findFirst()
			.map(ModuleAcceptanceCriteriaType::getNumber)
			.orElseGet(null);
	}

	public static ModuleAcceptanceCriteriaType toEnum(Integer i) {
		return Arrays.stream(ModuleAcceptanceCriteriaType.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}

}
