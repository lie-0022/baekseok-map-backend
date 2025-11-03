package com.example.baekseokmapbackend.map.repository;

import com.example.baekseokmapbackend.map.domain.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Integer> {
    // JpaRepository<Floor, Integer> 상속
    // <Floor, Integer> 의미: 'Floor' 엔티티를 다루며, PK 타입은 'Integer'이다.
}