package kr.lineedu.lms.utils.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class DivisionFeignDto {
    //학과
    private Long id;
    //학부
    private Long parentAccountId;
    private String name;

    @Builder
    public DivisionFeignDto(Long id, Long parentAccountId, String name) {
        this.id = id;
        this.parentAccountId = parentAccountId;
        this.name = name;
    }
}
