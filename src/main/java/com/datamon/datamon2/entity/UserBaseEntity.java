package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "TB_USER_BASE")
public class UserBaseEntity {
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

    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;

    @Column(name = "del_yn", nullable = false)
    private Boolean delYn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "create_id", nullable = false)
    private Integer createId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modifyId;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;

}