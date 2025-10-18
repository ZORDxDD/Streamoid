package com.intern.streamoid.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    private String sku;
    private String name;
    private String brand;
    private String color;
    private String size;
    private double mrp;
    private double price;
    private double quantity;
}
