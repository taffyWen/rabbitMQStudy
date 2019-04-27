package com.mable.springbootproducer.mapper;

import org.springframework.stereotype.Component;

import com.mable.springbootproducer.entity.Order;

@Component
public interface OrderMapper {

	void insert(Order order);
}
