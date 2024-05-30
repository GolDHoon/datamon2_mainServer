package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import com.datamon.datamon2.entity.PageCodeEntity;
import com.datamon.datamon2.mapper.repository.PageCodeMapper;
import com.datamon.datamon2.repository.jpa.PageCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageCodeService {
    private PageCodeRepository pageCodeRepository;
    private PageCodeMapper pageCodeMapper;

    public PageCodeService(PageCodeRepository pageCodeRepository, PageCodeMapper pageCodeMapper) {
        this.pageCodeRepository = pageCodeRepository;
        this.pageCodeMapper = pageCodeMapper;
    }

    @PostConstruct
    public void init() {
        List<PageCodeDto> pageCodes = getPageCodeAll();
        CommonCodeCache.setPageCodes(pageCodes);
    }

    @Transactional(readOnly = true)
    public List<PageCodeDto> getPageCodeAll(){
        List<PageCodeEntity> all = pageCodeRepository.findAll();
        List<PageCodeDto> result = new ArrayList<>();

        all.forEach(entity -> {
            result.add(pageCodeMapper.toDto(entity));
        });

        return result;
    }
}
