package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class GetMailbox {

    @Parameter(names = "-Anr")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String anr;

    @Parameter(names = "-Arbitration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean arbitration;

    @Parameter(names = "-Archive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean archive;

    @Parameter(names = "-AuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auditLog;

    @Parameter(names = "-AuxAuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auxAuditLog;

    @Parameter(names = "-Credential")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String credential;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController ;

    @Parameter(names = "-Filter")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String filter;

    @Parameter(names = "-GroupMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean groupMailbox;

    @Parameter(names = "-IgnoreDefaultScope")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreDefaultScope;

    @Parameter(names = "-InactiveMailboxOnly")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean inactiveMailboxOnly;

    @Parameter(names = "-IncludeInactiveMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean includeInactiveMailbox;

    @Parameter(names = "-Migration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean migration;

    @Parameter(names = "-Monitoring")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean monitoring;

    @Parameter(names = "-OrganizationalUnit")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String organizationalUnit;

    @Parameter(names = "-PublicFolder")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean publicFolder;

    @Parameter(names = "-ReadFromDomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean readFromDomainController;

    @Parameter(names = "-RecipientTypeDetails")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String recipientTypeDetails;

    @Parameter(names = "-RemoteArchive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean remoteArchive;

    @Parameter(names = "-ResultSize")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String resultSize;

    @Parameter(names = "-SoftDeletedMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean softDeletedMailbox;

    @Parameter(names = "-SortBy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sortBy;

    @Parameter(names = "-Database")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String database;

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Server")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String server;

    @Parameter(names = "-MailboxPlan")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String mailboxPlan;

    @Parameter(names = "-Async")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean async;

    @Parameter(names = "-Properties")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String properties;
}
