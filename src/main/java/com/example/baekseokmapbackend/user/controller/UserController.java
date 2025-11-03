package com.example.baekseokmapbackend.user.controller;

import com.example.baekseokmapbackend.user.dto.LoginRequest;
import com.example.baekseokmapbackend.user.dto.SignUpRequest;
import com.example.baekseokmapbackend.user.dto.TokenResponse;
import com.example.baekseokmapbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스가 REST API 컨트롤러임을 알립니다.
@RequestMapping("/api/auth") // 이 컨트롤러의 모든 메소드는 /api/auth 경로로 시작합니다.
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     * (POST /api/auth/signup)
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    /**
     * 로그인 API
     * (POST /api/auth/login)
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse token = userService.login(request);
        return ResponseEntity.ok(token); // 200 OK와 함께 토큰 응답
    }
}