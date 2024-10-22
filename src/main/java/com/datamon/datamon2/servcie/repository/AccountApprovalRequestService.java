package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.AccountApprovalRequestDto;
import com.datamon.datamon2.mapper.repository.AccountApprovalRequestMapper;
import com.datamon.datamon2.repository.jpa.AccountApprovalRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountApprovalRequestService {
    private AccountApprovalRequestRepository accountApprovalRequestRepository;
    private AccountApprovalRequestMapper accountApprovalRequestMapper;

    public AccountApprovalRequestService(AccountApprovalRequestRepository accountApprovalRequestRepository, AccountApprovalRequestMapper accountApprovalRequestMapper) {
        this.accountApprovalRequestRepository = accountApprovalRequestRepository;
        this.accountApprovalRequestMapper = accountApprovalRequestMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountApprovalRequestDto save (AccountApprovalRequestDto accountApprovalRequestDto){
        return accountApprovalRequestMapper.toDto(accountApprovalRequestRepository.save(accountApprovalRequestMapper.toEntity(accountApprovalRequestDto)));
    }
}
