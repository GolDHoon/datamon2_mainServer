package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "TB_LANDING_PAGE_BLOCKED_IP")
public class LandingPageBlockedIpEntity extends DrivenCommonCheckUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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

}