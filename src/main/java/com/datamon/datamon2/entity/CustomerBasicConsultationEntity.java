package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "TB_CUSTOMER_BASIC_CONSULTATION")
public class CustomerBasicConsultationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "cust_id", nullable = false)
    private Long custId;

    @Column(name = "`key`", nullable = false, length = 100)
    private String key;

    @Column(name = "value", nullable = false, length = 5000)
    private String value;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "create_id", nullable = false)
    private Integer createId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modifty_date", nullable = false)
    private Instant modiftyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modifyId;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;

}