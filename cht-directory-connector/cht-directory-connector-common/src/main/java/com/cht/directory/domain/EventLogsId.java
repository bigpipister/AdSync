package com.cht.directory.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventLogsId implements Serializable {

    private String region;
    private String jobname;
    private String dn;
    private String exceptionhash;

}
