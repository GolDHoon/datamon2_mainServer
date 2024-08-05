package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.entity.CustomerInformationEntity;
import com.datamon.datamon2.mapper.repository.CustomerInformationMapper;
import com.datamon.datamon2.repository.jpa.CustomerInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerInformationService {
    private CustomerInformationRepository customerInformationRepository;
    private CustomerInformationMapper customerInformationMapper;

    public CustomerInformationService(CustomerInformationRepository customerInformationRepository, CustomerInformationMapper customerInformationMapper) {
        this.customerInformationRepository = customerInformationRepository;
        this.customerInformationMapper = customerInformationMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CustomerInformationDto save(CustomerInformationDto customerInformationDto){
        return customerInformationMapper.toDto(customerInformationRepository.save(customerInformationMapper.toEntity(customerInformationDto)));
    }

    @Transactional(readOnly = true)
    public List<CustomerInformationDto> getCustomerInformationByCdbtLowCode(String cdbtLowCode){
        List<CustomerInformationDto> result = new ArrayList<>();

        customerInformationRepository.findByCdbtLowCode(cdbtLowCode).forEach(entity -> {
            result.add(customerInformationMapper.toDto(entity));
        });

        return result;
    }

    @Transactional(readOnly = true)
    public CustomerInformationDto getCustomerInformationById(String idx){
        return customerInformationMapper.toDto(customerInformationRepository.findById(idx).orElse(new CustomerInformationEntity()));
    }

}
