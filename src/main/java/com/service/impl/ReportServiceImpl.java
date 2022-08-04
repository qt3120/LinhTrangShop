package com.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Report;
import com.repository.OrderRepository;
import com.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepository orderRepository;

    public Date chuyen2(String a) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter1.parse(a);
            return date;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @Override
    public List<Report> reportByDate() {
        // TODO Auto-generated method stub
        return orderRepository.reportByDay();
    }

    @Override
    public List<Report> reportByMonth() {
        // TODO Auto-generated method stub
        return orderRepository.reportByMonth();
    }

    @Override
    public List<Report> reportByYear() {
        // TODO Auto-generated method stub
        return orderRepository.reportByYear();
    }

    @Override
    public List<Report> reportByProduct(String date1, String date2) {
        List<Report> ls = orderRepository.reportByProduct();

        List<Report> ls1 = new ArrayList<>();
        if (date1.equals("") || date2.equals("")) {
            return ls;
        }
        long a1 = chuyen2(date1).getTime();

        for (Report i : ls) {
            long a2 = chuyen2(i.getNgayThang()).getTime();
            if (chuyen2(i.getNgayThang()).getTime() <= (chuyen2(date2)).getTime() && chuyen2(i.getNgayThang()).getTime() >= (chuyen2(date1)).getTime()) {
                ls1.add(i);
            }
        }
        return ls1;
    }

}
