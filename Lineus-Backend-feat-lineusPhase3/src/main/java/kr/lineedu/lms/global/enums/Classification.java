package kr.lineedu.lms.global.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Classification {

	STUDENT("학습자", 0),
	PROFESSOR("교수자", 1),
	ADMIN("관리자", 2),
    NOTHING("없음", 3);

	private String name;
	private Integer number;

	//Enum 일치하는값
	public static Integer toInteger(Classification classification) {
		return Arrays.stream(Classification.values())
			.filter(v -> Objects.equals(v.getName(), classification.getName()))
			.findFirst()
			.map(Classification::getNumber)
			.orElseGet(null);
	}

	public static Classification toEnum(Integer i) {
		return Arrays.stream(Classification.values())
			.filter(v -> v.getNumber().intValue() == i.intValue())
			.findFirst()
			.orElseGet(null);
	}
}
