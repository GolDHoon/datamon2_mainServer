package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.CompanyInfomationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyInfomationRepository extends JpaRepository<CompanyInfomationEntity, Integer> {
    Optional<CompanyInfomationEntity> findByUserId(int UserId);
    Optional<CompanyInfomationEntity> findByCorporateNumber(String corporateNumber);
}