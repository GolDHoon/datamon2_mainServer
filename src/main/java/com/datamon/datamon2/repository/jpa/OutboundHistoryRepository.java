package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.OutboundHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboundHistoryRepository extends JpaRepository<OutboundHistoryEntity, String> {
    List<OutboundHistoryEntity> findByOriginalIdx(String originalIdx);
}