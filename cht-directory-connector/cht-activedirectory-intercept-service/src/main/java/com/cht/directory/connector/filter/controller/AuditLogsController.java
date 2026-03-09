package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.service.AuditLogsWebService;
import com.cht.directory.connector.filter.service.model.AuditResultModel;
import com.cht.directory.connector.filter.service.model.AuditRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditLogsController {

    private static final Logger log = LoggerFactory.getLogger(AuditLogsController.class);

    @Autowired
    private AuditLogsWebService auditLogsWebService;

    @RequestMapping(value = "/auditLogs", method = RequestMethod.POST)
    public AuditResultModel findAllAuditLogs(@RequestBody AuditRequestModel request) {
        //log.info(request.toString());
        return auditLogsWebService.getAuditLogs(request.getPage(), request.getSize(), request.getRegion(),
                request.getActivitydisplayname(), request.getTargetresources(), request.getStartTime(), request.getEndTime());
    }
}
