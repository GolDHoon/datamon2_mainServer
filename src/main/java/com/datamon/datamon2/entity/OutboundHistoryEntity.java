package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_OUTBOUND_HISTORY")
public class OutboundHistoryEntity extends DrivenCommonCheckUserEntity implements Serializable {
    @Id
    @Column(name = "idx", nullable = false, length = 33)
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