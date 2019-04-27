package com.mable.springbootproducer.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mable.springbootproducer.constants.ConstantsStatus;
import com.mable.springbootproducer.entity.BrokerMessageLog;
import com.mable.springbootproducer.entity.Order;
import com.mable.springbootproducer.mapper.BrokerMessageLogMapper;
import com.mable.springbootproducer.producer.RabbitOrderSender;

/**
 * 消息重试、最大努力尝试策略（定时任务）
 * @author wen
 * @date 2019-4-7
 */
@Component
public class RetryMessageTasker {

	@Autowired
	private RabbitOrderSender rabbitOrderSender;
	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	
	@Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend(){
		System.err.println("定时任务启动");
        //pull status = 0 and timeout message 
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        list.forEach(messageLog -> {
            if(messageLog.getTryCount() >= 3){
                //update fail message 
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), ConstantsStatus.ORDER_SEND_FAILURE, new Date());
            } else {
                // resend 
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
                Order reSendOrder = JSONObject.parseObject(messageLog.getMessage(), Order.class);
                try {
                    rabbitOrderSender.sendOrder(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }            
        });
    }
}
