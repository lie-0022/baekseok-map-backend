package com.example.baekseokmapbackend.user.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    // ▼▼▼ 추가된 필드 ▼▼▼
    private String studentId;
    private String nickname;

    public TokenResponse(String accessToken, String refreshToken, String studentId, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.studentId = studentId;
        this.nickname = nickname;
    }
}