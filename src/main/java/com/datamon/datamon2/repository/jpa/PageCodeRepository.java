package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.PageCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageCodeRepository extends JpaRepository<PageCodeEntity, Integer> {
}