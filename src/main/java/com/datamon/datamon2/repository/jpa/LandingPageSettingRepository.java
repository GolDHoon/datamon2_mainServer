package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.LandingPageSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LandingPageSettingRepository extends JpaRepository<LandingPageSettingEntity, Long> {
    List<LandingPageSettingEntity> findByLpgeCode(String lpgeCode);
}