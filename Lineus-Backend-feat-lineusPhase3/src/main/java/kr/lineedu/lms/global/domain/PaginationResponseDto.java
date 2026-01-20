package kr.lineedu.lms.global.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaginationResponseDto<C, P, E> {


    private List<C> content;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private E extraInfo;

    private Integer currentPageNumber;

    private Integer currentPageElementCount;

    private Integer elementPerPage;

    private Long totalElements;

    private Integer totalPages;

    private Boolean isFirstPage;

    private Boolean isLastPage;

    private Sort sort;


    /**
     * To respond generic pagination content and page data without extra info
     * @author Htet Phyo Maung
     * @co-author Htoo Maung Thait
     * @co-author GitHub Copilot
     * @since 2025-09-10
     * @lastModified 2025-09-10
     * @param content List<c> generic list content
     * @param page Page<P> generic page data
     * @return PaginationResponseDto<C, P, Void> generic pagination response dto without extra
     * */
    public static <C, P, Void> PaginationResponseDto<C, P, Void> of(List<C> content, Page<P> page) {

        return PaginationResponseDto.<C, P, Void>builder()
                .content(content)
                .extraInfo(null)
                .currentPageNumber(page.getNumber() + 1)
                .currentPageElementCount(content.size())
                .elementPerPage(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .sort(page.getSort())
                .build();
    }

    /**
     * To respond generic pagination content, page data and generic summary extra info
     *
     * @author Htoo Maung Thait
     * @co-author GitHub Copilot
     * @since 2025-09-10
     * @lastModified 2025-09-10
     * @param content List<c> generic list content
     * @param page Page<P> generic page data
     * @param extraInfo E generic extra info
     * @return PaginationResponseDto<C, P, E> generic pagination response dto
     * */
    public static <C, P, E> PaginationResponseDto<C, P, E> of(List<C> content, Page<P> page, E extraInfo) {

        return PaginationResponseDto.<C, P, E>builder()
                .content(content)
                .extraInfo(extraInfo)
                .currentPageNumber(page.getNumber() + 1)
                .currentPageElementCount(content.size())
                .elementPerPage(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .sort(page.getSort())
                .build();
    }
}
