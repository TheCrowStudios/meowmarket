package com.thecrowstudios.meowmarket.orders;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findAllByUser_Id(Integer userId);
}
