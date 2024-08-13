package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_OUTBOUND_HISTORY")
public class OutboundHistoryEntity extends DrivenCommonCheckUserEntity implements Serializable {
    @Id
    @Column(name = "idx", nullable = false, length = 62)
    private String idx;

    @Column(name = "original_idx", nullable = false, length = 51)
    private String originalIdx;

    @Column(name = "sort", nullable = false)
    private int sort;

    @Column(name = "save_reason")
    private String saveReason;

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