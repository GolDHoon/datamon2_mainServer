package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_LANDING_PAGE_SETTING")
public class LandingPageSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "lpge_code", nullable = false)
    private String lpgeCode;

    @Column(name = "duplication_column")
    private String duplicationColumn;

    @Column(name = "duplication_validation_days")
    private Integer duplicationValidationDays;

}