package com.example.baekseokmapbackend.map.dto;

public record BuildingResponse(
        Integer buildingId,
        String name,
        Double latitude,
        Double longitude,
        // ▼▼▼ [추가] 이미지 URL 필드 추가 ▼▼▼
        String imageUrl
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
) {
}