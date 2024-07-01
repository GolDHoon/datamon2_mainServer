package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.LandingPageDto;
import com.datamon.datamon2.entity.LandingPageEntity;
import com.datamon.datamon2.mapper.repository.LandingPageMapper;
import com.datamon.datamon2.repository.jpa.LandingPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LandingPageService {
    private LandingPageRepository landingPageRepository;
    private LandingPageMapper landingPageMapper;

    public LandingPageService(LandingPageRepository landingPageRepository, LandingPageMapper landingPageMapper) {
        this.landingPageRepository = landingPageRepository;
        this.landingPageMapper = landingPageMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LandingPageDto saveLandingPage(LandingPageDto landingPageDto){

        return landingPageMapper.toDto(landingPageRepository.save(landingPageMapper.toEntity(landingPageDto)));
    }

}
