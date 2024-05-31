package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.CustomerBasicConsultationDto;
import com.datamon.datamon2.entity.CustomerBasicConsultationEntity;
import com.datamon.datamon2.mapper.repository.CustomerBasicConsultationMapper;
import com.datamon.datamon2.repository.jpa.CustomerBasicConsultationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
//        CustomerBasicConsultationEntity customerBasicConsultationEntity = new CustomerBasicConsultationEntity();
//
//        customerBasicConsultationEntity.setCustId(customerBasicConsultationDto.getCustId());
//        customerBasicConsultationEntity.setKey(customerBasicConsultationDto.getKey());
//        customerBasicConsultationEntity.setValue(customerBasicConsultationDto.getValue());
//        customerBasicConsultationEntity.setCreateDate(customerBasicConsultationDto.getCreateDate());
//        customerBasicConsultationEntity.setCreateId(customerBasicConsultationDto.getCreateId());
//        customerBasicConsultationEntity.setModiftyDate(customerBasicConsultationDto.getModiftyDate());
//        customerBasicConsultationEntity.setModifyId(customerBasicConsultationDto.getModifyId());
//        customerBasicConsultationEntity.setDeleteDate(customerBasicConsultationDto.getDeleteDate());
//        customerBasicConsultationEntity.setDeleteId(customerBasicConsultationDto.getDeleteId());
        
        return customerBasicConsultationMapper.toDto(customerBasicConsultationRepository.save(customerBasicConsultationMapper.toEntity(customerBasicConsultationDto)));
    }
}
