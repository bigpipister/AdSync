package com.cht.exchange.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    /**
     *將傳進來的json格式化
     * @param  requestBody  接收到的json data
     * @return 回傳格式化的結果
     */
    public static LinkedHashMap<String,String> jsonFormatter(LinkedHashMap<String,Object> requestBody) {
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
        for (Map.Entry entry : requestBody.entrySet()) {
            if (entry.getValue() instanceof Boolean && (Boolean)entry.getValue()) {
                paramMap.put((String)entry.getKey(), "");
            } else if (((String)entry.getKey()).equalsIgnoreCase("Confirm")) {
                paramMap.put((String)entry.getKey(), "$"+(Boolean)entry.getValue());
            } else if (entry.getValue() instanceof String){
                paramMap.put((String)entry.getKey(), "\'" + (entry.getValue()) + "\'");
            } else {
                paramMap.put((String)entry.getKey(), String.valueOf((entry.getValue())));
            }
        }
        return paramMap;
    }

    /**
     *組出完整PowerShell的執行指令
     * @param  command  PowerShell指令名稱
     * @param params PowerShell指令參數
     * @return 回傳完整指令
     */
    public static String genCommand(String command, Map<String,String> params) {
        StringBuilder sb = new StringBuilder();
        String commandParams = "";
        sb.append(command);
        for (String key : params.keySet()) {
            if (key.equalsIgnoreCase("CommonParameters")) {
                commandParams = params.get(key);
            } else if (key.equalsIgnoreCase("Confirm")) {
                sb.append(" -" + key);
                sb.append(":" + params.get(key));
            } else {
                sb.append(" -" + key);
                sb.append(" " + params.get(key));
            }
        }
        sb.append(" " + commandParams);
        return sb.toString();
    }
}
