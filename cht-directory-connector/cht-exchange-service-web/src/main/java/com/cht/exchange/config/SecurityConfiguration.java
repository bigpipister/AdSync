package com.cht.exchange.config;

import com.cht.exchange.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Spring Security 6 替代 @EnableGlobalMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthService authService;

    /**
     * 提供密碼加密（可替換為 BCryptPasswordEncoder）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return Objects.equals(rawPassword.toString(), encodedPassword);
            }
        };
    }

    /**
     * 設定 Security Filter Chain（取代 configure(HttpSecurity)）
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // for h2 console
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2/**")
                        .disable()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                );

        return http.build();
    }

    /**
     * 提供 AuthenticationManager（取代 authenticationManagerBean）
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        var daoAuthProvider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(userDetailsService);
        daoAuthProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(daoAuthProvider));
    }

    /**
     * 提供 UserDetailsService 實作（也可以直接使用 authService 當作 Bean）
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return authService;
    }
}
