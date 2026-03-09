package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.service.EventLogsWebService;
import com.cht.directory.connector.filter.service.model.EventResultModel;
import com.cht.directory.connector.filter.service.model.EventRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventLogsController {

    private static final Logger log = LoggerFactory.getLogger(EventLogsController.class);

    @Autowired
    private EventLogsWebService eventLogsWebService;

    // 查詢 job 執行時發生的 exception 紀錄
    @RequestMapping(value = "/eventLogs", method = RequestMethod.POST)
    public EventResultModel findAllEventLogs(@RequestBody EventRequestModel request) {
        //log.info(request.toString());
        return eventLogsWebService.getEventLogs(request.getPage(), request.getSize(), request.getRegion(),
                request.getJobname(), request.getDn(), request.getKeyword());
    }
}
