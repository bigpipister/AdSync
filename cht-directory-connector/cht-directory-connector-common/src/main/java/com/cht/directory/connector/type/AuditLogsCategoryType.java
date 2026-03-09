package com.cht.directory.connector.type;

public enum AuditLogsCategoryType {

    UserManagement_CreateUser("UserManagement", "Create user"),

    UserManagement_ActivateUser("UserManagement", "Activate user"),

    UserManagement_SuspendUser("UserManagement", "Suspend user"),

    UserManagement_MarkUser("UserManagement", "Mark user"),

    UserManagement_DeleteUser("UserManagement", "Delete user"),

    UserManagement_DeleteUserOverMaxLimit("UserManagement", "Delete user counts over max limit"),

    UserManagement_DeletePhantomUser("UserManagement", "Delete phantom user"),

    UserManagement_MoveUser("UserManagement", "Move user"),

    UserManagement_UpdateUserAttribute("UserManagement", "Update user attribute"),

    UserManagement_AddMemberToGroup("UserManagement", "Add member to group"),

    UserManagement_RemoveMemberFromGroup("UserManagement", "Remove member from group"),

    UserManagement_EnableMailbox("UserManagement", "Enable-Mailbox"),

    UserManagement_DisableMailbox("UserManagement", "Disable-Mailbox"),

    UserManagement_UnlockUser("UserManagement", "Unlock user"),

    PasswordManagement_UserPasswordChanges("PasswordManagement", "User password changes"),

    PasswordManagement_ForcePasswordSync("PasswordManagement", "Force password sync"),

    OrganizationalUnitManagement_CreateOrganizationalUnit("OrganizationalUnitManagement",
            "Create organizational unit"),

    OrganizationalUnitManagement_DeleteOrganizationalUnit("OrganizationalUnitManagement",
            "Delete organizational unit"),

    OrganizationalUnitManagement_UpdateOrganizationalUnitAttribute("OrganizationalUnitManagement",
            "Update organizational unit attribute"),

    GroupManagement_CreateGroup("GroupManagement", "Create group"),

    GroupManagement_DeleteGroup("GroupManagement", "Delete group"),

    GroupManagement_MarkGroup("GroupManagement", "Mark group"),

    GroupManagement_DeletePhantomGroup("GroupManagement", "Delete phantom group"),

    GroupManagement_AddMemeber("GroupManagement", "Add member"),

    GroupManagement_RemoveMemeber("GroupManagement", "Remove member"),

    GroupManagement_UpdateGroupAttribute("GroupManagement", "Update group attribute"),

    GroupManagement_EnableDistributionGroup("GroupManagement", "Enable-DistributionGroup"),

    GroupManagement_DisableDistributionGroup("GroupManagement", "Disable-DistributionGroup"),

    Other("Other", "");

    private String category;
    private String activityDisplayName;

    AuditLogsCategoryType(String category, String activityDisplayName) {

        this.category = category;
        this.activityDisplayName = activityDisplayName;
    }

    public String getCategory() {

        return category;
    }

    public String getActivityDisplayName() {

        return activityDisplayName;
    }
}
