package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.EmailVerificationDto;
import com.datamon.datamon2.entity.EmailVerificationEntity;
import com.datamon.datamon2.mapper.repository.EmailVerificationMapper;
import com.datamon.datamon2.repository.jpa.EmailVerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailVerificationService {
    private EmailVerificationRepository emailVerificationRepository;
    private EmailVerificationMapper emailVerificationMapper;

    public EmailVerificationService(EmailVerificationRepository emailVerificationRepository, EmailVerificationMapper emailVerificationMapper) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.emailVerificationMapper = emailVerificationMapper;
    }

    @Transactional(readOnly = true)
    public EmailVerificationDto getEmailVerificationById(String id){
        return emailVerificationMapper.toDto(emailVerificationRepository.findById(id).orElse(new EmailVerificationEntity()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EmailVerificationDto save(EmailVerificationDto emailVerificationDto){
        return emailVerificationMapper.toDto(emailVerificationRepository.save(emailVerificationMapper.toEntity(emailVerificationDto)));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(String id){
        emailVerificationRepository.deleteById(id);
    }
}
