package com.mable.springbootproducer.producer;

import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mable.springbootproducer.constants.ConstantsSetting;
import com.mable.springbootproducer.constants.ConstantsStatus;
import com.mable.springbootproducer.entity.Order;
import com.mable.springbootproducer.mapper.BrokerMessageLogMapper;

@Component
public class RabbitOrderSender {

	//自动注入rabbitTemplate模板类
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
		
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			System.out.println("correlationData:" + correlationData);
			String messageId = correlationData.getId();
			if(ack) {
				brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, ConstantsStatus.ORDER_SEND_SUCCESS, new Date());
			}else {
				//失败，可以进行重传等
				System.out.println("异常----异常-------------");
			}
		}
	};
	
	public void sendOrder(Order order) {
		rabbitTemplate.setConfirmCallback(confirmCallback);
		//消息唯一id
		CorrelationData correlationData = new CorrelationData(order.getMessageId());

		rabbitTemplate.convertAndSend(ConstantsSetting.RABBIT_EXCHANGE, 
				ConstantsSetting.RABBIT_ROUTING_KEY, 
				order, 
				correlationData);
	}
	
	
	
	
	
	
	//单独测试消息的发送
	public void send(Order order) throws Exception{
		CorrelationData correlationData = new CorrelationData();
		correlationData.setId(order.getId());
		rabbitTemplate.convertAndSend("order-exchange",  //exchange
				"order.abcd", //routingKey
				order, //消息体
				correlationData);
	}
}
