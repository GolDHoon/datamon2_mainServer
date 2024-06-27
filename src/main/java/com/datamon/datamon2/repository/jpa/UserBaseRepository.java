package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.UserBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBaseRepository extends JpaRepository<UserBaseEntity, Integer> {
    Optional<UserBaseEntity> findByUserId(String userId);
    List<UserBaseEntity> findByUserTypeIn(List<String> userTypes);
    List<UserBaseEntity> findByIdxIn(List<Integer> idxs);
}