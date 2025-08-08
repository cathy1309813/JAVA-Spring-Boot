package com.gtalent.demo.controllers;


import com.gtalent.demo.models.Product;
import com.gtalent.demo.models.Supplier;
import com.gtalent.demo.repositories.ProductRepository;
import com.gtalent.demo.repositories.SupplierRepository;
import com.gtalent.demo.requests.CreateProductRequest;
import com.gtalent.demo.requests.CreateSupplierRequest;
import com.gtalent.demo.requests.UpdateProductRequest;
import com.gtalent.demo.requests.UpdateSupplierRequest;
import com.gtalent.demo.responses.GetProductResponse;
import com.gtalent.demo.responses.GetSupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
@CrossOrigin("*")
public class SupplierController {
    //step 1. 建立 Supplier Model
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public ResponseEntity<List<GetSupplierResponse>> getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ResponseEntity.ok(suppliers.stream().map(GetSupplierResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSupplierResponse> getSupplierById(@PathVariable int id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            GetSupplierResponse response = new GetSupplierResponse(supplier.get());
            return ResponseEntity.ok(response);
        } else {
            //回傳404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetSupplierResponse> updateSupplierById(@PathVariable int id, @RequestBody UpdateSupplierRequest request) {
        //1.找到Product
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(supplier.isPresent()) {
            //2.確定找到supplier之後
            Supplier updatedSupplier = supplier.get();
            System.out.println("Before Update:" + updatedSupplier);
            //3.將欲更新資料填充至對應supplier
            updatedSupplier.setName(request.getName());
            updatedSupplier.setAddress(request.getAddress());
            updatedSupplier.setPhone(request.getPhone());
            updatedSupplier.setEmail(request.getEmail());

            System.out.println("Before Save:" + updatedSupplier);

            updatedSupplier = supplierRepository.save(updatedSupplier);
            GetSupplierResponse response = new GetSupplierResponse(updatedSupplier);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GetSupplierResponse> createSuppliers(@RequestBody CreateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setAddress(request.getAddress());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        System.out.println("Before Save:" + supplier);
        Supplier savedSupplier = supplierRepository.save(supplier);
        GetSupplierResponse response = new GetSupplierResponse(savedSupplier);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuppliersById(@PathVariable int id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(supplier.isPresent()) {
            supplierRepository.delete(supplier.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
