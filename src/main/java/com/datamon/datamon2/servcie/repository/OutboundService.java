package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.OutboundDto;
import com.datamon.datamon2.entity.OutboundEntity;
import com.datamon.datamon2.mapper.repository.OutboundMapper;
import com.datamon.datamon2.repository.jpa.OutboundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutboundService {
    private OutboundRepository outboundRepository;
    private OutboundMapper outboundMapper;

    public OutboundService(OutboundRepository outboundRepository, OutboundMapper outboundMapper) {
        this.outboundRepository = outboundRepository;
        this.outboundMapper = outboundMapper;
    }

    public List<OutboundDto> getOutboundByUserId(int userId){
        List<OutboundDto> result = new ArrayList<>();
        outboundRepository.findByUserId(userId).forEach(entity -> result.add(outboundMapper.toDto(entity)));
        return result;
    }

    @Transactional(readOnly = true)
    public OutboundDto getOutboundByCustId (String custId){
        return outboundMapper.toDto(outboundRepository.findByCustId(custId).orElse(new OutboundEntity()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OutboundDto save (OutboundDto outboundDto){
        return outboundMapper.toDto(outboundRepository.save(outboundMapper.toEntity(outboundDto)));
    }
}
