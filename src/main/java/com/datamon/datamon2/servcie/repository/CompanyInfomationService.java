package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.CompanyInfomationDto;
import com.datamon.datamon2.mapper.repository.CompanyInfomationMapper;
import com.datamon.datamon2.repository.jpa.CompanyInfomationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyInfomationService {
    private CompanyInfomationRepository companyInfomationRepository;
    private CompanyInfomationMapper companyInfomationMapper;

    public CompanyInfomationService(CompanyInfomationRepository companyInfomationRepository, CompanyInfomationMapper companyInfomationMapper) {
        this.companyInfomationRepository = companyInfomationRepository;
        this.companyInfomationMapper = companyInfomationMapper;
    }

    @Transactional(readOnly = true)
    public CompanyInfomationDto getCompanyInfomationByUserId(int userId){
        return companyInfomationRepository.findByUserId(userId).map(companyInfomationMapper::toDto).orElse(new CompanyInfomationDto());
    }

    @Transactional(readOnly = true)
    public CompanyInfomationDto getCompanyInfomationById(int id){
        return companyInfomationRepository.findById(id).map(companyInfomationMapper::toDto).orElse(new CompanyInfomationDto());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompanyInfomationDto save (CompanyInfomationDto companyInfomationDto){
        return companyInfomationMapper.toDto(companyInfomationRepository.save(companyInfomationMapper.toEntity(companyInfomationDto)));
    }
}
