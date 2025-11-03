package com.example.baekseokmapbackend.user.service;

import com.example.baekseokmapbackend.security.JwtTokenProvider; // 1. JwtTokenProvider 임포트
import com.example.baekseokmapbackend.user.domain.RefreshToken; // 2. RefreshToken 임포트
import com.example.baekseokmapbackend.user.domain.User;
import com.example.baekseokmapbackend.user.dto.LoginRequest; // 3. LoginRequest 임포트
import com.example.baekseokmapbackend.user.dto.SignUpRequest;
import com.example.baekseokmapbackend.user.dto.TokenResponse; // 4. TokenResponse 임포트
import com.example.baekseokmapbackend.user.repository.RefreshTokenRepository; // 5. RefreshTokenRepository 임포트
import com.example.baekseokmapbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider; // 6. 의존성 추가
    private final RefreshTokenRepository refreshTokenRepository; // 7. 의존성 추가

    /**
     * 회원가입 로직
     */
    @Transactional
    public Long signup(SignUpRequest request) {
        if (userRepository.findByStudentId(request.getStudent_id()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 학번입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = new User(
                request.getStudent_id(),
                encodedPassword,
                request.getNickname()
        );

        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    /**
     * 로그인 로직
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {
        // 1. 학번으로 유저 찾기
        User user = userRepository.findByStudentId(request.getStudent_id())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 학번입니다."));

        // 2. 비밀번호 검증 (입력된 비밀번호 vs DB의 암호화된 비밀번호)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getStudentId());
        String refreshTokenString = jwtTokenProvider.createRefreshToken();

        // 4. Refresh Token을 DB에 저장
        // (이미 해당 유저의 토큰이 있다면 업데이트, 없다면 새로 생성)
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user) // <-- 이 메소드 Repository에 추가 필요!
                .orElse(new RefreshToken(
                        user,
                        refreshTokenString,
                        jwtTokenProvider.getRefreshTokenValidityInSeconds()
                ));

        // 기존 토큰이 있다면 값만 업데이트
        if (refreshToken.getId() != null) {
            refreshToken.updateToken(
                    refreshTokenString,
                    jwtTokenProvider.getRefreshTokenValidityInSeconds()
            );
        }

        refreshTokenRepository.save(refreshToken);
        // 5. 토큰 반환
        return new TokenResponse(accessToken, refreshTokenString);
    }
}