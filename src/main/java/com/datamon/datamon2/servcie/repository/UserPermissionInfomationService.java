package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserPermissionInfomationDto;
import com.datamon.datamon2.mapper.repository.UserPermissionInfomationMapper;
import com.datamon.datamon2.repository.jpa.UserPermissionInfomationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPermissionInfomationService {
    private UserPermissionInfomationRepository userPermissionInfomationRepository;
    private UserPermissionInfomationMapper userPermissionInfomationMapper;

    public UserPermissionInfomationService(UserPermissionInfomationRepository userPermissionInfomationRepository, UserPermissionInfomationMapper userPermissionInfomationMapper) {
        this.userPermissionInfomationRepository = userPermissionInfomationRepository;
        this.userPermissionInfomationMapper = userPermissionInfomationMapper;
    }

    @Transactional(readOnly = true)
    public List<UserPermissionInfomationDto> getUserPermissionInfomationByUserId(int userId){
        List<UserPermissionInfomationDto> result = new ArrayList<>();

        userPermissionInfomationRepository.findByUserId(userId).forEach(entity -> {
            result.add(userPermissionInfomationMapper.toDto(entity));
        });

        return result;
    }
}
