package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class EnableMailbox {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Arbitration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean arbitration;

    @Parameter(names = "-ActiveSyncMailboxPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String activeSyncMailboxPolicy;

    @Parameter(names = "-Alias")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String alias ;

    @Parameter(names = "-Confirm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean confirm;

    @Parameter(names = "-Database")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String database;

    @Parameter(names = "-DisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String displayName;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-Force")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean force;

    @Parameter(names = "-ManagedFolderMailboxPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String managedFolderMailboxPolicy;

    @Parameter(names = "-ManagedFolderMailboxPolicyAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean managedFolderMailboxPolicyAllowed;

    @Parameter(names = "-PrimarySmtpAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String primarySmtpAddress;

    @Parameter(names = "-RetentionPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retentionPolicy;

    @Parameter(names = "-RoleAssignmentPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String roleAssignmentPolicy;

    @Parameter(names = "-WhatIf")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean whatIf;

    @Parameter(names = "-ArchiveDomain")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveDomain;

    @Parameter(names = "-RemoteArchive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean remoteArchive;

    @Parameter(names = "-LinkedDomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String linkedDomainController;

    @Parameter(names = "-LinkedMasterAccount")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String linkedMasterAccount;

    @Parameter(names = "-LinkedCredential")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String linkedCredential;

    @Parameter(names = "-LinkedRoom")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean linkedRoom;

    @Parameter(names = "-Archive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean archive;

    @Parameter(names = "-ArchiveDatabase")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveDatabase;

    @Parameter(names = "-ArchiveGuid")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveGuid;

    @Parameter(names = "-ArchiveName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveName ;

    @Parameter(names = "-PublicFolder")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean publicFolder;

    @Parameter(names = "-HoldForMigration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean holdForMigration;

    @Parameter(names = "-Room")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean room;

    @Parameter(names = "-Shared")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean shared;
}
