package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.CdbqCodeDto;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import com.datamon.datamon2.entity.CdbqCodeEntity;
import com.datamon.datamon2.entity.PageCodeEntity;
import com.datamon.datamon2.mapper.repository.CdbqCodeMapper;
import com.datamon.datamon2.repository.jpa.CdbqCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CdbqCodeService {
    private CdbqCodeRepository cdbqCodeRepository;
    private CdbqCodeMapper cdbqCodeMapper;

    public CdbqCodeService(CdbqCodeRepository cdbqCodeRepository, CdbqCodeMapper cdbqCodeMapper) {
        this.cdbqCodeRepository = cdbqCodeRepository;
        this.cdbqCodeMapper = cdbqCodeMapper;
    }

    @PostConstruct
    public void init() {
        List<CdbqCodeDto> cdbqCodes = getCdbqCodeAll();
        CommonCodeCache.setCdbqCodes(cdbqCodes);
    }

    @Transactional(readOnly = true)
    public List<CdbqCodeDto> getCdbqCodeAll(){
        List<CdbqCodeEntity> all = cdbqCodeRepository.findAll();
        List<CdbqCodeDto> result = new ArrayList<>();

        all.forEach(entity -> {
            result.add(cdbqCodeMapper.toDto(entity));
        });

        return result;
    }
}
