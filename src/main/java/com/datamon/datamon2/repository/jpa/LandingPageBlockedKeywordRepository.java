package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.LandingPageBlockedKeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandingPageBlockedKeywordRepository extends JpaRepository<LandingPageBlockedKeywordEntity, Integer> {
    List<LandingPageBlockedKeywordEntity> findByLpgeCode(String lpgeCdoe);
}