package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.UserCdbtMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCdbtMappingRepository extends JpaRepository<UserCdbtMappingEntity, Long> {
    List<UserCdbtMappingEntity> findByUserId(int userId);
}