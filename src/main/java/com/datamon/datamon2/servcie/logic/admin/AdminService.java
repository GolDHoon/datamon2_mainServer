package com.datamon.datamon2.servcie.logic.admin;

import com.datamon.datamon2.servcie.repository.AccountApprovalRequestService;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private AccountApprovalRequestService accountApprovalRequestService;

    public AdminService(UserBaseService userBaseService, CompanyInfomationService companyInfomationService, AccountApprovalRequestService accountApprovalRequestService) {
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.accountApprovalRequestService = accountApprovalRequestService;
    }

    @Transactional
    public String requestAdminAccount(){

        return null;
    }
}
