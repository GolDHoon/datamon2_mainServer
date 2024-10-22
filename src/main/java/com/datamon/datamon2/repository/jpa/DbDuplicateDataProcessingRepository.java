package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.DbDuplicateDataProcessingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DbDuplicateDataProcessingRepository extends JpaRepository<DbDuplicateDataProcessingEntity, String> {
    List<DbDuplicateDataProcessingEntity> findByDbCode(String dbCode);
}