package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "TB_PAGE_PERMISSION_INFOMATION")
public class PagePermissionInfomationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "page_code", nullable = false)
    private String pageCode;

    @Column(name = "paat_code", nullable = false)
    private String paatCode;

    @ColumnDefault("1")
    @Column(name = "useYn", nullable = false)
    private Boolean useYn = false;

    @ColumnDefault("0")
    @Column(name = "delYn", nullable = false)
    private Boolean delYn = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "create_id", nullable = false)
    private Integer create;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modify;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;

}