package com.intern.streamoid.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String sku;
    private String name;
    private String brand;
    private String color;
    private String size;
    private double mrp;
    private double price;
    private double quantity;
}
