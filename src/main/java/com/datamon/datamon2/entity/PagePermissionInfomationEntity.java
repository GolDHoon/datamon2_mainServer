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
@Table(name = "TB_PAGE_PERMISSION_INFOMATION")
public class PagePermissionInfomationEntity extends DrivenCommonCheckUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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

}