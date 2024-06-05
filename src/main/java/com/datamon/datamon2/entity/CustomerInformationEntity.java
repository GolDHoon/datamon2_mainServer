package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_CUSTOMER_INFORMATION")
public class CustomerInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "lpge_code", nullable = false)
    private String lpgeCode;

    @Column(name = "utm_sourse", length = 20)
    private String utmSourse;

    @Column(name = "utm_medium", length = 20)
    private String utmMedium;

    @Column(name = "utm_campaign", length = 200)
    private String utmCampaign;

    @Column(name = "utm_term", length = 50)
    private String utmTerm;

    @Column(name = "utm_content", length = 200)
    private String utmContent;

    @Column(name = "ip", length = 15)
    private String ip;

    @ColumnDefault("1")
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn = false;

    @ColumnDefault("0")
    @Column(name = "del_yn")
    private Boolean delYn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "create_id")
    private Integer createId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;

    @Column(name = "modify_id")
    private Integer modifyId;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;

}