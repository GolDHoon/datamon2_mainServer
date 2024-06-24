package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserCdbtMappingDto;
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
    public List<UserCdbtMappingDto> getuserCdbtListByUserId(int userId){
        List<UserCdbtMappingDto> result = new ArrayList<>();
        userCdbtMappingRepository.findByUserId(userId).forEach(entity -> {
            result.add(userCdbtMappingMapper.toDto(entity));
        });
        return result;
    }

    @Transactional
    public UserCdbtMappingDto saveUserCdbtMapping(UserCdbtMappingDto userCdbtMappingDto){
        return userCdbtMappingMapper.toDto(userCdbtMappingRepository.save(userCdbtMappingMapper.toEntity(userCdbtMappingDto)));
    }
}
