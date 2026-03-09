package com.cht.directory.connector.service.utils;

public class StringUtils {

    public static boolean containsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }
}
