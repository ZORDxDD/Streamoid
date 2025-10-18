package com.intern.streamoid.repositories;

import com.intern.streamoid.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface Productrepository extends JpaRepository<Product, String> {
    Page<Product> findAllByBrand(String brand, Pageable pageDetails);

    Page<Product> findAllByColor(String color, Pageable pageDetails);

    Page<Product> findAllByPriceBetween(Double minPrice, Double maxPrice, Pageable pageDetails);
}
