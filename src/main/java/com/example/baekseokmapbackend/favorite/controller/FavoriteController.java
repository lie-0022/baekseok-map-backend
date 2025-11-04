package com.example.baekseokmapbackend.favorite.controller;

import com.example.baekseokmapbackend.favorite.dto.FavoriteAddRequest;
import com.example.baekseokmapbackend.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites") // /api/favorites 경로의 요청을 처리
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
}