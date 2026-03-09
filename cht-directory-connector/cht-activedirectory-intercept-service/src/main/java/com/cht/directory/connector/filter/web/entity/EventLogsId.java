package com.cht.directory.connector.filter.web.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventLogsId implements Serializable {

    private String region;
    private String jobname;
    private String dn;
    private String exceptionhash;

}
