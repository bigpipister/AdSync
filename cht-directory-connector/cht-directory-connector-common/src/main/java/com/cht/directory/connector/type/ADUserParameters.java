package com.cht.directory.connector.type;

public enum ADUserParameters {

    ACTIVE(512), SUSPEND(514), DELETE(514), UNKNOWN(999);

    private int code;

    ADUserParameters(int code) {

        this.code = code;
    }

    public int getCode() {

        return code;
    }

    public static ADUserParameters getByCode(int code) {

        for (ADUserParameters e : values()) {
            if (e.code == code)
                return e;
        }
        return UNKNOWN;
    }
}
