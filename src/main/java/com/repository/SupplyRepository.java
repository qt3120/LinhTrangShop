package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Supply;

public interface SupplyRepository extends JpaRepository<Supply, Integer> {

}
