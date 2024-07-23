package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.TableIndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableIndexRepository extends JpaRepository<TableIndexEntity, Long> {
    Optional<TableIndexEntity> findByOptionName(String optionName);
}