package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "TB_USER_PERMISSION_INFOMATION")
public class UserPermissionInfomationEntity extends DrivenCommonCheckUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "usat_code", nullable = false)
    private String usatCode;

    @Column(name = "cdbt_low_code", nullable = false, length = 20)
    private String cdbtLowCode;

    @Column(name = "cdbt_code", nullable = false)
    private String cdbtCode;
}