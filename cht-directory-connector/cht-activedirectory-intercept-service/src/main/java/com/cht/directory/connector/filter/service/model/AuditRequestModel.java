package com.cht.directory.connector.filter.service.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AuditRequestModel {

    private int page;
    private int size;
    private String region;
    private String activitydisplayname;
    private String targetresources;
    private Timestamp startTime;
    private Timestamp endTime;
}
