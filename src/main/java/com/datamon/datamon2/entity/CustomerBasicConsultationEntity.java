package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonUserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_CUSTOMER_BASIC_CONSULTATION")
public class CustomerBasicConsultationEntity extends DrivenCommonUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "cust_id", nullable = false)
    private String custId;

    @Column(name = "cust_key", nullable = false, length = 100)
    private String key;

    @Column(name = "cust_value", nullable = false, length = 5000)
    private String value;
}