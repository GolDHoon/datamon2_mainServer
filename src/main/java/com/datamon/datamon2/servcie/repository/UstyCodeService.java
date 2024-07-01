package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.UstyCodeDto;
import com.datamon.datamon2.entity.UstyCodeEntity;
import com.datamon.datamon2.mapper.repository.UstyCodeMapper;
import com.datamon.datamon2.repository.jpa.UstyCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UstyCodeService {
    private UstyCodeRepository ustyCodeRepository;
    private UstyCodeMapper ustyCodeMapper;

    public UstyCodeService(UstyCodeRepository ustyCodeRepository, UstyCodeMapper ustyCodeMapper) {
        this.ustyCodeRepository = ustyCodeRepository;
        this.ustyCodeMapper = ustyCodeMapper;
    }

    @PostConstruct
    public void init(){
        List<UstyCodeDto> ustyCodeAll = getUstyCodeAll();
        CommonCodeCache.setUstyCodes(ustyCodeAll);
    }

    @Transactional(readOnly = true)
    public List<UstyCodeDto> getUstyCodeAll(){
        List<UstyCodeDto> result = new ArrayList<>();
        ustyCodeRepository.findAll().forEach(entity -> {
            result.add(ustyCodeMapper.toDto(entity));
        });

        return result;
    }
}
