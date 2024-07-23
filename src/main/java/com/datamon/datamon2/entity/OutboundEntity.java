package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OutboundEntity {
    @Id
    @Column(name = "idx", nullable = false, length = 31)
    private String idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "cust_id", nullable = false)
    private String custId;

    @Column(name = "tel_column", nullable = false)
    private String telColumn;

    @Column(name = "memo")
    private String memo;

}