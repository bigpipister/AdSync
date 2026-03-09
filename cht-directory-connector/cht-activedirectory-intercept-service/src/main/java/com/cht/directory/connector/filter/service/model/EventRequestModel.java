package com.cht.directory.connector.filter.service.model;

import lombok.Data;


@Data
public class EventRequestModel {

    private int page;
    private int size;
    private String region;
    private String jobname;
    private String dn;
    private String keyword;
}
