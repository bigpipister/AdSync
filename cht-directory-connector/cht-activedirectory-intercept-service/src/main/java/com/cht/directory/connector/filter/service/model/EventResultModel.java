package com.cht.directory.connector.filter.service.model;

import com.cht.directory.connector.filter.web.entity.EventLogs;
import lombok.Data;

import java.util.List;

@Data
public class EventResultModel {

    private String result;
    private String message;
    private List<EventLogs> data;
    private int total;

}
