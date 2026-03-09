package com.cht.directory.connector.service.error;

import com.unboundid.ldap.sdk.LDAPException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sychuang on 2018/7/2.
 */
public class LDAPError
{
    private Map<String, String> error;

    public LDAPError(String code, String message)
    {
        error = new HashMap(2);
        error.put("code", code);
        error.put("message", message);
    }

    public LDAPError(int code, String message)
    {
        this(String.valueOf(code), message);
    }

    public LDAPError(LDAPException e)
    {
        this(e.getResultCode().intValue(), e.getDiagnosticMessage() == null? e.getMessage() : e.getDiagnosticMessage());
    }

    public Map getError()
    {
        return error;
    }
}