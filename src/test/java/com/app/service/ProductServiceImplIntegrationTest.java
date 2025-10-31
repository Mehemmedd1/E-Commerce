package com.app.service;

import com.app.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceImplIntegrationTest {
    @Autowired
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new Double(99.99));
        testProduct.setStock(10);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        Product savedProduct = productService.createProduct(testProduct);
        List<Product> products = productService.getAllProducts();

        assertFalse(products.isEmpty());
        assertTrue(products.contains(savedProduct));
    }

    @Test
    void saveProduct_ShouldSaveAndReturnProduct() {
        Product savedProduct = productService.createProduct(testProduct);

        assertNotNull(savedProduct.getId());
        assertEquals(testProduct.getName(), savedProduct.getName());
        assertEquals(testProduct.getDescription(), savedProduct.getDescription());
        assertEquals(testProduct.getPrice(), savedProduct.getPrice());
        assertEquals(testProduct.getStock(), savedProduct.getStock());
    }

//    @Test
//    void deleteProduct_ShouldRemoveProduct() {
//        Product savedProduct = productService.saveProduct(testProduct);
//        productService.deleteProduct(savedProduct.getId());
//
//        assertThrows(EntityNotFoundException.class, () ->
//                productService.getProductById(savedProduct.getId()));
//    }
//
//    @Test
//    void updateProduct_ShouldUpdateExistingProduct() {
//        Product savedProduct = productService.saveProduct(testProduct);
//        savedProduct.setName("Updated Name");
//        savedProduct.setPrice(new BigDecimal("199.99"));
//
//        Product updatedProduct = productService.updateProduct(savedProduct);
//
//        assertEquals("Updated Name", updatedProduct.getName());
//        assertEquals(new BigDecimal("199.99"), updatedProduct.getPrice());
//    }
//
//    @Test
//    void getProductById_ShouldReturnProduct() {
//        Product savedProduct = productService.saveProduct(testProduct);
//        Product foundProduct = productService.getProductById(savedProduct.getId());
//
//        assertEquals(savedProduct.getId(), foundProduct.getId());
//        assertEquals(savedProduct.getName(), foundProduct.getName());
//    }
}
