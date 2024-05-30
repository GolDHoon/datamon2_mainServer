package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.embeddable.UserLpgeMappingId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_USER_LPGE_MAPPING")
public class UserLpgeMappingEntity {
    @EmbeddedId
    private UserLpgeMappingId id;

    @MapsId("lpgeCode")
    @Column(name = "lpge_code", nullable = false)
    private String lpgeCode;

    @MapsId("userId")
    @Column(name = "user_id", nullable = false)
    private Integer userId;

}