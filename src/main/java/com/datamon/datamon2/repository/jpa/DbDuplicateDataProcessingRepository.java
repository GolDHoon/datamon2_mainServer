package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.DbDuplicateDataProcessingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbDuplicateDataProcessingRepository extends JpaRepository<DbDuplicateDataProcessingEntity, String> {
    List<DbDuplicateDataProcessingEntity> findByDbCode(String dbCode);
}