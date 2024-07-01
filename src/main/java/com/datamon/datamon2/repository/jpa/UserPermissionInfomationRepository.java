package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.UserPermissionInfomationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionInfomationRepository extends JpaRepository<UserPermissionInfomationEntity, Long> {
    List<UserPermissionInfomationEntity> findByUserId(int userId);
}