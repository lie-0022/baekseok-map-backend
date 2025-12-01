package com.example.baekseokmapbackend.map.service;

import com.example.baekseokmapbackend.map.dto.AvailableRoomResponse;
import com.example.baekseokmapbackend.map.repository.FloorRepository;
import com.example.baekseokmapbackend.map.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FloorQueryService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;

    public List<AvailableRoomResponse> getAvailableRooms(Integer floorId,
                                                         Integer dayOfWeek,
                                                         LocalTime start,
                                                         LocalTime end) {
        if (!floorRepository.existsById(floorId)) {
            throw new NoSuchElementException("존재하지 않는 층입니다: " + floorId);
        }

        var now = LocalDateTime.now();
        int dow = (dayOfWeek != null) ? dayOfWeek : now.getDayOfWeek().getValue(); // 1=월
        LocalTime s = (start != null) ? start : now.toLocalTime();

        LocalTime e;
        if (end != null) {
            e = end;
        } else {
            // 종료 시간이 없으면 시작 시간 + 2시간으로 설정하되,
            // 자정을 넘어가면 그날의 끝(MAX)으로 설정하여 날짜 넘어감 문제 방지
            LocalTime tempEnd = s.plusHours(2);
            if (tempEnd.isBefore(s)) { // 예: 23:00 + 2시간 = 01:00 (다음날이 되므로 s보다 작아짐)
                e = LocalTime.MAX; // 23:59:59.999999999
            } else {
                e = tempEnd;
            }
        }

        // 종료 시간이 시작 시간보다 빨라야 함 (단, MAX인 경우는 예외 아님)
        if (!e.isAfter(s) && !e.equals(LocalTime.MAX)) {
            throw new IllegalArgumentException("end는 start 이후여야 합니다.");
        }

        return roomRepository.findAvailableClassrooms(floorId, dow, s, e);
    }
}