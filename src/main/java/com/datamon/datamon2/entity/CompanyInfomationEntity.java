package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_COMPANY_INFOMATION")
public class CompanyInfomationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "ceo", length = 10)
    private String ceo;

    @Column(name = "corporate_number", nullable = false, length = 13)
    private String corporateNumber;

    @Column(name = "corporate_address", length = 200)
    private String corporateAddress;

    @Column(name = "corporate_mail", length = 200)
    private String corporateMail;

    @Column(name = "business_status", length = 50)
    private String businessStatus;

    @Column(name = "business_item", length = 200)
    private String businessItem;

}