package kr.lineedu.lms.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum EvaluationCriteria {

	STUDENT_NUMBER_CRITERIA("학생수", 0),
	STUDENT_RATIO("비율",1);

	private String name;
	private Integer number;

	//Enum 일치하는값
	public static Integer toInteger(EvaluationCriteria evaluationCriteria) {
		return Arrays.stream(EvaluationCriteria.values())
			.filter(v -> Objects.equals(v.getName(), evaluationCriteria.getName()))
			.findFirst()
			.map(EvaluationCriteria::getNumber)
			.orElseGet(null);
	}

	public static EvaluationCriteria toEnum(Integer i) {
		return Arrays.stream(EvaluationCriteria.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}

}
