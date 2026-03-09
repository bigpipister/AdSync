package com.cht.directory.connector.service.objects;

import java.io.Serializable;

public class ModifyDNObject implements Serializable
{
    private String dn;
    private String newRDN;
    private boolean deleteOldRDN;
    private String newSuperiorDN;

    private String logname;

    public void setDN(String dn)
    {
        this.dn = dn;
    }

    public String getDN()
    {
        return dn;
    }

    public void setNewRDN(String newRDN)
    {
        this.newRDN = newRDN;
    }

    public String getNewRDN()
    {
        return newRDN;
    }

    public void setDeleteOldRDN(boolean dn)
    {
        this.deleteOldRDN = deleteOldRDN;
    }

    public boolean getDeleteOldRDN()
    {
        return deleteOldRDN;
    }

    public void setNewSuperiorDN(String newSuperiorDN)
    {
        this.newSuperiorDN = newSuperiorDN;
    }

    public String getNewSuperiorDN()
    {
        return newSuperiorDN;
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
