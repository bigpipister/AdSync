package com.cht.directory.connector.filter.service.model;

import com.cht.directory.connector.filter.web.entity.WebAuditLogs;
import lombok.Data;

import java.util.List;

@Data
public class DecryptResultModel {

    private String result;
    private String message;
    private String data;
}
