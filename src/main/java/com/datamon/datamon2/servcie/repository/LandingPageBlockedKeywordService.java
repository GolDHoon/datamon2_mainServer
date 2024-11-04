package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.LandingPageBlockedKeywordDto;
import com.datamon.datamon2.mapper.repository.LandingPageBlockedKeywordMapper;
import com.datamon.datamon2.repository.jpa.LandingPageBlockedKeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LandingPageBlockedKeywordService {
    private LandingPageBlockedKeywordRepository landingPageBlockedKeywordRepository;
    private LandingPageBlockedKeywordMapper landingPageBlockedKeywordMapper;

    public LandingPageBlockedKeywordService(LandingPageBlockedKeywordRepository landingPageBlockedKeywordRepository, LandingPageBlockedKeywordMapper landingPageBlockedKeywordMapper) {
        this.landingPageBlockedKeywordRepository = landingPageBlockedKeywordRepository;
        this.landingPageBlockedKeywordMapper = landingPageBlockedKeywordMapper;
    }

    @Transactional(readOnly = true)
    public List<LandingPageBlockedKeywordDto> getLandingPageBlockedKeywordByLpgeCode(String lpgeCode){
        List<LandingPageBlockedKeywordDto> result = new ArrayList<>();
        landingPageBlockedKeywordRepository.findByLpgeCode(lpgeCode).forEach(entity -> {
            result.add(landingPageBlockedKeywordMapper.toDto(entity));
        });
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LandingPageBlockedKeywordDto saveLandingPageBlockedKeyword(LandingPageBlockedKeywordDto landingPageBlockedKeywordDto){
        return landingPageBlockedKeywordMapper.toDto(landingPageBlockedKeywordRepository.save(landingPageBlockedKeywordMapper.toEntity(landingPageBlockedKeywordDto)));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteLandingPageBlockedKeywordById(Integer id) {
        landingPageBlockedKeywordRepository.deleteById(id);
    }
}
