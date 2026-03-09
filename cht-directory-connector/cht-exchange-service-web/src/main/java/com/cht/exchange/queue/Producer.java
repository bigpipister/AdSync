package com.cht.exchange.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("producer")
public class Producer {
//    @Autowired
//    private JmsMessagingTemplate jmsTemplate;
//
//    // 用來往embedded ActiveMQ的exchange.command.queue發送更新message
//    public void sendMessage(Destination destination, final String message) {
//        jmsTemplate.convertAndSend(destination, message);
//    }
}
