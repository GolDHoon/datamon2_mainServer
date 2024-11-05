package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.SmsVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsVerificationRepository extends JpaRepository<SmsVerificationEntity, String> {
}