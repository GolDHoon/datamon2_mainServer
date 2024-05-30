package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "TB_LANDING_PAGE")
public class LandingPageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "lpge_code", nullable = false)
    private String lpgeCode;

    @Column(name = "domain", nullable = false, length = 300)
    private String domain;

    @ColumnDefault("1")
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn = false;

    @ColumnDefault("0")
    @Column(name = "del_yn", nullable = false)
    private Boolean delYn = false;

    @ColumnDefault("(now())")
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "create_id", nullable = false)
    private Integer createId;

    @ColumnDefault("(now())")
    @Column(name = "modify_date", nullable = false)
    private LocalDate modifyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modifyId;

    @Column(name = "delete_date")
    private LocalDate deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;

}