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
    private Integer id;

    @Column(name = "lpge_code")
    private String lpgeCode;

    @Column(name = "head")
    private String head;

    @Column(name = "body")
    private String body;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

}