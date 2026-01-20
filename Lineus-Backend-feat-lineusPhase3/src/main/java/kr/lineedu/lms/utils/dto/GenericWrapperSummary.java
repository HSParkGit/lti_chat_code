package kr.lineedu.lms.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericWrapperSummary<T> {

    private T summary;

    public static <T> GenericWrapperSummary<T> of(T summary) {
        return GenericWrapperSummary.<T>builder()
                .summary(summary)
                .build();
    }

}
