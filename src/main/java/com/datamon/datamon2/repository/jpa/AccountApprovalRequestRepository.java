package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.AccountApprovalRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountApprovalRequestRepository extends JpaRepository<AccountApprovalRequestEntity, String> {
    List<AccountApprovalRequestEntity> findByUserId(int userId);
    List<AccountApprovalRequestEntity> findByUserIdIn(List<Integer> userIdList);
}