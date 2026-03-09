package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class RemoveMailbox {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Permanent")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String permanent;

    @Parameter(names = "-Arbitration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean arbitration;

    @Parameter(names = "-AuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auditLog;

    @Parameter(names = "-AuxAuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auxAuditLog;

    @Parameter(names = "-Confirm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean confirm;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-Force")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean force;

    @Parameter(names = "-IgnoreDefaultScope")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreDefaultScope;

    @Parameter(names = "-IgnoreLegalHold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreLegalHold;

    @Parameter(names = "-Migration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean migration;

    @Parameter(names = "-PublicFolder")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean publicFolder;

    @Parameter(names = "-RemoveArbitrationMailboxWithOABsAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean removeArbitrationMailboxWithOABsAllowed;

    @Parameter(names = "-RemoveLastArbitrationMailboxAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean removeLastArbitrationMailboxAllowed;

    @Parameter(names = "-whatIf")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean whatIf;

    @Parameter(names = "-Database")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String database;

    @Parameter(names = "-StoreMailboxIdentity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String storeMailboxIdentity;
}
