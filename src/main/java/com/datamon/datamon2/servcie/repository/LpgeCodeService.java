package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.entity.LpgeCodeEntity;
import com.datamon.datamon2.mapper.repository.LpgeCodeMapper;
import com.datamon.datamon2.repository.jpa.LpgeCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LpgeCodeService {
    private LpgeCodeRepository lpgeCodeRepository;
    private LpgeCodeMapper lpgeCodeMapper;

    public LpgeCodeService(LpgeCodeRepository lpgeCodeRepository, LpgeCodeMapper lpgeCodeMapper) {
        this.lpgeCodeRepository = lpgeCodeRepository;
        this.lpgeCodeMapper = lpgeCodeMapper;
    }

    @PostConstruct
    public void init() {
        List<LpgeCodeDto> lpgeCodes = getLpgeCodeAll();
        CommonCodeCache.setLpgeCodes(lpgeCodes);
    }

    @Transactional(readOnly = true)
    public List<LpgeCodeDto> getLpgeCodeAll(){
        List<LpgeCodeDto> result = new ArrayList<>();
        lpgeCodeRepository.findAll().forEach(entity -> {
            result.add(lpgeCodeMapper.toDto(entity));
        });
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LpgeCodeDto saveLpgeCode(LpgeCodeDto lpgeCodeDto){
        LpgeCodeDto save = lpgeCodeMapper.toDto(lpgeCodeRepository.save(lpgeCodeMapper.toEntity(lpgeCodeDto)));
        List<LpgeCodeDto> lpgeCodes = CommonCodeCache.getLpgeCodes();
        lpgeCodes.add(save);
        return save;

    }
}
