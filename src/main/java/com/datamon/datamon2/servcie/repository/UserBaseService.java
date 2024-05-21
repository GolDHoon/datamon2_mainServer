package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.entity.UserBase;
import com.datamon.datamon2.mapper.repository.UserBaseMapper;
import com.datamon.datamon2.repository.jpa.UserBaseRepository;
import com.datamon.datamon2.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBaseService {
    private UserBaseRepository userBaseRepository;
    private UserBaseMapper userBaseMapper;

    public UserBaseService(UserBaseRepository userBaseRepository, UserBaseMapper userBaseMapper) {
        this.userBaseRepository = userBaseRepository;
        this.userBaseMapper = userBaseMapper;
    }

    public UserBaseDto getUserBaseByUserId(String userId){
        UserBase userBase = userBaseRepository.findByUserId(userId).orElse(new UserBase());
        return userBaseMapper.toDto(userBase);
    }

    public List<UserBaseDto> getUserBaseList(){
        List<UserBase> all = userBaseRepository.findAll();
        List<UserBaseDto> result = new ArrayList<>();

        all.forEach( (entity) -> {
            UserBaseDto userBaseDto = userBaseMapper.toDto(entity);
            result.add(userBaseDto);
        });

        return result;
    }

    public UserBaseDto getUserBaseById(int idx){
        return userBaseMapper.toDto(userBaseRepository.findById(idx).orElse(new UserBase()));
    }

    public UserBaseDto save (UserBaseDto userBaseDto){
        UserBase entity = userBaseMapper.toEntity(userBaseDto);
        return userBaseMapper.toDto(userBaseRepository.save(entity));
    }
}
