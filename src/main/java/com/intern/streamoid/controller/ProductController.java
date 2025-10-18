package com.intern.streamoid.controller;

import com.intern.streamoid.config.AppConstants;
import com.intern.streamoid.payload.ProductResponse;
import com.intern.streamoid.service.Productservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private Productservice productservice;

    @PostMapping("/admin/products/upload-csv")
    public ResponseEntity<Map<String, Object>> uploadProductsFromCsv(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = productservice.addProductsFromCsv(file);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to process CSV file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name= "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber, @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
            , @RequestParam(name = "sort" , defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sort, @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder){
        ProductResponse productResponse=productservice.getAllProducts(pageNumber,pageSize,sort,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/color/{color}")
    public ResponseEntity<ProductResponse> getAllProductsByColor(@RequestParam(name= "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber, @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
            , @RequestParam(name = "sort" , defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sort, @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder, @PathVariable String color){
        ProductResponse productResponse=productservice.getAllProductsByColor(color,pageNumber,pageSize,sort,sortOrder);
    return new ResponseEntity<>(productResponse, HttpStatus.OK);}

    @GetMapping("/public/products/brand/{brand}")
    public ResponseEntity<ProductResponse> getAllProductsByBrand(@RequestParam(name= "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber, @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
            , @RequestParam(name = "sort" , defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sort, @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder, @PathVariable String brand) {
        ProductResponse productResponse = productservice.getAllProductsByBrand(brand, pageNumber, pageSize, sort, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/products/price/{minPrice}/{maxPrice}")
    public ResponseEntity<ProductResponse> getAllProdcutsByPriceRange(@RequestParam(name= "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber, @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
            , @RequestParam(name = "sort" , defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sort, @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder, @PathVariable Double minPrice, @PathVariable Double maxPrice) {
        ProductResponse productResponse = productservice.getAllProdcutsByPriceRange(minPrice, maxPrice, pageNumber, pageSize, sort, sortOrder);
           return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


}
