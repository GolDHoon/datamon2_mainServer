package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "TB_LPGE_CODE")
public class LpgeCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "code_name", nullable = false)
    private Integer codeName;

    @ColumnDefault("(concat(`parent_code_full_name`,_utf8mb3'_',convert(lpad(`code_name`,10,_utf8mb4'0') using utf8mb3)))")
    @Column(name = "code_full_name", nullable = false, length = 15)
    private String codeFullName;

    @Column(name = "code_value", nullable = false, length = 100)
    private String codeValue;

    @Column(name = "code_descript", length = 200)
    private String codeDescript;

    @ColumnDefault("1")
    @Column(name = "use_yn")
    private Boolean useYn;

    @ColumnDefault("0")
    @Column(name = "del_yn")
    private Boolean delYn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "create_id", nullable = false)
    private Integer createId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modifyId;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;

}