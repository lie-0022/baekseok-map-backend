package com.example.baekseokmapbackend.map.repository;

import com.example.baekseokmapbackend.map.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    /**
     * 특정 층(floorId)에 속하고,
     * 이름이나 방 번호에 검색어(query)가 포함된 Room 목록을 찾는 메소드
     */
    @Query("SELECT r FROM Room r WHERE r.floor.id = :floorId AND (r.name LIKE %:query% OR r.roomNumber LIKE %:query%)")
    List<Room> findByFloorIdAndNameOrRoomNumberContaining(@Param("floorId") Integer floorId, @Param("query") String query);

    /**
     * 이름이나 방 번호에 검색어(query)가 포함된 Room 목록을 찾는 메소드 (통합 검색용)
     */
    List<Room> findByNameContainingOrRoomNumberContaining(String name, String roomNumber);
}