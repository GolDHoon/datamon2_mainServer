package com.datamon.datamon2.entity.common;

import com.datamon.datamon2.util.LocalDateTimeToIntConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class DrivenCommonUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "create_date", nullable = false)
    @Convert(converter = LocalDateTimeToIntConverter.class)
    private LocalDateTime createDate;

    @Column(name = "create_id", nullable = false)
    private Integer createId;

    @Column(name = "modify_date", nullable = false)
    @Convert(converter = LocalDateTimeToIntConverter.class)
    private LocalDateTime modifyDate;

    @Column(name = "modify_id", nullable = false)
    private Integer modifyId;

    @Column(name = "delete_date")
    @Convert(converter = LocalDateTimeToIntConverter.class)
    private LocalDateTime deleteDate;

    @Column(name = "delete_id")
    private Integer deleteId;
}
