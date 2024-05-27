package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService {
    private UserBaseService userBaseService;

    public DeveloperService(UserBaseService userBaseService) {
        this.userBaseService = userBaseService;
    }

    public List<UserBaseDto> getUserList (){
        List<UserBaseDto> userBaseList = userBaseService.getUserBaseList();

        return userBaseList;
    }

    public String setPassword(int idx, String userPw){
        EncryptionUtil encryptionUtil = new EncryptionUtil();

        UserBaseDto userBaseById = userBaseService.getUserBaseById(idx);

        String salt = encryptionUtil.getSalt();
        String encryptPw = encryptionUtil.getSHA256WithSalt(userPw, salt);

        userBaseById.setUserPw(encryptPw);
        userBaseById.setSalt(salt);

        UserBaseDto save = userBaseService.save(userBaseById);

        return save.getUserPw();
    }
}
