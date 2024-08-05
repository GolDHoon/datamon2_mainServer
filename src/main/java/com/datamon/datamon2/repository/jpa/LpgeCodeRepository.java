package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.LpgeCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LpgeCodeRepository extends JpaRepository<LpgeCodeEntity, Integer> {
    Optional<LpgeCodeEntity> findByCodeValue(String codeValue);
    Optional<LpgeCodeEntity> findByCodeFullName(String codeFullName);
}