package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.CdbsCodeDto;
import com.datamon.datamon2.entity.CdbsCodeEntity;
import com.datamon.datamon2.mapper.repository.CdbsCodeMapper;
import com.datamon.datamon2.repository.jpa.CdbsCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CdbsCodeService {
    private CdbsCodeRepository cdbsCodeRepository;
    private CdbsCodeMapper cdbsCodeMapper;

    public CdbsCodeService(CdbsCodeRepository cdbsCodeRepository, CdbsCodeMapper cdbsCodeMapper) {
        this.cdbsCodeRepository = cdbsCodeRepository;
        this.cdbsCodeMapper = cdbsCodeMapper;
    }

    @PostConstruct
    public void init() {
        List<CdbsCodeDto> cdbsCodes = getCdbsCodeAll();
        CommonCodeCache.setCdbsCodes(cdbsCodes);
    }

    @Transactional(readOnly = true)
    public List<CdbsCodeDto> getCdbsCodeAll(){
        List<CdbsCodeDto> result = new ArrayList<>();

        cdbsCodeRepository.findAll().forEach(entity -> {
            result.add(cdbsCodeMapper.toDto(entity));
        });

        return result;
    }
}
