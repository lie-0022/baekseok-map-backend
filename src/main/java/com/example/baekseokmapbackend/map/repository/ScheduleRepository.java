package com.example.baekseokmapbackend.map.repository;

import com.example.baekseokmapbackend.map.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    /**
     * 특정 요일과 시간대에 겹치는 강의실 ID 목록을 찾는 메소드
     * '빈 강의실 찾기' 기능의 핵심입니다.
     */
    @Query("SELECT s.room.id FROM Schedule s " +
            "WHERE s.dayOfWeek = :dayOfWeek " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime")
    List<Integer> findBookedRoomIdsByTime(
            @Param("dayOfWeek") int dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}