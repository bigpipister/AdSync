package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.service.StatusLogsWebService;
import com.cht.directory.connector.filter.service.model.StatusResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusLogsController {

    private static final Logger log = LoggerFactory.getLogger(StatusLogsController.class);

    @Autowired
    private StatusLogsWebService statusLogsWebService;

    // 查詢 job 運行狀態紀錄
    @RequestMapping(value = "/statusLogs", method = RequestMethod.GET)
    public StatusResultModel findAllStatusLogs() {

        return statusLogsWebService.getStatusLogs();
    }
}
