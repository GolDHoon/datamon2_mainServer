package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.SmsVerificationDto;
import com.datamon.datamon2.entity.SmsVerificationEntity;
import com.datamon.datamon2.mapper.repository.SmsVerificationMapper;
import com.datamon.datamon2.repository.jpa.SmsVerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmsVerificationService {
    private SmsVerificationRepository smsVerificationRepository;
    private SmsVerificationMapper smsVerificationMapper;

    public SmsVerificationService(SmsVerificationRepository smsVerificationRepository, SmsVerificationMapper smsVerificationMapper) {
        this.smsVerificationRepository = smsVerificationRepository;
        this.smsVerificationMapper = smsVerificationMapper;
    }

    @Transactional(readOnly = true)
    public SmsVerificationDto getSmsVerificationById(String id){
        return smsVerificationMapper.toDto(smsVerificationRepository.findById(id).orElse(new SmsVerificationEntity()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SmsVerificationDto save(SmsVerificationDto smsVerificationDto){
        return smsVerificationMapper.toDto(smsVerificationRepository.save(smsVerificationMapper.toEntity(smsVerificationDto)));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(String id){
        smsVerificationRepository.deleteById(id);
    }
}
