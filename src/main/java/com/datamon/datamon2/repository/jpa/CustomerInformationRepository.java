package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.entity.CustomerInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInformationRepository extends JpaRepository<CustomerInformationEntity, Long> {
    List<CustomerInformationEntity> findByLpgeCode(String lpgeCode);
}