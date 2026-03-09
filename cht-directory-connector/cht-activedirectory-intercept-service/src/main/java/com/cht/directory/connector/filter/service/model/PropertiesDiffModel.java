package com.cht.directory.connector.filter.service.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PropertiesDiffModel {

    private String samaccountname;
    private String dn;
    private String sn;
    private String innerProperties;
    private String externalProperties;

}
