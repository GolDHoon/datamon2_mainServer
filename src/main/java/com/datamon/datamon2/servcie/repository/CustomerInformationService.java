package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.mapper.repository.CustomerInformationMapper;
import com.datamon.datamon2.repository.jpa.CustomerInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerInformationService {
    private CustomerInformationRepository customerInformationRepository;
    private CustomerInformationMapper customerInformationMapper;

    public CustomerInformationService(CustomerInformationRepository customerInformationRepository, CustomerInformationMapper customerInformationMapper) {
        this.customerInformationRepository = customerInformationRepository;
        this.customerInformationMapper = customerInformationMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CustomerInformationDto saveCustomerInformation(CustomerInformationDto customerInformationDto){
        return customerInformationMapper.toDto(customerInformationRepository.save(customerInformationMapper.toEntity(customerInformationDto)));
    }

}
