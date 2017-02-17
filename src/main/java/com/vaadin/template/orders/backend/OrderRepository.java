package com.vaadin.template.orders.backend;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByDueDateAfterAndStateInOrderByDueDateAsc(LocalDate dueDate,
            Collection<OrderState> states, Pageable page);

    long countByDueDateAfterAndStateIn(LocalDate dueDate,
            Collection<OrderState> states);

    long countByDueDate(LocalDate dueDate);

    long countByDueDateAndStateIn(LocalDate dueDate,
            Collection<OrderState> state);

    long countByState(OrderState state);

    @Query("SELECT month(dueDate) as month, count(*) as deliveries FROM OrderInfo o where o.state=?1 and year(dueDate)=?2 group by month(dueDate)")
    List<Object[]> countPerMonth(OrderState orderState, int year);

    @Query("SELECT month(o.dueDate) as month, sum(oi.quantity*p.price) as deliveries FROM OrderInfo o JOIN o.items oi JOIN oi.product p where o.state=?1 and year(o.dueDate)=?2 group by month(o.dueDate)")
    List<Object[]> sumPerMonth(OrderState orderState, int year);

    @Query("SELECT day(dueDate) as day, count(*) as deliveries FROM OrderInfo o where o.state=?1 and year(dueDate)=?2 and month(dueDate)=?3 group by day(dueDate)")
    List<Object[]> countPerDay(OrderState orderState, int year, int month);

    @Query("SELECT sum(oi.quantity),p FROM OrderInfo o JOIN o.items oi JOIN oi.product p WHERE o.state=?1 AND year(o.dueDate)=?2 AND month(o.dueDate)=?3 GROUP BY p.id ORDER BY p.id")
    List<Object[]> countPerProduct(OrderState orderState, int year, int month);
}
