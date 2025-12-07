package com.example.baekseokmapbackend.map.dto;

import java.util.List;

public record BuildingDetailResponse(
        Integer buildingId,
        String name,
        Double latitude,
        Double longitude,
        String description,
        // ▼▼▼ [수정] 레코드에 이미지 URL 필드 추가 ▼▼▼
        String imageUrl,
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
        List<FloorResponse> floors
) {
    // ▼▼▼ [수정] Explicit Constructor 업데이트 (imageUrl 추가) ▼▼▼
    public BuildingDetailResponse(Integer buildingId, String name, Double latitude, Double longitude, String description, String imageUrl) {
        this(buildingId, name, latitude, longitude, description, imageUrl, List.of());
    }
    // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

    public BuildingDetailResponse withFloors(List<FloorResponse> floors) {
        // ▼▼▼ [수정] withFloors도 imageUrl을 포함하도록 업데이트 ▼▼▼
        return new BuildingDetailResponse(buildingId, name, latitude, longitude, description, imageUrl, floors);
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
    }
}