package com.service;

import java.util.List;

import com.entity.Report;

public interface ReportService {
    List<Report> reportByProduct(String date1, String date2);

    List<Report> reportByDate();

    List<Report> reportByMonth();

    List<Report> reportByYear();
}
