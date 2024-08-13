package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.LandingPageSettingDto;
import com.datamon.datamon2.entity.LandingPageSettingEntity;
import com.datamon.datamon2.mapper.repository.LandingPageSettingMapper;
import com.datamon.datamon2.repository.jpa.LandingPageSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LandingPageSettingService {
    private LandingPageSettingRepository landingPageSettingRepository;
    private LandingPageSettingMapper landingPageSettingMapper;

    public LandingPageSettingService(LandingPageSettingRepository landingPageSettingRepository, LandingPageSettingMapper landingPageSettingMapper) {
        this.landingPageSettingRepository = landingPageSettingRepository;
        this.landingPageSettingMapper = landingPageSettingMapper;
    }

    @Transactional(readOnly = true)
    public List<LandingPageSettingDto> getLandingPageSettingListByLpgeCode(String lpgeCode){
        List<LandingPageSettingDto> result = new ArrayList<>();
        landingPageSettingRepository.findByLpgeCode(lpgeCode).forEach(entity -> {
            result.add(landingPageSettingMapper.toDto(entity));
        });
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LandingPageSettingDto save(LandingPageSettingDto landingPageSettingDto){
        return landingPageSettingMapper.toDto(landingPageSettingRepository.save(landingPageSettingMapper.toEntity(landingPageSettingDto)));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(LandingPageSettingDto landingPageSettingDto){
        landingPageSettingRepository.delete(landingPageSettingMapper.toEntity(landingPageSettingDto));
    }

}
