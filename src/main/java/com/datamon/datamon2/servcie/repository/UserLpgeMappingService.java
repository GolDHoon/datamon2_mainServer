package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserLpgeMappingDto;
import com.datamon.datamon2.dto.repository.embeddable.UserLpgeMappingEntityIdDto;
import com.datamon.datamon2.entity.UserLpgeMappingEntity;
import com.datamon.datamon2.entity.embeddable.UserLpgeMappingEntityId;
import com.datamon.datamon2.mapper.repository.UserLpgeMappingMapper;
import com.datamon.datamon2.mapper.repository.embeddable.UserLpgeMappingEntityIdMapper;
import com.datamon.datamon2.repository.jpa.UserLpgeMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserLpgeMappingService {
    private UserLpgeMappingRepository userLpgeMappingRepository;
    private UserLpgeMappingMapper userLpgeMappingMapper;
    private UserLpgeMappingEntityIdMapper userLpgeMappingEntityIdMapper;

    public UserLpgeMappingService(UserLpgeMappingRepository userLpgeMappingRepository, UserLpgeMappingMapper userLpgeMappingMapper, UserLpgeMappingEntityIdMapper userLpgeMappingEntityIdMapper) {
        this.userLpgeMappingRepository = userLpgeMappingRepository;
        this.userLpgeMappingMapper = userLpgeMappingMapper;
        this.userLpgeMappingEntityIdMapper = userLpgeMappingEntityIdMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserLpgeMappingDto saveUserLpgeMapping(UserLpgeMappingDto userLpgeMappingDto) throws Exception{
        UserLpgeMappingEntity userLpgeMappingEntity = new UserLpgeMappingEntity();
        UserLpgeMappingEntityId userLpgeMappingEntityId = new UserLpgeMappingEntityId();

        userLpgeMappingEntityId.setLpgeCode(userLpgeMappingDto.getLpgeCode());
        userLpgeMappingEntityId.setUserId(userLpgeMappingDto.getUserId());
        userLpgeMappingEntity.setId(userLpgeMappingEntityId);

        return userLpgeMappingMapper.toDto(userLpgeMappingRepository.save(userLpgeMappingEntity));
    }

    @Transactional(readOnly = true)
    public List<UserLpgeMappingEntityIdDto> getUserLpgeMappingByUserId(int userId){
        List<UserLpgeMappingEntityIdDto> result = new ArrayList<>();
        userLpgeMappingRepository.findById_UserId(userId).forEach(entity -> {
            result.add(userLpgeMappingEntityIdMapper.toDto(entity.getId()));
        });
        return result;
    }
}
