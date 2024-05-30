package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import com.datamon.datamon2.entity.PaatCodeEntity;
import com.datamon.datamon2.mapper.repository.PaatCodeMapper;
import com.datamon.datamon2.repository.jpa.PaatCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaatCodeService {
    private PaatCodeRepository paatCodeRepository;
    private PaatCodeMapper paatCodeMapper;

    public PaatCodeService(PaatCodeMapper paatCodeMapper, PaatCodeRepository paatCodeRepository) {
        this.paatCodeMapper = paatCodeMapper;
        this.paatCodeRepository = paatCodeRepository;
    }

    @PostConstruct
    public void init() {
        List<PaatCodeDto> paatCodes = getPaatCodeAll();
        CommonCodeCache.setPaatCodes(paatCodes);
    }

    @Transactional(readOnly = true)
    public List<PaatCodeDto> getPaatCodeAll(){
        List<PaatCodeEntity> all = paatCodeRepository.findAll();
        List<PaatCodeDto> result = new ArrayList<>();

        all.forEach(entity -> {
            result.add(paatCodeMapper.toDto(entity));
        });

        return result;
    }
}
