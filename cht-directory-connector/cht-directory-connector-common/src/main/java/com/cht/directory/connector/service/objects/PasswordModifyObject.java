package com.cht.directory.connector.service.objects;

import java.io.Serializable;

public class PasswordModifyObject implements Serializable
{
    private String userIdentity;
    private String oldPassword;
    private String newPassword;

    private String logname;

    public void setUserIdentity(String userIdentity)
    {
        this.userIdentity = userIdentity;
    }

    public String getUserIdentity()
    {
        return userIdentity;
    }

    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    public String getOldPassword()
    {
        return oldPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setLogname(String logname)
    {
        this.logname = logname;
    }

    public String getLogname()
    {
        return logname;
    }
}
