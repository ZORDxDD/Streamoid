package com.intern.streamoid.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    List<ProductDTO> content;
    private long totalElements;
    private long totalPages;
    private long pageNumber;
    private long pageSize;
    private boolean lastPage;
}
