package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.LandingPageInfomationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandingPageInfomationRepository extends JpaRepository<LandingPageInfomationEntity, Integer> {
    Optional<LandingPageInfomationEntity> findByLpgeCode(String lpgeCode);
}