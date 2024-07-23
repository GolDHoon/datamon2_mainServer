package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_CUSTOMER_TAG_INFOMATION")
public class CustomerTagInfomationEntity extends DrivenCommonCheckUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @Column(name = "cdbt_low_code", nullable = false)
    private String cdbtLowCode;

    @Column(name = "tag_title", nullable = false, length = 100)
    private String tagTitle;

    @Column(name = "tag_discription", nullable = false, length = 5000)
    private String tagDiscription;

}