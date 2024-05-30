package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "TB_LANDING_PAGE_BLOCKED_IP")
public class LandingPageBlockedIpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "lpge_code", nullable = false)
    private String lpgeCode;

    @Column(name = "ip_1", columnDefinition = "tinyint UNSIGNED not null")
    private Short ip1;

    @Column(name = "ip_2", columnDefinition = "tinyint UNSIGNED not null")
    private Short ip2;

    @Column(name = "ip_3", columnDefinition = "tinyint UNSIGNED not null")
    private Short ip3;

    @Column(name = "ip_4", columnDefinition = "tinyint UNSIGNED not null")
    private Short ip4;

    @ColumnDefault("1")
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn = false;

    @ColumnDefault("0")
    @Column(name = "del_yn")
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