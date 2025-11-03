package com.example.baekseokmapbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean // 이 메소드가 반환하는 객체를 Spring이 관리하도록 등록
    public PasswordEncoder passwordEncoder() {
        // BCrypt는 가장 많이 사용되는 강력한 암호화 방식입니다.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF, HTTP Basic, Form Login 비활성화 (JWT 사용을 위함)
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                // 2. 세션 정책을 STATELESS로 설정 (JWT는 세션을 사용하지 않음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. API 경로별 접근 권한 설정
                .authorizeHttpRequests(authz -> authz
                        // '/api/auth/**' 경로는 회원가입/로그인이므로 모두 허용
                        .requestMatchers("/api/auth/**").permitAll()
                        // H2 콘솔 접근 허용 (개발용)
                        .requestMatchers("/h2-console/**").permitAll()
                        // 그 외 모든 요청은 인증(로그인)이 필요함
                        .anyRequest().authenticated()
                )

                // H2 콘솔은 iframe을 사용하므로 X-Frame-Options 헤더 비활성화
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}