package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserCdbtMappingDto;
import com.datamon.datamon2.entity.UserCdbtMappingEntity;
import com.datamon.datamon2.mapper.repository.UserCdbtMappingMapper;
import com.datamon.datamon2.repository.jpa.UserCdbtMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCdbtMappingService {
    private UserCdbtMappingRepository userCdbtMappingRepository;
    private UserCdbtMappingMapper userCdbtMappingMapper;

    public UserCdbtMappingService(UserCdbtMappingRepository userCdbtMappingRepository, UserCdbtMappingMapper userCdbtMappingMapper) {
        this.userCdbtMappingRepository = userCdbtMappingRepository;
        this.userCdbtMappingMapper = userCdbtMappingMapper;
    }

    @Transactional(readOnly = true)
    public List<UserCdbtMappingDto> getUserCdbtListByUserId(int userId){
        List<UserCdbtMappingDto> result = new ArrayList<>();
        userCdbtMappingRepository.findByUserId(userId).forEach(entity -> {
            result.add(userCdbtMappingMapper.toDto(entity));
        });
        return result;
    }

    public List<UserCdbtMappingDto> getUserCdbtListByCdbtLowCode(String cdbtLowCode){
        List<UserCdbtMappingDto> result = new ArrayList<>();
        userCdbtMappingRepository.findByCdbtLowCode(cdbtLowCode).forEach(entity -> {
            result.add(userCdbtMappingMapper.toDto(entity));
        });
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserCdbtMappingDto save(UserCdbtMappingDto userCdbtMappingDto){
        UserCdbtMappingEntity entity = userCdbtMappingMapper.toEntity(userCdbtMappingDto);
        UserCdbtMappingEntity save = userCdbtMappingRepository.save(entity);
        return userCdbtMappingMapper.toDto(save);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(UserCdbtMappingDto UserCdbtMappingDto){
        userCdbtMappingRepository.delete(userCdbtMappingMapper.toEntity(UserCdbtMappingDto));
    }
}
