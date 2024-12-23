package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.MemberInfomationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberInfomationRepository extends JpaRepository<MemberInfomationEntity, Integer> {
    Optional<MemberInfomationEntity> findByUserId(int userId);
    List<MemberInfomationEntity> findByCompanyId(int companyId);
}