package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.PersonalInfomationDownloadHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInfomationDownloadHistoryRepository extends JpaRepository<PersonalInfomationDownloadHistoryEntity, Long> {
}