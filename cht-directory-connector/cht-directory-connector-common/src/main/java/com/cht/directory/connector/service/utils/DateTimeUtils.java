package com.cht.directory.connector.service.utils;

import java.sql.Timestamp;

public class DateTimeUtils {

    public static Timestamp msADtoTimestamp(String timeIn100Nanos) {

        return new Timestamp((Long.parseLong(timeIn100Nanos) - 0x19db1ded53e8000L) / 10000);
    }

    public static String timestamptoMsAD(Timestamp timestamp) {

        return timestamp.getTime() * 10000 + 0x19db1ded53e8000L + "";
    }
}
