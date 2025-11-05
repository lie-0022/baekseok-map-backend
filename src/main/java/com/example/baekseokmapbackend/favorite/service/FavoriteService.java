package com.example.baekseokmapbackend.favorite.service;

import com.example.baekseokmapbackend.favorite.domain.Favorite;
import com.example.baekseokmapbackend.favorite.dto.FavoriteAddRequest;
import com.example.baekseokmapbackend.favorite.dto.FavoriteResponse;
import com.example.baekseokmapbackend.favorite.repository.FavoriteRepository;
import com.example.baekseokmapbackend.map.domain.Room;
import com.example.baekseokmapbackend.map.repository.RoomRepository;
import com.example.baekseokmapbackend.user.domain.User;
import com.example.baekseokmapbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // 1. List 임포트 추가
import java.util.stream.Collectors; // 2. Collectors 임포트 추가

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    /**
     * 즐겨찾기 추가 로직
     */
    @Transactional
    public Long addFavorite(FavoriteAddRequest request) {
        // 1. 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String studentId = authentication.getName(); // JWT 필터에서 저장한 학번

        // 2. 사용자 엔티티 조회
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 강의실 엔티티 조회
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("강의실을 찾을 수 없습니다."));

        // 4. 이미 즐겨찾기에 추가되었는지 확인
        favoriteRepository.findByUserAndRoom(user, room)
                .ifPresent(favorite -> {
                    throw new IllegalArgumentException("이미 즐겨찾기에 추가된 항목입니다.");
                });

        // 5. 즐겨찾기 엔티티 생성 및 저장
        Favorite newFavorite = new Favorite(user, room);
        Favorite savedFavorite = favoriteRepository.save(newFavorite);

        return savedFavorite.getId();
    }

    /**
     * 즐겨찾기 목록 조회 로직
     */
    public List<FavoriteResponse> getFavorites() {
        // 1. 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String studentId = authentication.getName();

        // 2. 사용자 엔티티 조회
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 해당 사용자의 모든 즐겨찾기 목록을 DB에서 조회
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);

        // 4. List<Favorite>를 List<FavoriteResponse>로 변환 (DTO 사용)
        return favorites.stream()
                .map(FavoriteResponse::new) // .map(favorite -> new FavoriteResponse(favorite))
                .collect(Collectors.toList());
    }
}