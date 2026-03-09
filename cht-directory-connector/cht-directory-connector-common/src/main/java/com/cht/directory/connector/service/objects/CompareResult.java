package com.cht.directory.connector.service.objects;

import java.io.Serializable;

/**
 * Created by sychuang on 2018/7/5.
 */
public class CompareResult implements Serializable
{
    private boolean result;
    private String logname;

    public CompareResult(boolean result)
    {
        this.result = result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public boolean getResult()
    {
        return result;
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
