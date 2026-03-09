package com.cht.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class ResourceServerConfiguration {

    /**
     * 設定 JWT 驗證用的 filter chain
     */
    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/exchange/**").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())) // 可省略，若已定義 bean
                );

        return http.build();
    }

    /**
     * JWT 驗證用的解碼器
     * 如果你使用對稱金鑰 (signingKey)，可以用這個方法建出來
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = "chtKey"; // ✅ 這要夠長，建議至少 32 位元組 (256 bit)
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}