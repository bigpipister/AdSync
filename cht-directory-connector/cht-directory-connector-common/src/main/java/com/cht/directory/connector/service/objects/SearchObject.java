package com.cht.directory.connector.service.objects;

import java.io.Serializable;

public class SearchObject implements Serializable
{
    private String baseDN;
    private int scope;
    private String filter;
    private String[] attributes;
//    private String[] binaryAttributes;
    // sort, page, vlv
    private int derefPolicy;
    private int sizeLimit;
    private int timeLimit;
    private boolean typesOnly;
//    private String output;

    private String logname;

    public void setBaseDN(String baseDN)
    {
        this.baseDN = baseDN;
    }

    public String getBaseDN()
    {
        return baseDN;
    }

    public void setScope(int scope)
    {
        this.scope = scope;
    }

    public int getScope()
    {
        return scope;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public String getFilter()
    {
        return filter;
    }

    public void setAttributes(String[] attributes)
    {
        this.attributes = attributes;
    }

    public String[] getAttributes()
    {
        return (attributes == null || attributes.length == 0) ? new String[]{"*"} : attributes;
    }

    public void setDerefPolicy(int derefPolicy)
    {
        this.derefPolicy = derefPolicy;
    }

    public int getDerefPolicy()
    {
        return derefPolicy;
    }

    public void setSizeLimit(int sizeLimit)
    {
        this.sizeLimit = sizeLimit;
    }

    public int getSizeLimit()
    {
        return sizeLimit;
    }

    public void setTimeLimit(int timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public int getTimeLimit()
    {
        return timeLimit;
    }

    public void setTypesOnly(boolean typesOnly)
    {
        this.typesOnly = typesOnly;
    }

    public boolean getTypesOnly()
    {
        return typesOnly;
    }

//    public void setOutput(String output)
//    {
//        this.output = output;
//    }

//    public String getOutput()
//    {
//        return output;
//    }

    public void setLogname(String logname)
    {
        this.logname = logname;
    }

    public String getLogname()
    {
        return logname;
    }
}
