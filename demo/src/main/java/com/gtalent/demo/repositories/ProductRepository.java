package com.gtalent.demo.repositories;

import com.gtalent.demo.models.Product;
import com.gtalent.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
