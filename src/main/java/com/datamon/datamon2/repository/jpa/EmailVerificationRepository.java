package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {
}