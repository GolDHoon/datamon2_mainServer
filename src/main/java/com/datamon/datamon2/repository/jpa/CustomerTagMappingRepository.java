package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.CustomerTagMappingEntity;
import com.datamon.datamon2.entity.embedded.CustomerTagMappingEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTagMappingRepository extends JpaRepository<CustomerTagMappingEntity, CustomerTagMappingEntityId> {
}