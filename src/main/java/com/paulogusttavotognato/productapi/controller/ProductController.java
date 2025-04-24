package com.paulogusttavotognato.productapi.controller;

import com.paulogusttavotognato.productapi.controller.dto.ProductRequest;
import com.paulogusttavotognato.productapi.controller.dto.ProductResponse;
import com.paulogusttavotognato.productapi.model.Product;
import com.paulogusttavotognato.productapi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    private ResponseEntity<List<ProductResponse>> findAll() {
        var products = productService.listAll();
        var productResponseList = products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
        return ResponseEntity.ok(productResponseList);
    }

    @GetMapping("/count")
    private ResponseEntity<Long> count() {
        return ResponseEntity.ok(productService.count());
    }

    @GetMapping("/name/{name}")
    private ResponseEntity<List<ProductResponse>> search(@RequestParam String name) {
        var products = productService.findByName(name);
        var productResponseList = products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
        return ResponseEntity.ok(productResponseList);
    }

    @GetMapping("/{id}")
    private ResponseEntity<ProductResponse> findById(@RequestParam Long id) {
        var product = productService.findById(id);
        if (product.isPresent()) {
            var productResponse = new ProductResponse(product.get().getId(), product.get().getName(), product.get().getDescription(), product.get().getPrice());
            return ResponseEntity.ok(productResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    private ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest product) {
        var newProduct = new Product(product.getName(), product.getDescription(), product.getPrice());
        var savedProduct = productService.save(newProduct);
        var productResponse = new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(), savedProduct.getPrice());
        return ResponseEntity.created(null).body(productResponse);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@RequestParam Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest product) {
        var updatedProduct = new Product(product.getName(), product.getDescription(), product.getPrice());
        var savedProduct = productService.update(id, updatedProduct);
        var productResponse = new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(), savedProduct.getPrice());
        return ResponseEntity.ok(productResponse);
    }

}
