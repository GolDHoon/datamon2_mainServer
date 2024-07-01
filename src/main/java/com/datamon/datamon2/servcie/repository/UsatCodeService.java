package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.dto.repository.UsatCodeDto;
import com.datamon.datamon2.mapper.repository.UsatCodeMapper;
import com.datamon.datamon2.repository.jpa.UsatCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsatCodeService {
    private UsatCodeRepository usatCodeRepository;
    private UsatCodeMapper usatCodeMapper;

    public UsatCodeService(UsatCodeRepository usatCodeRepository, UsatCodeMapper usatCodeMapper) {
        this.usatCodeRepository = usatCodeRepository;
        this.usatCodeMapper = usatCodeMapper;
    }

    @Transactional(readOnly = true)
    public List<UsatCodeDto> getUsatCodeAll(){
        List<UsatCodeDto> result = new ArrayList<>();
        usatCodeRepository.findAll().forEach(entity -> result.add(usatCodeMapper.toDto(entity)));
        return result;
    }

    @PostConstruct
    public void init() {
        List<UsatCodeDto> usatCodes = getUsatCodeAll();
        CommonCodeCache.setUsatCodes(usatCodes);
    }

}
