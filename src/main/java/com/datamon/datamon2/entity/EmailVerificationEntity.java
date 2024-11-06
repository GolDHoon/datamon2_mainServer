package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.common.DrivenCommonUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TB_EMAIL_VERIFICATION")
public class EmailVerificationEntity extends DrivenCommonUserEntity implements Serializable {
    @Id
    @Size(max = 32)
    @Column(name = "idx", nullable = false, length = 32)
    private String idx;

    @Size(max = 16)
    @NotNull
    @Column(name = "verification_code", nullable = false, length = 16)
    private String verificationCode;

}