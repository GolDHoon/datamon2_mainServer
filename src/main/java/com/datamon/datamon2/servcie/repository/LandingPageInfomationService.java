package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.LandingPageInfomationDto;
import com.datamon.datamon2.entity.LandingPageInfomationEntity;
import com.datamon.datamon2.mapper.repository.LandingPageInfomationMapper;
import com.datamon.datamon2.repository.jpa.LandingPageInfomationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LandingPageInfomationService {
    private LandingPageInfomationRepository landingPageInfomationRepository;
    private LandingPageInfomationMapper landingPageInfomationMapper;

    public LandingPageInfomationService(LandingPageInfomationRepository landingPageInfomationRepository, LandingPageInfomationMapper landingPageInfomationMapper) {
        this.landingPageInfomationRepository = landingPageInfomationRepository;
        this.landingPageInfomationMapper = landingPageInfomationMapper;
    }

    @Transactional(readOnly = true)
    public LandingPageInfomationDto getLandingPageInfomationByLpgeCode (String lpgeCode){
        return landingPageInfomationMapper.toDto(landingPageInfomationRepository.findByLpgeCode(lpgeCode).orElse(new LandingPageInfomationEntity()));
    }

}
