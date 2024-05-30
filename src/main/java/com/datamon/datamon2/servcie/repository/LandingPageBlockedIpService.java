package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.LandingPageBlockedIpDto;
import com.datamon.datamon2.mapper.repository.LandingPageBlockedIpMapper;
import com.datamon.datamon2.repository.jpa.LandingPageBlockedIpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LandingPageBlockedIpService {
    private LandingPageBlockedIpRepository landingPageBlockedIpRepository;
    private LandingPageBlockedIpMapper landingPageBlockedIpMapper;

    public LandingPageBlockedIpService(LandingPageBlockedIpRepository landingPageBlockedIpRepository, LandingPageBlockedIpMapper landingPageBlockedIpMapper) {
        this.landingPageBlockedIpRepository = landingPageBlockedIpRepository;
        this.landingPageBlockedIpMapper = landingPageBlockedIpMapper;
    }

    @Transactional(readOnly = true)
    public List<LandingPageBlockedIpDto> getLandingPageBlockedIpByLpgeCode(String lpgeCode){
        List<LandingPageBlockedIpDto> result = new ArrayList<>();
        landingPageBlockedIpRepository.findByLpgeCode(lpgeCode).forEach(entity -> {
            result.add(landingPageBlockedIpMapper.toDto(entity));
        });
        return result;
    }

}
