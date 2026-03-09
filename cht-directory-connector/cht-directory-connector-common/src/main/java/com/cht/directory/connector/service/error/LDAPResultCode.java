package com.cht.directory.connector.service.error;

import com.unboundid.ldap.sdk.ResultCode;

public enum LDAPResultCode {

    LDAP_SUCCESS(0), LDAP_ALREADY_EXISTS(
            ResultCode.ENTRY_ALREADY_EXISTS_INT_VALUE), LDAP_NO_SUCH_OBJECT(
                    ResultCode.NO_SUCH_OBJECT_INT_VALUE);

    private int code;

    private LDAPResultCode(int code) {

        this.code = code;
    }

    public int getCode() {

        return code;
    }
}
