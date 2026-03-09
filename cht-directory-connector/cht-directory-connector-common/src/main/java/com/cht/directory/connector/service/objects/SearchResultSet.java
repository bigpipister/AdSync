package com.cht.directory.connector.service.objects;

import java.io.Serializable;
import java.util.List;

public class SearchResultSet implements Serializable
{
    private List result;

    public void setResult(List result)
    {
        this.result = result;
    }

    public List getResult()
    {
        return result;
    }
}
