package com.cht.directory.connector.filter.service.model;

import com.cht.directory.connector.filter.web.entity.StatusLogs;
import lombok.Data;

import java.util.List;

@Data
public class StatusResultModel {

    private String result;
    private String message;
    private List<StatusLogs> data;
    private int total;

}
