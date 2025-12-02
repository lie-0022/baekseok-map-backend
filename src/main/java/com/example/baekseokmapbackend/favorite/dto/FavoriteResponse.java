package com.example.baekseokmapbackend.favorite.dto;

import com.example.baekseokmapbackend.favorite.domain.Favorite;
import com.example.baekseokmapbackend.map.domain.Floor;
import com.example.baekseokmapbackend.map.domain.Room;
import lombok.Getter;

@Getter
public class FavoriteResponse {

    private Long favoriteId;
    private Integer roomId;
    private String roomNumber;
    private String roomName;

    private Integer buildingId;
    private String buildingName;
    private Integer floorId;
    private Integer floorLevel;

    // Favorite 엔티티를 FavoriteResponse DTO로 변환하는 생성자
    public FavoriteResponse(Favorite favorite) {
        Room room = favorite.getRoom();

        this.favoriteId = favorite.getId();
        this.roomId = room.getId();
        this.roomNumber = room.getRoomNumber();
        this.roomName = room.getName();

        // Room -> Floor -> Building 순서로 정보 가져오기
        Floor floor = room.getFloor();
        if (floor != null) {
            this.floorId = floor.getId();
            this.floorLevel = floor.getLevel();

            if (floor.getBuilding() != null) {
                this.buildingId = floor.getBuilding().getId();
                this.buildingName = floor.getBuilding().getName();
            }
        }
    }
}