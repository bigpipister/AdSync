package com.cht.directory.connector.service.objects;

import java.io.Serializable;

/**
 * Created by sychuang on 2018/8/21.
 */
public class GetObject implements Serializable
{
    private String dn;
    private String[] attributes;
    private String[] binaryAttributes;

    private String logname;

    public void setDN(String dn)
    {
        this.dn = dn;
    }

    public String getDN()
    {
        return dn;
    }

    public void setAttributes(String[] attributes)
    {
        this.attributes = attributes;
    }

    public String[] getAttributes()
    {
        return attributes;
    }

    public void setBinaryAttributes(String[] binaryAttributes)
    {
        this.binaryAttributes = binaryAttributes;
    }

    public String[] getBinaryAttributes()
    {
        return binaryAttributes;
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
