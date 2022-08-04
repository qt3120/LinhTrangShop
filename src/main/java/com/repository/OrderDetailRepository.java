package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

}
