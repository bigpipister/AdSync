package com.cht.directory.connector.filter.service.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PwdDiffModel {

    private String samaccountname;
    private String dn;
    private String sn;
    private Timestamp innerTimestamp;
    private Timestamp externalTimestamp;

}
