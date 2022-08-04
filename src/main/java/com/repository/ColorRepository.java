package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Color;

public interface ColorRepository extends JpaRepository<Color, Integer> {

}
