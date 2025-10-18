package com.intern.streamoid.service;

import com.intern.streamoid.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface Productservice {
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sort, String sortOrder);

    ProductResponse getAllProductsByBrand(String brand, Integer pageNumber, Integer pageSize, String sort, String sortOrder);

    ProductResponse getAllProductsByColor(String color, Integer pageNumber, Integer pageSize, String sort, String sortOrder);

    ProductResponse getAllProdcutsByPriceRange(Double minPrice, Double maxPrice, Integer pageNumber, Integer pageSize, String sort, String sortOrder);

    Map<String, Object> addProductsFromCsv(MultipartFile file) throws IOException;

}
