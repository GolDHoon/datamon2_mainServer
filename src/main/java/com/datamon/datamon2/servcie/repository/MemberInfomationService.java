package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.MemberInfomationDto;
import com.datamon.datamon2.mapper.repository.MemberInfomationMapper;
import com.datamon.datamon2.repository.jpa.MemberInfomationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberInfomationService {
    private MemberInfomationRepository memberInfomationRepository;
    private MemberInfomationMapper memberInfomationMapper;

    public MemberInfomationService(MemberInfomationRepository memberInfomationRepository, MemberInfomationMapper memberInfomationMapper) {
        this.memberInfomationRepository = memberInfomationRepository;
        this.memberInfomationMapper = memberInfomationMapper;
    }

    @Transactional(readOnly = true)
    public MemberInfomationDto getMemberInfomationByUserId(int userId){
        return memberInfomationRepository.findByUserId(userId).map(memberInfomationMapper::toDto).orElse(new MemberInfomationDto());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberInfomationDto save(MemberInfomationDto memberInfomationDto){
        return memberInfomationMapper.toDto(memberInfomationRepository.save(memberInfomationMapper.toEntity(memberInfomationDto)));
    }
}
