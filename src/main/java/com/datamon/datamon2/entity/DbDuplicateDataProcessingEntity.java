package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import com.datamon.datamon2.entity.common.DrivenCommonUserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_DB_DUPLICATE_DATA_PROCESSING")
public class DbDuplicateDataProcessingEntity extends DrivenCommonUserEntity implements Serializable {
    @Id
    @Size(max = 32)
    @Column(name = "idx", nullable = false, length = 32)
    private String idx;

    @NotNull
    @Column(name = "db_type", nullable = false, length = 9)
    private String dbType;

    @Size(max = 20)
    @NotNull
    @Column(name = "db_code", nullable = false, length = 20)
    private String dbCode;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "key_group_no", nullable = false)
    private Integer keyGroupNo;

    @Size(max = 100)
    @Column(name = "`key`", length = 100)
    private String key;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "preprocessing_yn", nullable = false)
    private Boolean preprocessingYn = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "postprocessing_yn", nullable = false)
    private Boolean postprocessingYn = false;

}