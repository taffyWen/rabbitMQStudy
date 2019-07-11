package com.mable.springbootproducer.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mable.springbootproducer.constants.ConstantsStatus;
import com.mable.springbootproducer.entity.BrokerMessageLog;
import com.mable.springbootproducer.entity.Order;
import com.mable.springbootproducer.mapper.BrokerMessageLogMapper;
import com.mable.springbootproducer.mapper.OrderMapper;
import com.mable.springbootproducer.producer.RabbitOrderSender;
import com.mable.springbootproducer.utils.DateUtil;

@Service
public class OrderService {

	 	@Autowired
	    private OrderMapper orderMapper;
	     
	    @Autowired
	    private BrokerMessageLogMapper brokerMessageLogMapper;
	     
	    @Autowired
	    private RabbitOrderSender rabbitOrderSender;
	     
	    public void createOrder(Order order) throws Exception {
	        // 使用当前时间当做订单创建时间（为了模拟一下简化）
	        Date orderTime = new Date();
	        // 插入业务数据
	        orderMapper.insert(order);
	        // 插入消息记录表数据
	        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
	        // 消息唯一ID
	        brokerMessageLog.setMessageId(order.getMessageId());
	        // 保存消息整体 转为JSON 格式存储入库
	        brokerMessageLog.setMessage(JSONObject.toJSONString(order));
	         // 设置消息状态为0 表示发送中
	        brokerMessageLog.setStatus(ConstantsStatus.ORDER_SENDING);
	         // 设置消息未确认超时时间窗口为 一分钟 
	        brokerMessageLog.setNextRetry(DateUtils.addMinutes(DateUtil.getNowDate(), ConstantsStatus.ORDER_TIMEOUT));
	        /*brokerMessageLog.setCreateTime(new Date());
	        brokerMessageLog.setUpdateTime(new Date());*/
	        brokerMessageLogMapper.insert(brokerMessageLog);
	        // 发送消息
	        rabbitOrderSender.sendOrder(order);
	    }

}
