package com.gtalent.demo.controllers;


import com.gtalent.demo.models.Supplier;
import com.gtalent.demo.repositories.SupplierRepository;
import com.gtalent.demo.requests.CreateSupplierRequest;
import com.gtalent.demo.requests.UpdateSupplierRequest;
import com.gtalent.demo.responses.SupplierResponse;
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
    public ResponseEntity<List<SupplierResponse>> getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ResponseEntity.ok(suppliers.stream().map(SupplierResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable int id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            SupplierResponse response = new SupplierResponse(supplier.get());
            return ResponseEntity.ok(response);
        } else {
            //回傳404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplierById(@PathVariable int id, @RequestBody UpdateSupplierRequest request) {
        //1.找到Product
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(supplier.isPresent()) {
            //2.確定找到supplier之後
            Supplier updatedSupplier = supplier.get();
            System.out.println("Before Update:" + updatedSupplier);
            //3.將欲更新資料填充至對應supplier
            updatedSupplier.setName(request.getName());
            updatedSupplier.setAddress(request.getAddress());

            System.out.println("Before Save:" + updatedSupplier);

            updatedSupplier = supplierRepository.save(updatedSupplier);
            SupplierResponse response = new SupplierResponse(updatedSupplier);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSuppliers(@RequestBody CreateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setAddress(request.getAddress());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        System.out.println("Before Save:" + supplier);
        Supplier savedSupplier = supplierRepository.save(supplier);
        SupplierResponse response = new SupplierResponse(savedSupplier);
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
