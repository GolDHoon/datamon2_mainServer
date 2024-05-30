package com.datamon.datamon2.repository.jpa;

import com.datamon.datamon2.entity.UserLpgeMappingEntity;
import com.datamon.datamon2.entity.embeddable.UserLpgeMappingEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLpgeMappingRepository extends JpaRepository<UserLpgeMappingEntity, UserLpgeMappingEntityId> {

}