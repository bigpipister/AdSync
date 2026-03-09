package com.cht.directory.connector.filter.service;

import com.cht.directory.connector.filter.controller.FilterEventController;
import com.cht.directory.connector.filter.web.entity.WebAuditLogs;
import com.cht.directory.connector.filter.web.repository.WebAuditLogsRepository;
import com.cht.directory.connector.filter.service.model.AuditResultModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AuditLogsWebService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogsWebService.class);

    @Autowired
    private WebAuditLogsRepository webAuditLogsRepository;

    public AuditResultModel getAuditLogs(int page, int size, String region, String activitydisplayname, String dn
            , Timestamp startTime, Timestamp endTime) {

        Pageable pageable = PageRequest.of(page-1, size);

        int total = webAuditLogsRepository.findByRegionContainingAndActivitydisplaynameContainingAndTargetresourcesContainingAndActivitydatetimeBetween(
                region, activitydisplayname, dn, startTime, endTime).size();
        Page<WebAuditLogs> auditLogsList =
                webAuditLogsRepository.findByRegionContainingAndActivitydisplaynameContainingAndTargetresourcesContainingAndActivitydatetimeBetween(
                        region, activitydisplayname, dn, startTime, endTime, pageable);
        AuditResultModel auditResultModel = new AuditResultModel();
        auditResultModel.setResult("T");
        auditResultModel.setMessage("");
        auditResultModel.setData(auditLogsList.getContent());
        auditResultModel.setTotal(total);

        return auditResultModel;
    }

}
