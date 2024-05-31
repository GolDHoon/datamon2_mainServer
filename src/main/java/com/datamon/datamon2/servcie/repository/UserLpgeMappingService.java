package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserLpgeMappingDto;
import com.datamon.datamon2.entity.UserLpgeMappingEntity;
import com.datamon.datamon2.entity.embeddable.UserLpgeMappingEntityId;
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
        UserLpgeMappingEntity userLpgeMappingEntity = new UserLpgeMappingEntity();
        UserLpgeMappingEntityId userLpgeMappingEntityId = new UserLpgeMappingEntityId();

//        userLpgeMappingEntityId.setLpgeCode(userLpgeMappingDto.getLpgeCode());
//        userLpgeMappingEntityId.setUserId(userLpgeMappingDto.getUserId());
//        userLpgeMappingEntity.setId(userLpgeMappingEntityId);
        userLpgeMappingEntity.setLpgeCode(userLpgeMappingDto.getLpgeCode());
        userLpgeMappingEntity.setUserId(userLpgeMappingDto.getUserId());

        return userLpgeMappingMapper.toDto(userLpgeMappingRepository.save(userLpgeMappingEntity));
    }
}
