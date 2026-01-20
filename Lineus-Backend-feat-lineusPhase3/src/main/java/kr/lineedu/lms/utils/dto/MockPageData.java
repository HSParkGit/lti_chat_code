package kr.lineedu.lms.utils.dto;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


public class MockPageData<T> {


    /**
     * Creates a mock Page object for testing purposes.
     *
     * @author Htoo Maung Thait
     * @co-author GitHub Copilot
     * @since 2025-08-19
     * @lastModified  2025-08-19
     * @param totalMockData the complete list of attendance events
     * @param subSetMockData             the subset of data to be returned for the current page
     * @param pageable                   the pagination information
     * @return a mock Page object containing the paginated data
     */

    public Page<T>  getPageData(
            List<T> totalMockData,
            List<T> subSetMockData,
            Pageable pageable
    ) {



        return new Page<T>() {
            @NotNull
            @Override
            public Iterator<T> iterator() {
                return null;
            }

            @Override
            public int getTotalPages() {
                return (int) Math.ceil((double) totalMockData.size() / pageable.getPageSize());
            }

            @Override
            public long getTotalElements() {
                return totalMockData.size();
            }

            @Override
            public <U> Page<U> map(Function<? super T, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return pageable.getPageNumber();
            }

            @Override
            public int getSize() {
                return pageable.getPageSize();
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<T> getContent() {
                return subSetMockData;
            }

            @Override
            public boolean hasContent() {
                return !subSetMockData.isEmpty();
            }

            @Override
            public Sort getSort() {
                return pageable.getSort();
            }

            @Override
            public boolean isFirst() {
                return pageable.getPageNumber() == 0;
            }

            @Override
            public boolean isLast() {
                return pageable.getPageNumber() >= getTotalPages() - 1;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }
        };
            
        
    }
}
