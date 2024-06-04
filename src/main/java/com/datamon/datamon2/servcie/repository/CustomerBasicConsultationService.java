package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.CustomerBasicConsultationDto;
import com.datamon.datamon2.entity.CustomerBasicConsultationEntity;
import com.datamon.datamon2.mapper.repository.CustomerBasicConsultationMapper;
import com.datamon.datamon2.repository.jpa.CustomerBasicConsultationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerBasicConsultationService {
    private CustomerBasicConsultationRepository customerBasicConsultationRepository;
    private CustomerBasicConsultationMapper customerBasicConsultationMapper;

    public CustomerBasicConsultationService(CustomerBasicConsultationRepository customerBasicConsultationRepository, CustomerBasicConsultationMapper customerBasicConsultationMapper) {
        this.customerBasicConsultationRepository = customerBasicConsultationRepository;
        this.customerBasicConsultationMapper = customerBasicConsultationMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CustomerBasicConsultationDto saveCustomerBasicConsultation(CustomerBasicConsultationDto customerBasicConsultationDto){
        return customerBasicConsultationMapper.toDto(customerBasicConsultationRepository.save(customerBasicConsultationMapper.toEntity(customerBasicConsultationDto)));
    }

    @Transactional(readOnly = true)
    public List<CustomerBasicConsultationDto> getCustomerBasicConsultationBycustIdList(List<Long> custIds){
        List<CustomerBasicConsultationDto> result = new ArrayList<>();
        customerBasicConsultationRepository.findByCustIdIn(custIds).forEach(entity -> {
            result.add(customerBasicConsultationMapper.toDto(entity));
        });
        return result;
    }
}
