package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_LANDING_PAGE_INFOMATION")
public class LandingPageInfomationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "lpge_code")
    private String lpgeCode;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

}