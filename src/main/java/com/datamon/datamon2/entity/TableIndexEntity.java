package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_TABLE_INDEX")
public class TableIndexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "table_name", nullable = false, length = 200)
    private String tableName;

    @Column(name = "option_name", nullable = false, length = 200)
    private String optionName;

    @Column(name = "`index`", nullable = false)
    private Long index;

}