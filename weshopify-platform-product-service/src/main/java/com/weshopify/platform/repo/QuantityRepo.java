package com.weshopify.platform.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weshopify.platform.model.Quantity;

public interface QuantityRepo extends JpaRepository<Quantity, String>{

}
