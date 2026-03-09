package com.cht.directory.connector.service.objects;

import java.io.Serializable;

/**
 * Created by sychuang on 2018/8/21.
 */
public class CompareObject implements Serializable
{
    private String dn;
    private String attribute;
    private String value;
    private boolean binary;

    private String logname;

    public void setDN(String dn)
    {
        this.dn = dn;
    }

    public String getDN()
    {
        return dn;
    }

    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setBinary(boolean binary)
    {
        this.binary = binary;
    }

    public boolean getBinary()
    {
        return binary;
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
