package com.datamon.datamon2.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@MappedSuperclass
@Data
public class DrivenCommonCheckEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ColumnDefault("1")
    @Column(name = "use_yn", nullable = false)
    private Boolean useYn = false;

    @ColumnDefault("0")
    @Column(name = "del_yn")
    private Boolean delYn;
}
