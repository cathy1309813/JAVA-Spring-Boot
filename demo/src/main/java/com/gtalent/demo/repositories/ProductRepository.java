package com.gtalent.demo.repositories;

import com.gtalent.demo.models.Product;
import com.gtalent.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

//step 2. 建立 Product Repository extends JpaRepository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
