package com.example.baekseokmapbackend.config;

import com.example.baekseokmapbackend.security.JwtAuthenticationFilter; // 1. 필터 임포트
import com.example.baekseokmapbackend.security.JwtTokenProvider; // 2. JwtTokenProvider 임포트
import lombok.RequiredArgsConstructor; // 3. RequiredArgsConstructor 임포트
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 4. 필터 위치 지정을 위해 임포트

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 5. final 필드 주입을 위해 추가
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // 6. JwtTokenProvider 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 7. 우리가 만든 필터 주입

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 (만약 dev 프로필에서 쓴다면)
                        // (팀원이 Docker(MySQL)로 바꿨다면 이 줄은 없어도 무방합니다)
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // 8. ★★★★★ 가장 중요한 부분 ★★★★★
        // Spring Security의 기본 인증 필터(UsernamePasswordAuthenticationFilter)가 실행되기 전에,
        // 우리가 만든 'jwtAuthenticationFilter'를 먼저 실행하도록 순서를 지정합니다.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}