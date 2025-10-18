package com.intern.streamoid.service;

import com.intern.streamoid.model.Product;
import com.intern.streamoid.payload.ProductDTO;
import com.intern.streamoid.payload.ProductResponse;
import com.intern.streamoid.repositories.Productrepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImplementation implements Productservice{

    @Autowired
    private Productrepository productrepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sort, String sortOrder) {
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByandOrder);
        Page<Product> productPage = productrepository.findAll(pageDetails);
        List<Product> products=productPage.getContent();
        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByBrand(String Brand, Integer pageNumber, Integer pageSize, String sort, String sortOrder) {
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByandOrder);
        Page<Product> productPage = productrepository.findAllByBrand(Brand,pageDetails);
        List<Product> products=productPage.getContent();
        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByColor(String color, Integer pageNumber, Integer pageSize, String sort, String sortOrder) {
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByandOrder);
        Page<Product> productPage = productrepository.findAllByColor(color,pageDetails);
        List<Product> products=productPage.getContent();
        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getAllProdcutsByPriceRange(Double minPrice, Double maxPrice, Integer pageNumber, Integer pageSize, String sort, String sortOrder) {
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productPage = productrepository.findAllByPriceBetween(minPrice, maxPrice, pageDetails);
        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public Map<String, Object> addProductsFromCsv(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed");
        }

        List<Product> products = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] fields = line.split(",");

                    if (fields.length < 8) {
                        errors.add("Line " + lineNumber + ": Insufficient fields");
                        errorCount++;
                        continue;
                    }

                    Product product = new Product();
                    product.setSku(fields[0].trim());
                    product.setName(fields[1].trim());
                    product.setBrand(fields[2].trim());
                    product.setColor(fields[3].trim());
                    product.setSize(fields[4].trim());  // Changed from Long.parseLong()
                    product.setMrp(Double.parseDouble(fields[5].trim()));
                    product.setPrice(Double.parseDouble(fields[6].trim()));
                    product.setQuantity(Double.parseDouble(fields[7].trim()));

                    products.add(product);

                    successCount++;

                } catch (NumberFormatException e) {
                    errors.add("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
                    errorCount++;
                } catch (Exception e) {
                    errors.add("Line " + lineNumber + ": " + e.getMessage());
                    errorCount++;
                }
            }

            // Save all valid products to database
            if (!products.isEmpty()) {
                productrepository.saveAll(products);
            }

        } catch (IOException e) {
            throw new IOException("Error reading CSV file: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalProcessed", lineNumber - 1); // Exclude header
        result.put("successCount", successCount);
        result.put("errorCount", errorCount);

        if (!errors.isEmpty()) {
            result.put("errors", errors);
        }

        result.put("message", successCount + " products added successfully");

        return result;
    }
}
