package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "TB_CDBQ_CODE")
public class CdbqCodeEntity extends DrivenCommonCheckUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "code_name", nullable = false, length = 4)
    private String codeName;

    @Column(name = "code_full_name", nullable = false, length = 9)
    private String codeFullName;

    @Column(name = "code_value", nullable = false, length = 100)
    private String codeValue;

    @Column(name = "code_descript", length = 200)
    private String codeDescript;

}