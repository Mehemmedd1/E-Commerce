package com.app.service;

import com.app.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    void deleteProduct(Long id);
    Product createProduct(Product product);
}
