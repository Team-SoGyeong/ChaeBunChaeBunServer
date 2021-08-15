package com.sogyeong.cbcb.defaults.repository;

import com.sogyeong.cbcb.defaults.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository  extends JpaRepository<Products,Long> {
   Boolean existsByName(String name);
    List<Products> findByName(String name);
}
