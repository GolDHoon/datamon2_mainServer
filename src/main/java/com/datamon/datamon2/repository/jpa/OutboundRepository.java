package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.OutboundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboundRepository extends JpaRepository<OutboundEntity, String> {
    Optional<OutboundEntity> findByCustId (String custId);
    List<OutboundEntity> findByUserId (int userId);
}