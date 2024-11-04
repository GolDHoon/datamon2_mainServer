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
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public LpgeCodeDto getLpgeCodeFindById(int idx){
        return lpgeCodeMapper.toDto(lpgeCodeRepository.findById(idx).orElse(new LpgeCodeEntity()));
    }

    @Transactional(readOnly = true)
    public LpgeCodeDto getLpgeCodeByCodeFullName(String codeFullName){
        return lpgeCodeMapper.toDto(lpgeCodeRepository.findByCodeFullName(codeFullName).orElse(new LpgeCodeEntity()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LpgeCodeDto save(LpgeCodeDto lpgeCodeDto){
        LpgeCodeDto save = lpgeCodeMapper.toDto(lpgeCodeRepository.save(lpgeCodeMapper.toEntity(lpgeCodeDto)));
        List<LpgeCodeDto> lpgeCodes = CommonCodeCache.getLpgeCodes();
        lpgeCodes.add(save);
        return save;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LpgeCodeDto modify(LpgeCodeDto lpgeCodeDto){
        LpgeCodeDto save = lpgeCodeMapper.toDto(lpgeCodeRepository.save(lpgeCodeMapper.toEntity(lpgeCodeDto)));
        List<LpgeCodeDto> lpgeCodes = CommonCodeCache.getLpgeCodes();
        for (int i = 0; i < lpgeCodes.size(); i++) {
            if (lpgeCodes.get(i).getIdx().equals(save.getIdx())) {
                lpgeCodes.set(i, save);
                break;
            }
        }
        return save;
    }
}
