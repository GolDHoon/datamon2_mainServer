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

    @Column(name = "lpge_code", nullable = false, length = 15)
    private String lpgeCode;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

}