package com.cht.directory.connector.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cht.directory.connector.type.AuditLogsResult;
import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.AuditLogs;
import com.cht.directory.repository.AuditLogsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AuditLogsService {

    @Value("${spring.application.name:none}")
    private String appName;

    @Value("${activedirectory.service.placeholder:inner}")
    private PlaceHolder placeHolder;

    @Autowired
    private AuditLogsRepository auditLogsRepository;

    public void audit(String activityDisplayName, String additionalDetails, String category,
            String correlationId, String loggedByService, AuditLogsResult auditLogsResult,
            int resultCode, String resultReason, String targetResources,
            long durationInMilliseconds) throws Exception {

        AuditLogs auditLogs = new AuditLogs();

//        List<AuditLogs> auditLogsList = auditLogsRepository
//                .findByTargetresourcesAndResultAndResultcodeAndAdditionaldetailsAndRegionOrderByActivitydatetimeDesc(
//                        targetResources, auditLogsResult.name(), resultCode, additionalDetails,
//                        placeHolder.name().toLowerCase());
//
//        if (!CollectionUtils.isEmpty(auditLogsList)) {
//
//            auditLogs = auditLogsList.get(0);
//            auditLogs.setDurationinmilliseconds(auditLogs.getDurationinmilliseconds() + 1);
//        } else {
//
//            auditLogs.setDurationinmilliseconds(0);
//        }

        auditLogs.setRegion(placeHolder.name().toLowerCase());
        Date date = new Date();
        auditLogs.setActivitydatetime(new Timestamp(date.getTime()));
        auditLogs.setActivitydisplayname(activityDisplayName);
        auditLogs.setAdditionaldetails(additionalDetails);
        auditLogs.setCategory(category);
        auditLogs.setCorrelationid(correlationId);
        auditLogs.setInitiatedby(appName);
        auditLogs.setLoggedbyservice(loggedByService);
        auditLogs.setResult(auditLogsResult.name());
        auditLogs.setResultcode(resultCode);
        auditLogs.setResultreason(resultReason);
        auditLogs.setTargetresources(targetResources);
        auditLogs.setDurationinmilliseconds(durationInMilliseconds);

        auditLogsRepository.save(auditLogs);
    }
}
