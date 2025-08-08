package com.gtalent.demo.controllers;

import com.gtalent.demo.models.Product;
import com.gtalent.demo.repositories.ProductRepository;
import com.gtalent.demo.requests.CreateProductRequest;
import com.gtalent.demo.requests.UpdateProductRequest;
import com.gtalent.demo.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {
    //step 1. 建立 Product Model
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products.stream().map(ProductResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductResponse response = new ProductResponse(product.get());
            return ResponseEntity.ok(response);
        } else {
            //回傳404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable int id, @RequestBody UpdateProductRequest request) {
        //1.找到Product
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            //2.確定找到product之後
            Product updatedProduct = product.get();
            System.out.println("Before Update:" + updatedProduct);
            //3.將欲更新資料填充至對應product
            updatedProduct.setName(request.getName());
            updatedProduct.setPrice(request.getPrice());
            updatedProduct.setQuantity(request.getQuantity());
            updatedProduct.setStatus(request.isStatus());

            System.out.println("Before Save:" + updatedProduct);

            updatedProduct = productRepository.save(updatedProduct);
            ProductResponse response = new ProductResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProducts(@RequestBody CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setStatus(request.isStatus());
        product.setSupplierId(request.getSupplierId());
        System.out.println("Before Save:" + product);
        Product savedProduct = productRepository.save(product);
        ProductResponse response = new ProductResponse(savedProduct);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductsById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            productRepository.delete(product.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
