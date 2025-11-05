package com.example.baekseokmapbackend.favorite.controller;

import com.example.baekseokmapbackend.favorite.dto.FavoriteAddRequest;
import com.example.baekseokmapbackend.favorite.dto.FavoriteResponse; // 1. FavoriteResponse 임포트
import com.example.baekseokmapbackend.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping; // 2. GetMapping 임포트
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List; // 3. List 임포트

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 즐겨찾기 추가 API
     * (POST /api/favorites)
     */
    @PostMapping
    public ResponseEntity<String> addFavorite(@RequestBody FavoriteAddRequest request) {
        favoriteService.addFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("즐겨찾기에 추가되었습니다.");
    }

    /**
     * 즐겨찾기 목록 조회 API
     * (GET /api/favorites)
     */
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        List<FavoriteResponse> favorites = favoriteService.getFavorites();
        return ResponseEntity.ok(favorites); // 200 OK와 함께 목록 반환
    }
}