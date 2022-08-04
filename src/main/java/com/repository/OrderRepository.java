package com.repository;

import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.entity.Order;
import com.entity.Report;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    //Truy vấn dữ liệu sql
    @Query(value = "SELECT product_id as ProductId, p.product_name as ProductName, SUM(od.quantity) as Quantity, SUM(amount) as Amount, created_at as NgayThang\r\n"
            + "FROM products p\r\n" + "LEFT JOIN orders_detail od ON od.product_id = p.id \r\n"
            + "LEFT JOIN orders o  ON od.order_id = o.id \r\n"
            + "where o.status=3 \r\n"
            + "GROUP BY product_id\r\n" + "ORDER BY Amount  DESC ", nativeQuery = true)
    List<Report> reportByProduct();

    @Query(value = "SELECT CONCAT (YEAR(created_at),'-', MONTH(created_at), '-', DAY(created_at)) NgayThang, SUM(od.amount) Amount\r\n"
            + "FROM orders o\r\n"
            + "LEFT JOIN orders_detail od ON o.id = od.order_id\r\n"
            + "GROUP BY YEAR(created_at),MONTH(created_at),DAY(created_at) ", nativeQuery = true)
    List<Report> reportByDay();

    @Query(value = "SELECT CONCAT (YEAR(created_at),'-', MONTH(created_at)) NgayThang, SUM(od.amount) Amount\r\n"
            + "FROM orders o\r\n"
            + "LEFT JOIN orders_detail od ON o.id = od.order_id\r\n"
            + "GROUP BY YEAR(created_at),MONTH(created_at) ", nativeQuery = true)
    List<Report> reportByMonth();

    @Query(value = "SELECT CONCAT (YEAR(created_at)) NgayThang, SUM(od.amount) Amount\r\n"
            + "FROM orders o\r\n"
            + "LEFT JOIN orders_detail od ON o.id = od.order_id\r\n"
            + "GROUP BY YEAR(created_at) ", nativeQuery = true)
    List<Report> reportByYear();

    List<Order> findByUserId(int userId);

    List<Order> findByStatusOrderByIdDesc(int status);

    List<Order> findAllByOrderByIdDesc();

}
