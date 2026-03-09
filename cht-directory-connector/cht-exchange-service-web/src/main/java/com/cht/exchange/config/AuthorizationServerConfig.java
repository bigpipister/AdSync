package com.cht.exchange.config;

import com.cht.exchange.service.AuthService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("exchange_cmd_service_client")
                .clientSecret(passwordEncoder.encode("exchange_cmd_service_secret"))
                .scope("read")
                .scope("write")
                .authorizationGrantType(AuthorizationGrantType.PASSWORD) // 這需要額外實作支援
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(600)) // 10 分鐘
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(List.of(provider));
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return authService;
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RegisteredClientRepository clients) {
        return new org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            // 可以自訂 claim，例如：
            // context.getClaims().claim("custom-claim", "value");
        };
    }

    // JWKSource 實作 (使用 RSA 金鑰)
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        return (jwkSelector, context) -> jwkSelector.select(new JWKSet(Jwks.generateRsa()));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}
