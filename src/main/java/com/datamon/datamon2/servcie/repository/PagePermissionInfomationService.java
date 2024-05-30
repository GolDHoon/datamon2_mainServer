package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.PagePermissionInfomationDto;
import com.datamon.datamon2.entity.PagePermissionInfomationEntity;
import com.datamon.datamon2.mapper.repository.PagePermissionInfomationMapper;
import com.datamon.datamon2.repository.jpa.PagePermissionInfomationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagePermissionInfomationService {
    private PagePermissionInfomationRepository pagePermissionInfomationRepository;
    private PagePermissionInfomationMapper pagePermissionInfomationMapper;

    public PagePermissionInfomationService(PagePermissionInfomationRepository pagePermissionInfomationRepository, PagePermissionInfomationMapper pagePermissionInfomationMapper) {
        this.pagePermissionInfomationRepository = pagePermissionInfomationRepository;
        this.pagePermissionInfomationMapper = pagePermissionInfomationMapper;
    }

    @Transactional(readOnly = true)
    public List<PagePermissionInfomationDto> getPagePermissionInfomationByUserId(int userId){
        List<PagePermissionInfomationEntity> byUserId = pagePermissionInfomationRepository.findByUserId(userId);
        List<PagePermissionInfomationDto> result = new ArrayList<>();
        byUserId.forEach(entity -> {
            result.add(pagePermissionInfomationMapper.toDto(entity));
        });
        return result;
    }
}
