package com.cht.directory.connector.type;

public enum MOFType {

    FIA("FIA"), NTBT("A05"), NTBNA("H01"), NTBCA("B01"), NTBSA("D01"), NTBK("E01");

    private String type;
    private String rdn;

    MOFType(String type) {

        this.type = type;
        this.rdn = "OU=" + type + "ROOT";
    }

    public String getType() {

        return type;
    }

    public String getRdn() {

        return rdn;
    }
}
