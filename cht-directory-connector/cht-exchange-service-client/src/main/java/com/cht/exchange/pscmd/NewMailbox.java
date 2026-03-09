package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class NewMailbox {

    @Parameter(names = "-Name")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String name;

    @Parameter(names = "-Password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String password;

    @Parameter(names = "-UserPrincipalName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String userPrincipalName;

    @Parameter(names = "-InactiveMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String inactiveMailbox;

    @Parameter(names = "-RemovedMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String removedMailbox;

    @Parameter(names = "-ActiveSyncMailboxPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String activeSyncMailboxPolicy;

    @Parameter(names = "-AddressBookPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String addressBookPolicy ;

    @Parameter(names = "-Alias")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String alias;

    @Parameter(names = "-ArbitrationMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String arbitrationMailbox;

    @Parameter(names = "-Archive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean archive;

    @Parameter(names = "-ArchiveDatabase")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveDatabase;

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
    String domainController ;

    @Parameter(names = "-FirstName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String firstName;

    @Parameter(names = "-Force")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean force;

    @Parameter(names = "-ImmutableId")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String immutableId ;

    @Parameter(names = "-Initials")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String initials ;

    @Parameter(names = "-LastName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String lastName;

    @Parameter(names = "-MailboxPlan")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String mailboxPlan;

    @Parameter(names = "-MailboxRegion")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String mailboxRegion ;

    @Parameter(names = "-ManagedFolderMailboxPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String managedFolderMailboxPolicy;

    @Parameter(names = "-ManagedFolderMailboxPolicyAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean managedFolderMailboxPolicyAllowed;

    @Parameter(names = "-ModeratedBy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String moderatedBy;

    @Parameter(names = "-ModerationEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String moderationEnabled;

    @Parameter(names = "-OrganizationalUnit")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String organizationalUnit;

    @Parameter(names = "-PrimarySmtpAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String primarySmtpAddress;

    @Parameter(names = "-RemotePowerShellEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String remotePowerShellEnabled;

    @Parameter(names = "-ResetPasswordOnNextLogon")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String resetPasswordOnNextLogon;

    @Parameter(names = "-RetentionPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retentionPolicy;

    @Parameter(names = "-RoleAssignmentPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String roleAssignmentPolicy;

    @Parameter(names = "-SamAccountName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String samAccountName;

    @Parameter(names = "-SendModerationNotifications")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sendModerationNotifications;

    @Parameter(names = "-SharingPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sharingPolicy;

    @Parameter(names = "-TargetAllMDBs")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean targetAllMDBs;

    @Parameter(names = "-ThrottlingPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String throttlingPolicy;

    @Parameter(names = "-WhatIf")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean whatIf;

    @Parameter(names = "-MicrosoftOnlineServicesID")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String microsoftOnlineServicesID;

    @Parameter(names = "-AuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auditLog;

    @Parameter(names = "-AuxAuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auxAuditLog;

    @Parameter(names = "-AccountDisabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean accountDisabled;

    @Parameter(names = "-Discovery")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean discovery;

    @Parameter(names = "-EnableRoomMailboxAccount")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String enableRoomMailboxAccount;

    @Parameter(names = "-Room")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean room;

    @Parameter(names = "-RoomMailboxPassword")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String roomMailboxPassword;

    @Parameter(names = "-ResourceCapacity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer resourceCapacity;
}
