package com.cht.directory.connector.filter.service.model;

import com.cht.directory.connector.filter.web.entity.WebAuditLogs;
import lombok.Data;

import java.util.List;

@Data
public class AuditResultModel {

    private String result;
    private String message;
    private List<WebAuditLogs> data;
    private int total;

}
