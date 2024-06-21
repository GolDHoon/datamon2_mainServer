package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_MEMBER_INFOMATION")
public class MemberInfomationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "role", length = 100)
    private String role;

    @Column(name = "contact_phone", length = 10)
    private String contactPhone;

    @Column(name = "contact_mail", length = 50)
    private String contactMail;

}