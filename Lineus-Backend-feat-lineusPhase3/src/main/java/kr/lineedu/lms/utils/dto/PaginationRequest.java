package kr.lineedu.lms.utils.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

@Data
public class PaginationRequest {

    @Min(1)
    private int page = 1;

    @Min(3)
    private int size = 10;

//    private List<String> sort = List.of("id:desc", "name:asc");
    private List<String> sort = new LinkedList<>();

    private final Logger logger = LoggerFactory.getLogger(PaginationRequest.class);

    public Pageable getPageable() {
        Sort sortBy = Sort.by(
                sort.stream().map(sortContent -> {
                    String[] sortInfo = sortContent.split(":");
                    Sort.Direction sortDirection = sortInfo[0].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                    return new Sort.Order(sortDirection, sortInfo[0]);
                }).toList()
        );

        return PageRequest.of(this.page - 1, this.size, sortBy);
    }

}
