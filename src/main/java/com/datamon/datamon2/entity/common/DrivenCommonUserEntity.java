package com.datamon.datamon2.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class DrivenCommonUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "create_id", nullable = false)
    private Integer createId;

    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modifyId;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;
}
