package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class SetMailbox {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-AcceptMessagesOnlyFrom")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String acceptMessagesOnlyFrom;

    @Parameter(names = "-AcceptMessagesOnlyFromDLMembers")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String acceptMessagesOnlyFromDLMembers;

    @Parameter(names = "-AcceptMessagesOnlyFromSendersOrMembers")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String acceptMessagesOnlyFromSendersOrMembers;

    @Parameter(names = "-AccountDisabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String accountDisabled;

    @Parameter(names = "-AddressBookPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String addressBookPolicy;

    @Parameter(names = "-Alias")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String alias;

    @Parameter(names = "-AntispamBypassEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String antispamBypassEnabled;

    @Parameter(names = "-ApplyMandatoryProperties")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean applyMandatoryProperties;

    @Parameter(names = "-Arbitration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean arbitration;

    @Parameter(names = "-ArbitrationMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String arbitrationMailbox;

    @Parameter(names = "-ArchiveDatabase")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveDatabase;

    @Parameter(names = "-ArchiveDomain")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveDomain;

    @Parameter(names = "-ArchiveName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveName;

    @Parameter(names = "-ArchiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveQuota;

    @Parameter(names = "-ArchiveStatus")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveStatus;

    @Parameter(names = "-ArchiveWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String archiveWarningQuota;

    @Parameter(names = "-AttributesToClear")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String attributesToClear;

    @Parameter(names = "-AuditAdmin")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String auditAdmin;

    @Parameter(names = "-AuditDelegate")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String auditDelegate;

    @Parameter(names = "-AuditEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String auditEnabled;

    @Parameter(names = "-AuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auditLog;

    @Parameter(names = "-AuditLogAgeLimit")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String auditLogAgeLimit;

    @Parameter(names = "-AuditOwner")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String auditOwner;

    @Parameter(names = "-AuxAuditLog")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean auxAuditLog;

    @Parameter(names = "-BypassModerationFromSendersOrMembers")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String bypassModerationFromSendersOrMembers;

    @Parameter(names = "-CalendarLoggingQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String calendarLoggingQuota;

    @Parameter(names = "-CalendarRepairDisabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String calendarRepairDisabled;

    @Parameter(names = "-CalendarVersionStoreDisabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String calendarVersionStoreDisabled;

    @Parameter(names = "-ClientExtensions")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String clientExtensions;

    @Parameter(names = "-Confirm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean confirm;

    @Parameter(names = "-CreateDTMFMap")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String createDTMFMap;

    @Parameter(names = "-CustomAttribute1")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute1;

    @Parameter(names = "-CustomAttribute2")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute2;

    @Parameter(names = "-CustomAttribute3")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute3;

    @Parameter(names = "-CustomAttribute4")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute4;

    @Parameter(names = "-CustomAttribute5")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute5;

    @Parameter(names = "-CustomAttribute6")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute6;

    @Parameter(names = "-CustomAttribute7")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute7;

    @Parameter(names = "-CustomAttribute8")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute8;

    @Parameter(names = "-CustomAttribute9")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute9;

    @Parameter(names = "-CustomAttribute10")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute10;

    @Parameter(names = "-CustomAttribute11")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute11;

    @Parameter(names = "-CustomAttribute12")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute12;

    @Parameter(names = "-CustomAttribute13")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute13;

    @Parameter(names = "-CustomAttribute14")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute14;

    @Parameter(names = "-CustomAttribute15")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String customAttribute15;

    @Parameter(names = "-Database")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String database;

    @Parameter(names = "-DefaultAuditSet")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String defaultAuditSet;

    @Parameter(names = "-DefaultPublicFolderMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String defaultPublicFolderMailbox;

    @Parameter(names = "-DeliverToMailboxAndForward")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String deliverToMailboxAndForward;

    @Parameter(names = "-DisableThrottling")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String disableThrottling;

    @Parameter(names = "-DisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String displayName;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-DowngradeHighPriorityMessagesEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String downgradeHighPriorityMessagesEnabled;

    @Parameter(names = "-DumpsterMessagesPerFolderCountReceiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer dumpsterMessagesPerFolderCountReceiveQuota;

    @Parameter(names = "-DumpsterMessagesPerFolderCountWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer dumpsterMessagesPerFolderCountWarningQuota;

    @Parameter(names = "-EmailAddresses")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String emailAddresses;

    @Parameter(names = "-EmailAddressPolicyEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String emailAddressPolicyEnabled;

    @Parameter(names = "-EnableRoomMailboxAccount")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String enableRoomMailboxAccount;

    @Parameter(names = "-EndDateForRetentionHold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String endDateForRetentionHold;

    @Parameter(names = "-ExtendedPropertiesCountQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer extendedPropertiesCountQuota;

    @Parameter(names = "-ExtensionCustomAttribute1")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String extensionCustomAttribute1;

    @Parameter(names = "-ExtensionCustomAttribute2")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String extensionCustomAttribute2;

    @Parameter(names = "-ExtensionCustomAttribute3")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String extensionCustomAttribute3;

    @Parameter(names = "-ExtensionCustomAttribute4")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String extensionCustomAttribute4;

    @Parameter(names = "-ExtensionCustomAttribute5")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String extensionCustomAttribute5;

    @Parameter(names = "-ExternalOofOptions")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String externalOofOptions;

    @Parameter(names = "-FolderHierarchyChildrenCountReceiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer folderHierarchyChildrenCountReceiveQuota;

    @Parameter(names = "-FolderHierarchyChildrenCountWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer folderHierarchyChildrenCountWarningQuota;

    @Parameter(names = "-FolderHierarchyDepthReceiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer folderHierarchyDepthReceiveQuota;

    @Parameter(names = "-FolderHierarchyDepthWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer folderHierarchyDepthWarningQuota;

    @Parameter(names = "-FoldersCountReceiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer foldersCountReceiveQuota;

    @Parameter(names = "-FoldersCountWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer foldersCountWarningQuota;

    @Parameter(names = "-Force")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean force;

    @Parameter(names = "-ForwardingAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String forwardingAddress;

    @Parameter(names = "-ForwardingSmtpAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String forwardingSmtpAddress;

    @Parameter(names = "-GMGen")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String gMGen;

    @Parameter(names = "-GrantSendOnBehalfTo")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String grantSendOnBehalfTo;

    @Parameter(names = "-GroupMailbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String groupMailbox;

    @Parameter(names = "-HiddenFromAddressListsEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String hiddenFromAddressListsEnabled;

    @Parameter(names = "-IgnoreDefaultScope")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreDefaultScope;

    @Parameter(names = "-ImListMigrationCompleted")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String imListMigrationCompleted;

    @Parameter(names = "-ImmutableId")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String immutableId;

    @Parameter(names = "-IsExcludedFromServingHierarchy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String isExcludedFromServingHierarchy;

    @Parameter(names = "-IsHierarchyReady")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String isHierarchyReady;

    @Parameter(names = "-IsHierarchySyncEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String isHierarchySyncEnabled;

    @Parameter(names = "-IssueWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String issueWarningQuota;

    @Parameter(names = "-Languages")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String languages;

    @Parameter(names = "-LinkedCredential")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String linkedCredential;

    @Parameter(names = "-LinkedDomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String linkedDomainController;

    @Parameter(names = "-LinkedMasterAccount")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String linkedMasterAccount;

    @Parameter(names = "-LitigationHoldDate")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String litigationHoldDate;

    @Parameter(names = "-LitigationHoldDuration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String litigationHoldDuration;

    @Parameter(names = "-LitigationHoldEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String litigationHoldEnabled;

    @Parameter(names = "-LitigationHoldOwner")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String litigationHoldOwner;

    @Parameter(names = "-MailboxMessagesPerFolderCountReceiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer mailboxMessagesPerFolderCountReceiveQuota;

    @Parameter(names = "-MailboxMessagesPerFolderCountWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer mailboxMessagesPerFolderCountWarningQuota;

    @Parameter(names = "-MailTip")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String mailTip;

    @Parameter(names = "-MailTipTranslations")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String mailTipTranslations;

    @Parameter(names = "-ManagedFolderMailboxPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String managedFolderMailboxPolicy;

    @Parameter(names = "-ManagedFolderMailboxPolicyAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean managedFolderMailboxPolicyAllowed;

    @Parameter(names = "-Management")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String management;

    @Parameter(names = "-MaxBlockedSenders")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer maxBlockedSenders;

    @Parameter(names = "-MaxReceiveSize")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String maxReceiveSize;

    @Parameter(names = "-MaxSafeSenders")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer maxSafeSenders;

    @Parameter(names = "-MaxSendSize")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String maxSendSize;

    @Parameter(names = "-MessageCopyForSendOnBehalfEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String messageCopyForSendOnBehalfEnabled;

    @Parameter(names = "-MessageCopyForSentAsEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String messageCopyForSentAsEnabled;

    @Parameter(names = "-MessageTracking")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String messageTracking;

    @Parameter(names = "-MessageTrackingReadStatusEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String messageTrackingReadStatusEnabled;

    @Parameter(names = "-Migration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String migration;

    @Parameter(names = "-ModeratedBy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String moderatedBy;

    @Parameter(names = "-ModerationEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String moderationEnabled;

    @Parameter(names = "-Name")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String name;

    @Parameter(names = "-NewPassword")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String newPassword;

    @Parameter(names = "-OABGen")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String oABGen;

    @Parameter(names = "-Office")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String office;

    @Parameter(names = "-OfflineAddressBook")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String offlineAddressBook;

    @Parameter(names = "-OldPassword")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String oldPassword;

    @Parameter(names = "-OMEncryption")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String oMEncryption;

    @Parameter(names = "-OMEncryptionStore")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String oMEncryptionStore;

    @Parameter(names = "-Password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String password;

    @Parameter(names = "-PrimarySmtpAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String primarySmtpAddress;

    @Parameter(names = "-ProhibitSendQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String prohibitSendQuota;

    @Parameter(names = "-ProhibitSendReceiveQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String prohibitSendReceiveQuota;

    @Parameter(names = "-PstProvider")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String pstProvider;

    @Parameter(names = "-PublicFolder")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean publicFolder;

    @Parameter(names = "-QueryBaseDN")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String queryBaseDN;

    @Parameter(names = "-RecipientLimits")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String recipientLimits;

    @Parameter(names = "-RecoverableItemsQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String recoverableItemsQuota;

    @Parameter(names = "-RecoverableItemsWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String recoverableItemsWarningQuota;

    @Parameter(names = "-RejectMessagesFrom")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String rejectMessagesFrom;

    @Parameter(names = "-RejectMessagesFromDLMembers")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String rejectMessagesFromDLMembers;

    @Parameter(names = "-RejectMessagesFromSendersOrMembers")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String rejectMessagesFromSendersOrMembers;

    @Parameter(names = "-RemoteRecipientType")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String remoteRecipientType ;

    @Parameter(names = "-RemoveManagedFolderAndPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean removeManagedFolderAndPolicy;

    @Parameter(names = "-RemovePicture")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean removePicture;

    @Parameter(names = "-RemoveSpokenName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean removeSpokenName;

    @Parameter(names = "-RequireSenderAuthenticationEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String requireSenderAuthenticationEnabled;

    @Parameter(names = "-ResetPasswordOnNextLogon")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String resetPasswordOnNextLogon;

    @Parameter(names = "-ResourceCustom")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer resourceCustom;

    @Parameter(names = "-RetainDeletedItemsFor")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retainDeletedItemsFor;

    @Parameter(names = "-RetainDeletedItemsUntilBackup")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retainDeletedItemsUntilBackup;

    @Parameter(names = "-RetentionComment")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retentionComment ;

    @Parameter(names = "-RetentionHoldEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retentionHoldEnabled;

    @Parameter(names = "-RetentionPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retentionPolicy;

    @Parameter(names = "-RetentionUrl")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String retentionUrl;

    @Parameter(names = "-RoleAssignmentPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String roleAssignmentPolicy;

    @Parameter(names = "-RoomMailboxPassword")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String roomMailboxPassword;

    @Parameter(names = "-RulesQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String rulesQuota;

    @Parameter(names = "-SamAccountName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String samAccountName;

    @Parameter(names = "-SCLDeleteEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sCLDeleteEnabled;

    @Parameter(names = "-SCLDeleteThreshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer sCLDeleteThreshold;

    @Parameter(names = "-SCLJunkEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sCLJunkEnabled;

    @Parameter(names = "-SCLJunkThreshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer sCLJunkThreshold;

    @Parameter(names = "-SCLQuarantineEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sCLQuarantineEnabled;

    @Parameter(names = "-SCLQuarantineThreshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer sCLQuarantineThreshold;

    @Parameter(names = "-SCLRejectEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sCLRejectEnabled;

    @Parameter(names = "-SCLRejectThreshold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer sCLRejectThreshold;

    @Parameter(names = "-SecondaryAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String secondaryAddress;

    @Parameter(names = "-SecondaryDialPlan")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String secondaryDialPlan;

    @Parameter(names = "-SendModerationNotifications")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sendModerationNotifications;

    @Parameter(names = "-SharingPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sharingPolicy;

    @Parameter(names = "-SimpleDisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String simpleDisplayName;

    @Parameter(names = "-SingleItemRecoveryEnabled")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String singleItemRecoveryEnabled;

    @Parameter(names = "-StartDateForRetentionHold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String startDateForRetentionHold;

    @Parameter(names = "-StsRefreshTokensValidFrom")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String stsRefreshTokensValidFrom;

    @Parameter(names = "-SystemMessageSizeShutoffQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer systemMessageSizeShutoffQuota;

    @Parameter(names = "-SystemMessageSizeWarningQuota")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Integer systemMessageSizeWarningQuota;

    @Parameter(names = "-ThrottlingPolicy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String throttlingPolicy;

    @Parameter(names = "-Type")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String type ;

    @Parameter(names = "-UMDataStorage")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String uMDataStorage;

    @Parameter(names = "-UMDtmfMap")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String uMDtmfMap;

    @Parameter(names = "-UMGrammar")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String uMGrammar;

    @Parameter(names = "-UseDatabaseQuotaDefaults")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String useDatabaseQuotaDefaults;

    @Parameter(names = "-UseDatabaseRetentionDefaults")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String useDatabaseRetentionDefaults;

    @Parameter(names = "-UserCertificate")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String userCertificate;

    @Parameter(names = "-UserPrincipalName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String userPrincipalName;

    @Parameter(names = "-UserSMimeCertificate")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String userSMimeCertificate;

    @Parameter(names = "-WhatIf")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean whatIf;

    @Parameter(names = "-WindowsEmailAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String windowsEmailAddress;
}
