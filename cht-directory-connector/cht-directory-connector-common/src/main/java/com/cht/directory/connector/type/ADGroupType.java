package com.cht.directory.connector.type;

public enum ADGroupType {

    UNIVERSAL_SECURITY_GROUP("2147483656", -2147483640L, ""), GLOBAL_SECURITY_GROUP("2147483650",
            -2147483646L, "-G"), DOMAINLOCAL_SECURITY_GROUP("2147483652", -2147483644L,
                    "-DL"), UNKNOWN("9999", 9999L, "");

    private String type;
    private long adtype;
    private String suffix;

    ADGroupType(String type, long adtype, String suffix) {

        this.type = type;
        this.adtype = adtype;
        this.suffix = suffix;
    }

    public String getType() {

        return type;
    }

    public long getAdtype() {

        return adtype;
    }

    public String getSuffix() {

        return suffix;
    }

    public static ADGroupType findByAdType(long adtype) {

        if (adtype == UNIVERSAL_SECURITY_GROUP.adtype)
            return UNIVERSAL_SECURITY_GROUP;
        if (adtype == ADGroupType.GLOBAL_SECURITY_GROUP.adtype)
            return GLOBAL_SECURITY_GROUP;
        if (adtype == ADGroupType.DOMAINLOCAL_SECURITY_GROUP.adtype)
            return DOMAINLOCAL_SECURITY_GROUP;

        return UNKNOWN;
    }
}
