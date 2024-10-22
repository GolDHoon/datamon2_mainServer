package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonUserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "TB_ACCOUNT_APPROVAL_REQUEST")
public class AccountApprovalRequestEntity extends DrivenCommonUserEntity implements Serializable {
    @Id
    @Size(max = 32)
    @Column(name = "idx", nullable = false, length = 32)
    private String idx;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @Size(max = 1)
    @NotNull
    @Column(name = "request_type", nullable = false, length = 1)
    private String requestType;

    @NotNull
    @Lob
    @Column(name = "request_reason", nullable = false)
    private String requestReason;

    @Lob
    @Column(name = "rejection_reason")
    private String rejectionReason;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "completion_yn", nullable = false)
    private Boolean completionYn = false;
}