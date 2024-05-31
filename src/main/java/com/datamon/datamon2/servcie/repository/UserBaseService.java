package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.entity.UserBaseEntity;
import com.datamon.datamon2.mapper.repository.UserBaseMapper;
import com.datamon.datamon2.repository.jpa.UserBaseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBaseService {
    @Value("${system.Id}")
    String systemId;
    private UserBaseRepository userBaseRepository;
    private UserBaseMapper userBaseMapper;

    public UserBaseService(UserBaseRepository userBaseRepository, UserBaseMapper userBaseMapper) {
        this.userBaseRepository = userBaseRepository;
        this.userBaseMapper = userBaseMapper;
    }

    public UserBaseDto getUserBaseByUserId(String userId){
        UserBaseEntity userBaseEntity = userBaseRepository.findByUserId(userId).orElse(new UserBaseEntity());
        return userBaseMapper.toDto(userBaseEntity);
    }

    public List<UserBaseDto> getUserBaseList(){
        List<UserBaseEntity> all = userBaseRepository.findAll();
        List<UserBaseDto> result = new ArrayList<>();

        all.forEach( (entity) -> {
            UserBaseDto userBaseDto = userBaseMapper.toDto(entity);
            result.add(userBaseDto);
        });

        return result;
    }

    @PostConstruct
    public void init() {
        UserBaseDto userBaseByUserId = getUserBaseByUserId(systemId);
        CommonCodeCache.setSystemIdIdx(userBaseByUserId.getIdx());
    }

    @Transactional(readOnly = true)
    public UserBaseDto getUserBaseById(int idx){
        return userBaseMapper.toDto(userBaseRepository.findById(idx).orElse(new UserBaseEntity()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserBaseDto save (UserBaseDto userBaseDto){
        return userBaseMapper.toDto(userBaseRepository.save(userBaseMapper.toEntity(userBaseDto)));
    }
}
