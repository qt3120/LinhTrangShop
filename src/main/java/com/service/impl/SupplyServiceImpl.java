package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Supply;
import com.repository.SupplyRepository;
import com.service.SupplyService;

@Service
public class SupplyServiceImpl implements SupplyService {
    @Autowired
    private SupplyRepository supplyRepository;

    @Override
    public List<Supply> list() {
        return supplyRepository.findAll();
    }

    @Override
    public Supply createOrUpdate(Supply supply) {
        return supplyRepository.save(supply);
    }

    @Override
    public Supply delete(int id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        supplyRepository.delete(supply);
        return supply;
    }

    @Override
    public Supply findById(int id) {
        return supplyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
    }

}
