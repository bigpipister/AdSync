package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.service.PublishPwdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilterEventController {

    private static final Logger log = LoggerFactory.getLogger(FilterEventController.class);

    private PublishPwdService publishPwdService;

    public FilterEventController(PublishPwdService publishPwdService) {

        this.publishPwdService = publishPwdService;
    }

    // 接收 ad password filter DLL 丟過來的帳號密碼變更訊息
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity pwdSyncToDB(@RequestBody String body ) {
        //log.info(body);
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < body.length()) {
            char ch = body.charAt(i); // consume letter
            builder.append(ch);
            // 由於收到的是 json 格式，後面會進行 json parser，但 json 視 backslash 為 escape character，
            // 所以如果密碼有 backslash ，要多加一個 backslash
            if(ch == '\\') {
                builder.append('\\');
            }
            i++;
        }
        //log.info(builder.toString());

        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> message = new HashMap();
        try {
            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(builder.toString(), Map.class);
            //log.info(map.toString());

            if (map.get("name") == null || map.get("password") == null) {
                log.error("receive request but parameters not correct");
                return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
            } else {
                // 這裡攔到的username是ad上的sAMAccountName
                log.info("receive request => username: " + String.valueOf(map.get("name")) +
                        ", time: " + new Date());
                message.put("user_name", String.valueOf(map.get("name")));
                message.put("new_password", String.valueOf(map.get("password")));

                // 將密碼寫入IDM db
                publishPwdService.publish(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
