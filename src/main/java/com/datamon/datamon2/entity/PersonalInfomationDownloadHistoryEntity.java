package com.datamon.datamon2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_PERSONAL_INFOMATION_DOWNLOAD_HISTORY")
public class PersonalInfomationDownloadHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "download_url", nullable = false)
    private String downloadUrl;

    @Column(name = "file_url", nullable = false, length = 200)
    private String fileUrl;

    @Column(name = "file_name", nullable = false, length = 100)
    private String fileName;

}