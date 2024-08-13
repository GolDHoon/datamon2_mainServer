package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "TB_LANDING_PAGE_SETTING")
public class LandingPageSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "lpge_code", nullable = false)
    private String lpgeCode;

    @Column(name = "column_name")
    private String columnName;

    @ColumnDefault("0")
    @Column(name = "duplication_validation_yn", nullable = false)
    private Boolean duplicationValidationYn = false;

    @Column(name = "duplication_validation_days")
    private Integer duplicationValidationDays;

    @ColumnDefault("0")
    @Column(name = "display_ordering_yn", nullable = false)
    private Boolean displayOrderingYn = false;

    @Column(name = "display_ordering_number", columnDefinition = "int UNSIGNED")
    private Long displayOrderingNumber;

    @Column(name = "display_prefix")
    private String displayPrefix;

    @Column(name = "display_suffix")
    private String displaySuffix;

    @ColumnDefault("0")
    @Column(name = "tel_number_yn", nullable = false)
    private Boolean telNumberYn = false;
}