package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_CUSTOMER_INFORMATION")
public class CustomerInformationEntity extends DrivenCommonCheckUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "cdbt_low_code", nullable = false)
    private String cdbtLowCode;

    @Column(name = "cdbs_code", nullable = false)
    private String cdbsCode;

    @Column(name = "cdbq_code", nullable = false)
    private String cdbqCode;

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
}