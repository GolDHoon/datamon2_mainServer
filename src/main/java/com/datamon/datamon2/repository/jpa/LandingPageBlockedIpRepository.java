package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.LandingPageBlockedIpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandingPageBlockedIpRepository extends JpaRepository<LandingPageBlockedIpEntity, Integer> {
    List<LandingPageBlockedIpEntity> findByLpgeCode(String lpgeCode);
}