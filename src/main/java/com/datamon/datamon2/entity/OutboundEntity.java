package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_OUTBOUND")
public class OutboundEntity {
    @Id
    @Column(name = "idx", nullable = false, length = 51)
    private String idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "cust_id", nullable = false)
    private String custId;

    @Column(name = "tel_column", nullable = false)
    private String telColumn;

    @Column(name = "memo")
    private String memo;

    @Column(name = "order_memo")
    private String orderMemo;

    @Column(name = "scheduled_callback_date")
    private LocalDateTime scheduledCallbackDate;

    @Column(name = "scheduled_conversion_date")
    private LocalDateTime scheduledConversionDate;

}