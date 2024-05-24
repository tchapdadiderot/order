package com.jagaad.technical_test.order.repository;

import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderId;
import com.jagaad.technical_test.order.domain.OrderNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface OrderSpringRepository extends ListCrudRepository<Order, OrderId> {

    @Query(value = "SELECT NEXT VALUE FOR order_number_seq_id", nativeQuery = true)
    int nextFreeOrderNumberForH2();

    Optional<Order> findByOrderNumber(OrderNumber orderNumber);

    @Query(value = "SELECT * FROM t_order WHERE c_firstname LIKE CONCAT('%', :searchText, '%') OR c_lastname LIKE CONCAT('%', :searchText, '%') OR c_city LIKE CONCAT('%', :searchText, '%') OR c_street LIKE CONCAT('%', :searchText, '%')", nativeQuery = true)
    List<Order> searchOrderByCustomerData(@Param("searchText") String searchText);
}
