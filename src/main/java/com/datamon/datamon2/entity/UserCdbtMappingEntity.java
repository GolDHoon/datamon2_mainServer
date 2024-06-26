package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_USER_CDBT_MAPPING")
public class UserCdbtMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "cdbt_low_code", nullable = false, length = 20)
    private String cdbtLowCode;

    @Column(name = "cdbt_code", nullable = false)
    private String cdbtCode;

}