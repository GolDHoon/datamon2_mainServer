package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.PagePermissionInfomationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagePermissionInfomationRepository extends JpaRepository<PagePermissionInfomationEntity, Long> {
    List<PagePermissionInfomationEntity> findByUserId(int userId);
}