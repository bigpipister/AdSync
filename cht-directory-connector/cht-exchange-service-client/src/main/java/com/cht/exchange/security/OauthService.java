package com.cht.exchange.security;

import com.cht.exchange.common.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OauthService {
    @Value("${exServer_ip:127.0.0.1:3888}")
    private String exServer_ip;

    @Value("${user_name:admin}")
    private String user_name;

    @Value("${user_pwd:admin}")
    private String user_pwd;

    @Value("${client_id:exchange_cmd_service_client}")
    private String client_id;

    @Value("${client_secret:exchange_cmd_service_secret}")
    private String client_secret;

    public String getToken() {
        String tokenGetUrl = "http://"+exServer_ip+"/adws/oauth/token";
        log.info("get access token from:" + tokenGetUrl);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        // username 與 password 是 spring security 需要的認證資訊
        map.add("username", user_name);
        map.add("password", user_pwd);
        // 下面這些是 spring oauth2 authorization 需要的資訊
        map.add("client_id", client_id);
        map.add("client_secret", client_secret);
        map.add("grant_type", "password");
        map.add("scope", "write");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccessToken> response =
                restTemplate.exchange(tokenGetUrl, HttpMethod.POST, entity, AccessToken.class);
        log.info("get access token:" + response.getBody().getAccess_token());
        return response.getBody().getAccess_token();
    }
}
