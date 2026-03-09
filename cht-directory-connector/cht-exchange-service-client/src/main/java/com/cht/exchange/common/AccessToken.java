package com.cht.exchange.common;

import lombok.Data;

@Data
public class AccessToken {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String cope;
    private String jti;
}
