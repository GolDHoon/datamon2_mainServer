package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserLpgeMappingDto;
import com.datamon.datamon2.mapper.repository.UserLpgeMappingMapper;
import com.datamon.datamon2.repository.jpa.UserLpgeMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLpgeMappingService {
    private UserLpgeMappingRepository userLpgeMappingRepository;
    private UserLpgeMappingMapper userLpgeMappingMapper;

    public UserLpgeMappingService(UserLpgeMappingRepository userLpgeMappingRepository, UserLpgeMappingMapper userLpgeMappingMapper) {
        this.userLpgeMappingRepository = userLpgeMappingRepository;
        this.userLpgeMappingMapper = userLpgeMappingMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserLpgeMappingDto saveUserLpgeMapping(UserLpgeMappingDto userLpgeMappingDto) throws Exception{
        return userLpgeMappingMapper.toDto(userLpgeMappingRepository.save(userLpgeMappingMapper.toEntity(userLpgeMappingDto)));
    }
}
