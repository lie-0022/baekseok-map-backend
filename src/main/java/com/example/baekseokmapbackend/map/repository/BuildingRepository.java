package com.example.baekseokmapbackend.map.repository;

import com.example.baekseokmapbackend.map.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Integer> {
    // JpaRepository<Building, Integer> 상속
    // <Building, Integer> 의미: 'Building' 엔티티를 다루며, PK 타입은 'Integer'이다.
    // 기본 CRUD (save, findById, findAll, delete 등)는 자동 제공됩니다.
}