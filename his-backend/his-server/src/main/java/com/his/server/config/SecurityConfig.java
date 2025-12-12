package com.his.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF，因为是 API 服务
            .authorizeHttpRequests(auth -> auth
                // 开发阶段放行所有接口，方便测试业务逻辑
                // 后续集成 JWT 时再改为 .authenticated()
                .requestMatchers("/**").permitAll()
            );
        return http.build();
    }
}
