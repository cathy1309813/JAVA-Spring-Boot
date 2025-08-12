package com.gtalent.demo.controllers;

import com.gtalent.demo.models.Product;
import com.gtalent.demo.models.Supplier;
import com.gtalent.demo.repositories.ProductRepository;
import com.gtalent.demo.repositories.SupplierRepository;
import com.gtalent.demo.requests.CreateProductRequest;
import com.gtalent.demo.requests.UpdateProductRequest;
import com.gtalent.demo.responses.ProductResponse;
import com.gtalent.demo.responses.SupplierResponse;
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
    private final SupplierRepository supplierRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, SupplierRepository supplierRepository){
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

//    @GetMapping
//    public ResponseEntity<List<ProductResponse>> getAllProduct() {
//        List<Product> products = productRepository.findAll();
//        return ResponseEntity.ok(products.stream().map(ProductResponse::new).toList());
//    }

    //功能改寫: 為因應Product model中，新增了資料庫中多個products對應供應商詳細資料的關聯
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products.stream().map(product -> {
            ProductResponse response = new ProductResponse(product);
            response.setSupplier(new SupplierResponse(product.getSupplier()));
            return response;
        }).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductResponse response = new ProductResponse(product.get());
            response.setSupplier(new SupplierResponse(product.get().getSupplier()));
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

//    @PostMapping
//    public ResponseEntity<ProductResponse> createProducts(@RequestBody CreateProductRequest request) {
//        Product product = new Product();
//        product.setName(request.getName());
//        product.setPrice(request.getPrice());
//        product.setQuantity(request.getQuantity());
//        product.setStatus(request.isStatus());
//        product.setSupplierId(request.getSupplierId());
//        System.out.println("Before Save:" + product);
//        Product savedProduct = productRepository.save(product);
//        ProductResponse response = new ProductResponse(savedProduct);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProducts(@RequestBody CreateProductRequest request) {
        Optional<Supplier> supplier = supplierRepository.findById(request.getSupplierId());
        if (supplier.isPresent()) {
            Product product = new Product();
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setStatus(request.isStatus());
            product.setSupplier(supplier.get());
            System.out.println("Before Save:" + product);
            Product savedProduct = productRepository.save(product);
            ProductResponse response = new ProductResponse(savedProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
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
