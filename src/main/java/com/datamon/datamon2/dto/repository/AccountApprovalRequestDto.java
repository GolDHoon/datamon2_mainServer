package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.AccountApprovalRequestEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link AccountApprovalRequestEntity}
 */
@Data
public class AccountApprovalRequestDto extends DrivenCommonUserDto implements Serializable {
    String idx;
    int userId;
    String requestType;
    String requestReason;
    String rejectionReason;
    Boolean completionYn;
}