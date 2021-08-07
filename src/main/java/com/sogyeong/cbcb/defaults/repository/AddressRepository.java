package com.sogyeong.cbcb.defaults.repository;

import com.sogyeong.cbcb.defaults.entity.Address;
import com.sogyeong.cbcb.defaults.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
