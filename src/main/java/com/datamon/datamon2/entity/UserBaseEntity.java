package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "TB_USER_BASE")
public class UserBaseEntity extends DrivenCommonCheckUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "user_pw", nullable = false, length = 256)
    private String userPw;

    @Column(name = "salt", length = 32)
    private String salt;

    @Column(name = "user_type", nullable = false)
    private String userType;

}