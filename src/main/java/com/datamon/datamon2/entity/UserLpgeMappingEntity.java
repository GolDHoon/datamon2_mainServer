package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.embeddable.UserLpgeMappingEntityId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_USER_LPGE_MAPPING")
public class UserLpgeMappingEntity {
    @EmbeddedId
    private UserLpgeMappingEntityId id;
}