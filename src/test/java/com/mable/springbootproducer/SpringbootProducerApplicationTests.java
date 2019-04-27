package com.mable.springbootproducer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mable.springbootproducer.entity.Order;
import com.mable.springbootproducer.producer.RabbitOrderSender;
import com.mable.springbootproducer.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootProducerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private RabbitOrderSender orderSender;
	@Autowired
	private OrderService orderService;
	@Test
	public void sendOrderTest() throws Exception{
		Order order = new Order();
		order.setId(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		order.setMessageId("我是消息");
		order.setMessageId(UUID.randomUUID().toString());
		//orderSender.send(order);
		orderService.createOrder(order);
	}
}
