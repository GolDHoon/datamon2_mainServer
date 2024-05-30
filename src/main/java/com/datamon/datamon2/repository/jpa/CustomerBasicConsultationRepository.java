package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.CustomerBasicConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBasicConsultationRepository extends JpaRepository<CustomerBasicConsultationEntity, Integer> {
}