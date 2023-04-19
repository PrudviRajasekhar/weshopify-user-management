package com.weshopify.platform.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weshopify.platform.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
