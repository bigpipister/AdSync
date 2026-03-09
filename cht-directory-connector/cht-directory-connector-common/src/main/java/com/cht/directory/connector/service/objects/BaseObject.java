package com.cht.directory.connector.service.objects;

import com.cht.directory.connector.type.AuditLogsCategoryType;

import lombok.Data;

@Data
public class BaseObject {

    protected AuditLogsCategoryType auditLogsCategoryType;
    protected String additionalDetails;
    protected String logname;
    protected String correlationId;

    protected BaseObject() {

        auditLogsCategoryType = AuditLogsCategoryType.Other;
    }
}
