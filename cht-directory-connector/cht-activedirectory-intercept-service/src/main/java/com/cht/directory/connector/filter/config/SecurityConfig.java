//package gov.fia.ad.pwd.filter.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 允許所有請求
//                .csrf(csrf -> csrf.disable()); // 關閉 CSRF（可選）
//        return http.build();
//    }
//}