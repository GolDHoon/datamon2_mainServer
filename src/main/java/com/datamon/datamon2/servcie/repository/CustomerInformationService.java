package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.entity.CustomerInformationEntity;
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
        CustomerInformationEntity customerInformationEntity = new CustomerInformationEntity();
        customerInformationEntity.setLpgeCode(customerInformationDto.getLpgeCode());
        customerInformationEntity.setUtmSourse(customerInformationDto.getUtmSourse());
        customerInformationEntity.setUtmMedium(customerInformationDto.getUtmMedium());
        customerInformationEntity.setUtmCampaign(customerInformationDto.getUtmCampaign());
        customerInformationEntity.setUtmTerm(customerInformationDto.getUtmTerm());
        customerInformationEntity.setUtmContent(customerInformationDto.getUtmContent());
        customerInformationEntity.setIp(customerInformationDto.getIp());
        customerInformationEntity.setUseYn(customerInformationDto.getUseYn());
        customerInformationEntity.setDelYn(customerInformationDto.getDelYn());
        customerInformationEntity.setCreateDate(customerInformationDto.getCreateDate());
        customerInformationEntity.setCreateId(customerInformationDto.getCreateId());
        customerInformationEntity.setModifyDate(customerInformationDto.getModifyDate());
        customerInformationEntity.setModifyId(customerInformationDto.getModifyId());
        customerInformationEntity.setDeleteDate(customerInformationDto.getDeleteDate());
        customerInformationEntity.setDeleteId(customerInformationDto.getDeleteId());

        return customerInformationMapper.toDto(customerInformationRepository.save(customerInformationEntity));
    }

}
