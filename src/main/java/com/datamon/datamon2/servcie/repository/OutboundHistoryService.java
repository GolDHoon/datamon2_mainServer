package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.OutboundHistoryDto;
import com.datamon.datamon2.entity.OutboundHistoryEntity;
import com.datamon.datamon2.mapper.repository.OutboundHistoryMapper;
import com.datamon.datamon2.repository.jpa.OutboundHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutboundHistoryService {
    private OutboundHistoryRepository outboundHistoryRepository;
    private OutboundHistoryMapper outboundHistoryMapper;

    public OutboundHistoryService(OutboundHistoryRepository outboundHistoryRepository, OutboundHistoryMapper outboundHistoryMapper) {
        this.outboundHistoryRepository = outboundHistoryRepository;
        this.outboundHistoryMapper = outboundHistoryMapper;
    }

    @Transactional(readOnly = true)
    public List<OutboundHistoryDto> getOutboundHistoryByOriginalIdx(String originalIdx){
        List<OutboundHistoryDto> result = new ArrayList<>();
        outboundHistoryRepository.findByOriginalIdx(originalIdx).forEach(enttiy -> result.add(outboundHistoryMapper.toDto(enttiy)));
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OutboundHistoryDto save (OutboundHistoryDto outboundHistoryDto){
        return outboundHistoryMapper.toDto(outboundHistoryRepository.save(outboundHistoryMapper.toEntity(outboundHistoryDto)));
    }
}
