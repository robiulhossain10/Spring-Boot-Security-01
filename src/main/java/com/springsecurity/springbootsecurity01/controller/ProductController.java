package com.springsecurity.springbootsecurity01.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private List<Product> products = Arrays.asList(
            new Product(1L, "Laptop", "Electronics", 999.99),
            new Product(2L, "Book", "Education", 29.99),
            new Product(3L, "Phone", "Electronics", 699.99)
    );

    // PUBLIC ACCESS - Anyone can view products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(products);
    }

    // USER LEVEL ACCESS - Only authenticated users can view details
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = products.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElse(null);

        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // MODERATOR OR ADMIN - Can create products
    @PostMapping
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // In real application, save to database
        products.add(product);
        return ResponseEntity.ok(product);
    }

    // MODERATOR OR ADMIN - Can update products
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Update logic here
        return ResponseEntity.ok(product);
    }

    // ADMIN ONLY - Can delete products
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        // Delete logic here
        return ResponseEntity.ok("Product deleted successfully");
    }

    // MULTIPLE CONDITIONS - Complex security rule
    @GetMapping("/premium")
    @PreAuthorize("hasRole('PREMIUM_USER') and authentication.principal.enabled == true")
    public ResponseEntity<List<Product>> getPremiumProducts() {
        List<Product> premiumProducts = Arrays.asList(
                new Product(4L, "Premium Laptop", "Electronics", 1999.99),
                new Product(5L, "Premium Phone", "Electronics", 1299.99)
        );
        return ResponseEntity.ok(premiumProducts);
    }
}

// Product DTO
record Product(Long id, String name, String category, Double price) {}