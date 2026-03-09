package com.cht.exchange.queue;

import com.cht.exchange.commander.ExcCommander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {

//    @Autowired
//    private ExcCommander excCommander;
//
//    @JmsListener(destination = "exchange.command.queue")
//    //@Transactional(rollbackFor = Exception.class)
//    public void receiveQueue(String message) {
//        log.info("Queue Out: " + message);
//        excCommander.runTask(message);
//    }
}
