package com.cht.directory.connector.filter.service.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DecryptRequestModel {

    private String cn;
    private String encryptPwd;
}
